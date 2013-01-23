/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bpd.rmi.api;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A task for calculating PI to the specified precision.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Pi implements Task<BigDecimal>, Serializable {

  private static final long serialVersionUID = 227L;

  /** Constants used in PI computation. */
  private static final BigDecimal FOUR = BigDecimal.valueOf(4);

  /** Rounding mode to use during PI computation. */
  private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

  /** Digits of precision after the decimal point. */
  private final int digits;

  /**
   * Constructs a task to calculate PI to the specified precision.
   * 
   * @param digits The precision, i.e. the number of digits after the decimal point.
   */
  public Pi(int digits) {
    this.digits = digits;
  }

  /**
   * Calculates PI.
   * 
   * @return PI computed to the given digits.
   */
  @Override
  public BigDecimal execute() {
    return computePi(this.digits);
  }

  /**
   * Computes the value of PI to the specified number of digits after the decimal point. The value
   * is computed using Machin's formula
   * 
   * <code>pi/4 = 4*arctan(1/5) - arctan(1/239)</code>
   * 
   * and a power series expansion of arctan(x) to sufficient precision.
   * 
   * @param digits The precision, i.e. the number of digits after the decimal point.
   * @return PI computed to the given digits.
   */
  public static BigDecimal computePi(int digits) {
    int scale = digits + 5;
    BigDecimal arctan1_5 = arctan(5, scale);
    BigDecimal arctan1_239 = arctan(239, scale);
    BigDecimal pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);
    return pi.setScale(digits, BigDecimal.ROUND_HALF_UP);
  }

  /**
   * Computes the value, in radians, of the arctangent of the inverse of the supplied integer to the
   * specified number of digits after the decimal point. The value is computed using the power
   * series expansion for the arc tangent
   * 
   * <code>arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + (x^9)/9 ...</code>
   * 
   * @param inverseX The inverse.
   * @param scale The scale.
   * @return The arctangent of the inverse of the supplied integer to the specified number of
   * digits.
   */
  public static BigDecimal arctan(int inverseX, int scale) {
    BigDecimal result, numer, term;
    BigDecimal invX = BigDecimal.valueOf(inverseX);
    BigDecimal invX2 = BigDecimal.valueOf(1L * inverseX * inverseX);

    numer = BigDecimal.ONE.divide(invX, scale, ROUNDING_MODE);

    result = numer;
    int i = 1;
    do {
      numer = numer.divide(invX2, scale, ROUNDING_MODE);
      int denom = 2 * i + 1;
      term = numer.divide(BigDecimal.valueOf(denom), scale, ROUNDING_MODE);
      if ((i % 2) == 0) {
        result = result.add(term);
      }
      else {
        result = result.subtract(term);
      }
      i++;
    }
    while (term.compareTo(BigDecimal.ZERO) != 0);
    return result;
  }
}
