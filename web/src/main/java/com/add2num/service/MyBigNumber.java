package com.add2num.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core big-number addition service.
 *
 * <p>Mirrors the C# {@code MyBigNumber} class from the {@code core} branch.
 * Implements the elementary-school digit-by-digit algorithm on string operands
 * so numbers of arbitrary size can be handled without overflow.
 */
public class MyBigNumber {

    /**
     * Represents one step of the elementary-school addition process.
     */
    public static class AdditionStep {
        private final int stepNumber;
        private final int digit1;
        private final int digit2;
        private final int carryIn;
        private final int total;
        private final int digitWritten;
        private final int carryOut;
        private final String partialResult;

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

    /**
     * Full result of one addition operation, including the steps list and the
     * final answer string.
     */
    public static class AdditionResult {
        private final String stn1;
        private final String stn2;
        private final String result;
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

    /**
     * Adds two non-negative integer strings and returns an {@link AdditionResult}
     * that contains the answer and every intermediate step for UI display.
     *
     * @param stn1 first operand (digit characters only)
     * @param stn2 second operand (digit characters only)
     * @return an {@link AdditionResult} with the final answer and step-by-step breakdown
     */
    public AdditionResult sum(String stn1, String stn2) {
        int i     = stn1.length() - 1;
        int j     = stn2.length() - 1;
        int carry = 0;
        int step  = 1;

        StringBuilder rawDigits = new StringBuilder();
        List<AdditionStep> steps = new ArrayList<>();

        while (i >= 0 || j >= 0 || carry > 0) {
            int digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            int digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            int total        = digit1 + digit2 + carry;
            int digitWritten = total % 10;
            int carryOut     = total / 10;

            rawDigits.append(digitWritten);

            // Build partial result so far (reversed to show correct order)
            String partial = rawDigits.reverse().toString();
            rawDigits.reverse(); // restore original order for next iteration

            steps.add(new AdditionStep(step, digit1, digit2,
                    carry, total, digitWritten, carryOut, partial));

            carry = carryOut;
            i--;
            j--;
            step++;
        }

        // Final result = reverse of accumulated digits
        String finalResult = rawDigits.reverse().toString();

        return new AdditionResult(stn1, stn2, finalResult, steps);
    }
}
