package com.alvo.check;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface CheckResultReducer<T extends CheckResult> {
  BinaryOperator<T>reduce();
}
