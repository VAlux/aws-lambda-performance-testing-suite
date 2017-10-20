package com.meteogroup.check;

import com.meteogroup.check.concreete.LatencyCheckResult;
import com.meteogroup.check.concreete.LatencyCheckResultReducer;

import java.util.HashMap;
import java.util.Map;

public final class CheckResultReductionRegistry {

  private static Map<Class<? extends CheckResult>, CheckResultReducer<? extends CheckResult>> registry;

  static {
    registry = new HashMap<>();
    registry.put(LatencyCheckResult.class, new LatencyCheckResultReducer());
  }

  public static CheckResultReducer<? extends CheckResult> getReducerForResult(Class<? extends CheckResult> resultClass) {
    return registry.get(resultClass);
  }
}
