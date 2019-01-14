package com.alvo.check;

import com.alvo.http.HttpResponse;
import com.alvo.http.HttpResponseTranslator;
import com.alvo.http.PerformanceCheckResultResponse;
import com.alvo.infrastructure.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public final class PerformanceCheckExecutionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceCheckExecutionService.class);

  private static final String CHECK_LOCATION_PACKAGE = PropertiesLoader.getProperty("check.location.package");

  private PerformanceCheckExecutor checkExecutor;

  public PerformanceCheckExecutionService() {
    this.checkExecutor = new PerformanceCheckExecutor();
  }

  public List<Optional<CheckResult>> getCheckResults(String checkClassName, String payload, int repetitions) {
    try {
      final Class<? extends PerformanceCheck> checkClass =
          Class.forName(CHECK_LOCATION_PACKAGE + "." + checkClassName).asSubclass(PerformanceCheck.class);

      LOGGER.info("Started executing check using checker class: [{}] repetitions amount: [{}]", checkClassName, repetitions);
      List<Optional<CheckResult>> result = checkExecutor.execute(checkClass, payload, repetitions);
      LOGGER.info("Finished executing check for checker class: [{}]", checkClassName);
      return result;
    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
      LOGGER.error("No suitable check class for: {}.{}", CHECK_LOCATION_PACKAGE, checkClassName);
    }
    return Collections.singletonList(Optional.empty());
  }

  public HttpResponse getReducedResponse(List<CheckResult> checkResults) {
    LOGGER.info("Started reduction of the response payload with [{}] check results", checkResults.size());

    final CheckResultReducer<? extends CheckResult> resultReducer = checkResults.stream()
        .findAny()
        .map(result -> CheckResultReductionRegistry.getReducerForResult(result.getClass()))
        .orElse(null);

    if (resultReducer != null) {
      @SuppressWarnings("unchecked")
      final List<CheckResult> reduced = Collections.singletonList(
          checkResults.stream()
              .reduce((BinaryOperator<CheckResult>) resultReducer.reduce())
              .orElse(null));

      return HttpResponseTranslator.from(new PerformanceCheckResultResponse(reduced));
    }
    LOGGER.error("Can't find corresponding reducer. The results will be returned as-is");
    return HttpResponseTranslator.from(new PerformanceCheckResultResponse(checkResults));
  }
}