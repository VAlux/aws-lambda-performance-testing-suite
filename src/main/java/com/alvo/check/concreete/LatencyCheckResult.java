package com.alvo.check.concreete;

import com.alvo.check.CheckResult;

public final class LatencyCheckResult extends CheckResult {

  private long latency;
  private static final String LATENCY_CHECK_NAME = "Latency Check";

  public LatencyCheckResult(long latency, boolean success) {
    super(LATENCY_CHECK_NAME, success);
    this.latency = latency;
  }

  public LatencyCheckResult() {
    this(0, true);
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }

  @Override
  public String getResultMessage() {
    return String.format("request execution time: %s ms", latency);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LatencyCheckResult that = (LatencyCheckResult) o;
    return latency == that.latency &&
           success == that.success &&
           getResultMessage().equals(that.getResultMessage()) &&
           getCheckName().equals(that.getCheckName());
  }

  @Override
  public int hashCode() {
    int result = (int) (latency ^ (latency >>> 32));
    result = 31 * result + (success ? 1 : 0);
    return result;
  }
}
