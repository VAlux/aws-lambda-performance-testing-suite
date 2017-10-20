package com.meteogroup.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerformanceCheckExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceCheckExecutor.class);

  public List<Optional<CheckResult>> execute(Class<? extends PerformanceCheck> targetCheckClass, String payload, int repetitions)
      throws IllegalAccessException, InstantiationException {

    if (repetitions == 1) {
      LOGGER.info("Started single repetition using [{}] checker", targetCheckClass);
      return execute(targetCheckClass, payload);
    } else {
      PerformanceCheck check = targetCheckClass.newInstance();
      return IntStream
          .iterate(0, i -> i + 1)
          .limit(repetitions)
          .peek(i -> LOGGER.info("Started repetition: [{}] using [{}] checker", i + 1, targetCheckClass.getName()))
          .mapToObj(i -> check.execute(payload))
          .collect(Collectors.toList());
    }
  }

  private List<Optional<CheckResult>> execute(Class<? extends PerformanceCheck> targetCheckClass, String payload)
      throws IllegalAccessException, InstantiationException {

    PerformanceCheck check = targetCheckClass.newInstance();
    return Collections.singletonList(check.execute(payload));
  }
}