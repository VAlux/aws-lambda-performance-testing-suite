package com.meteogroup.check.concreete;

import com.meteogroup.check.CheckResultReducer;

import java.util.function.BinaryOperator;

public final class LatencyCheckResultReducer implements CheckResultReducer<LatencyCheckResult> {
  @Override
  public BinaryOperator<LatencyCheckResult> reduce() {
    return (LatencyCheckResult first, LatencyCheckResult second) ->
        new LatencyCheckResult(first.getLatency() + second.getLatency(),
                               first.isSuccess() && second.isSuccess());
  }
}