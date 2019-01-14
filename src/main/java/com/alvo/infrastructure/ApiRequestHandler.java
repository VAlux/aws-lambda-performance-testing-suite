package com.alvo.infrastructure;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.alvo.check.CheckResult;
import com.alvo.check.PerformanceCheckExecutionService;
import com.alvo.http.HttpRequest;
import com.alvo.http.HttpResponse;
import com.alvo.http.HttpResponseTranslator;
import com.alvo.http.PerformanceCheckResultResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ApiRequestHandler implements RequestHandler<HttpRequest, HttpResponse> {

  private static final String CHECKS_TO_EXECUTE_PARAM_NAME = "checksToExecute";
  private static final String REPETITIONS_PARAM_NAME = "repetitions";
  private static final String PAYLOAD_PARAM_NAME = "payload";
  private static final String REDUCE_RESULTS_PARAM_NAME = "reduceResults";

  private PerformanceCheckExecutionService executionService;

  public ApiRequestHandler() {
    this.executionService = new PerformanceCheckExecutionService();
  }

  @Override
  public HttpResponse handleRequest(HttpRequest request, Context context) {
    Map<String, String> parameters = request.getQueryStringParameters();

    final int repetitions = Integer.parseInt(parameters.getOrDefault(REPETITIONS_PARAM_NAME, "1"));
    final String checkClassName = parameters.get(CHECKS_TO_EXECUTE_PARAM_NAME);
    final String payload = parameters.get(PAYLOAD_PARAM_NAME);
    final boolean reduceNeeded = Boolean.parseBoolean(parameters.getOrDefault(REDUCE_RESULTS_PARAM_NAME, "false"));

    final List<CheckResult> checkResults = executionService.getCheckResults(checkClassName, payload, repetitions).stream()
        .flatMap(checkResult -> checkResult.map(Stream::of).orElseGet(Stream::empty))
        .collect(Collectors.toList());

    if (reduceNeeded && checkResults.size() > 1) {
      return executionService.getReducedResponse(checkResults);
    } else {
      return HttpResponseTranslator.from(new PerformanceCheckResultResponse(checkResults));
    }
  }
}