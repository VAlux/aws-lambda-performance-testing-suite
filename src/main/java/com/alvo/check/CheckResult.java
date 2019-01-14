package com.alvo.check;

public abstract class CheckResult {

  private final String checkName;
  protected final boolean success;

  public CheckResult(String checkName, boolean success) {
    this.checkName = checkName;
    this.success = success;
  }

  public abstract String getResultMessage();

  public String getCheckName() {
    return checkName;
  }

  public boolean isSuccess() {
    return success;
  }
}
