package com.add2num.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core big-number addition class.
 *
 * <p>Implements the elementary-school digit-by-digit algorithm on string operands,
 * allowing numbers of arbitrary size without integer overflow.
 *
 * <p>Packaged as {@code add2num-core-0.0.1.jar} and reused by the
 * {@code add2num-web} Spring Boot application (Task 2).
 *
 * <p>Usage:
 * <pre>{@code
 *   MyBigNumber calc = new MyBigNumber();
 *   MyBigNumber.AdditionResult result = calc.sum("1234", "897");
 *   System.out.println(result.getResult()); // "2131"
 * }</pre>
 */
public class MyBigNumber {

    private static final Logger log = LoggerFactory.getLogger(MyBigNumber.class);

    // ── Inner DTO: one step ─────────────────────────────────────────────────

    /** Represents a single step in the digit-by-digit addition process. */
    public static class AdditionStep {
        private final int    stepNumber;
        private final int    digit1;
        private final int    digit2;
        private final int    carryIn;
        private final int    total;
        private final int    digitWritten;
        private final int    carryOut;
        private final String partialResult;   // running result so far (correct order)

        public AdditionStep(int stepNumber, int digit1, int digit2,
                            int carryIn, int total, int digitWritten,
                            int carryOut, String partialResult) {
            this.stepNumber    = stepNumber;
            this.digit1        = digit1;
            this.digit2        = digit2;
            this.carryIn       = carryIn;
            this.total         = total;
            this.digitWritten  = digitWritten;
            this.carryOut      = carryOut;
            this.partialResult = partialResult;
        }

        public int    getStepNumber()    { return stepNumber; }
        public int    getDigit1()        { return digit1; }
        public int    getDigit2()        { return digit2; }
        public int    getCarryIn()       { return carryIn; }
        public int    getTotal()         { return total; }
        public int    getDigitWritten()  { return digitWritten; }
        public int    getCarryOut()      { return carryOut; }
        public String getPartialResult() { return partialResult; }
    }

    // ── Inner DTO: full result ──────────────────────────────────────────────

    /** Holds the final answer and every intermediate step. */
    public static class AdditionResult {
        private final String             stn1;
        private final String             stn2;
        private final String             result;
        private final List<AdditionStep> steps;

        public AdditionResult(String stn1, String stn2,
                              String result, List<AdditionStep> steps) {
            this.stn1   = stn1;
            this.stn2   = stn2;
            this.result = result;
            this.steps  = Collections.unmodifiableList(steps);
        }

        public String             getStn1()   { return stn1; }
        public String             getStn2()   { return stn2; }
        public String             getResult() { return result; }
        public List<AdditionStep> getSteps()  { return steps; }
    }

    // ── Public API ──────────────────────────────────────────────────────────

    /**
     * Adds two non-negative integers represented as digit-only strings.
     *
     * <p>Assumption: both parameters contain only valid digit characters (0–9).
     * No sign, no whitespace, no decimal point.
     *
     * @param stn1 first operand as a string of digits
     * @param stn2 second operand as a string of digits
     * @return {@link AdditionResult} with the final answer and step-by-step breakdown
     */
    public AdditionResult sum(String stn1, String stn2) {
        log.info("sum() start: stn1='{}', stn2='{}'", stn1, stn2);

        int i     = stn1.length() - 1;
        int j     = stn2.length() - 1;
        int carry = 0;
        int step  = 1;

        StringBuilder      rawDigits = new StringBuilder();
        List<AdditionStep> steps     = new ArrayList<>();

        while (i >= 0 || j >= 0 || carry > 0) {
            int digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            int digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            int total        = digit1 + digit2 + carry;
            int digitWritten = total % 10;
            int carryOut     = total / 10;

            rawDigits.append(digitWritten);

            // Partial result = digits collected so far, reversed to correct order
            String partial = rawDigits.reverse().toString();
            rawDigits.reverse(); // restore for next append

            log.info("Step {}: {} + {} + carry({}) = {} => write {}, carry_out={}",
                    step, digit1, digit2, carry, total, digitWritten, carryOut);

            steps.add(new AdditionStep(
                    step, digit1, digit2, carry,
                    total, digitWritten, carryOut, partial));

            carry = carryOut;
            i--;
            j--;
            step++;
        }

        String finalResult = rawDigits.reverse().toString();
        log.info("sum() done: '{}' + '{}' = '{}'", stn1, stn2, finalResult);

        return new AdditionResult(stn1, stn2, finalResult, steps);
    }
}
