package com.alvo.http;

import com.alvo.check.CheckResult;

import java.util.List;

public final class PerformanceCheckResultResponse {

  private final List<CheckResult> checkResults;

  public PerformanceCheckResultResponse(List<CheckResult> checkResults) {
    this.checkResults = checkResults;
  }

  public List<CheckResult> getCheckResults() {
    return checkResults;
  }
}
