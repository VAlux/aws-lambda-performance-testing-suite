package com.alvo.check;

import java.util.Optional;

@FunctionalInterface
public interface PerformanceCheck {
  Optional<CheckResult> execute(String payload);
}
