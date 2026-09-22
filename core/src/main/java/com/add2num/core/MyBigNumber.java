package com.add2num.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adds arbitrarily large numbers represented as strings.
 *
 * <p>Uses right-to-left digit addition without integer overflow.
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

    // DTOs for calculation details

    /**
     * Represents one digit-addition step.
     */
    public static class AdditionStep {
        private final int    stepNumber;      // Step number, starting at 1
        private final int    digit1;          // Digit from the first number
        private final int    digit2;          // Digit from the second number
        private final int    carryIn;         // Carry from the previous step
        private final int    total;           // digit1 + digit2 + carryIn
        private final int    digitWritten;    // Digit written to the result
        private final int    carryOut;        // Carry to the next step
        private final String partialResult;   // Result built so far

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
     * Contains the operands, final result, and calculation steps.
     */
    public static class AdditionResult {
        private final String             stn1;    // First number
        private final String             stn2;    // Second number
        private final String             result;  // Final result
        private final List<AdditionStep> steps;   // Read-only calculation steps

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

    // Public API

    /**
     * Adds two large numbers represented as strings.
     * 
     * @param stn1 first operand
     * @param stn2 second operand
     * @return the sum as a string
     */
    public String sum(String stn1, String stn2) {
        int len1 = stn1.length();
        int len2 = stn2.length();
        // Find the number of digit positions to process.
        int maxLen = Math.max(len1, len2);
        
        // Allocate one extra position for a final carry.
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen; // Write from right to left.
        int carry = 0;         // Carry from the previous position.

        // Keep loop variables outside the loop.
        int i, j, digit1, digit2, total, digitWritten, carryOut;

        // Process digits from right to left until all digits and carry are consumed.
        for (int k = 0; (k < maxLen) || (carry > 0); k++) {
            // Read each number from right to left; missing digits are zero.
            i = len1 - 1 - k;
            j = len2 - 1 - k;

            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            total        = digit1 + digit2 + carry;
            digitWritten = total % 10;
            carryOut     = total / 10;

            resultChars[writePos] = (char) (digitWritten + '0');
            
            log.info("Step {}: {} + {} + carry({}) = {} => write {}, next carry={}",
                    k + 1, digit1, digit2, carry, total, digitWritten, carryOut);

            carry = carryOut;
            writePos--;
        }

        // Skip unused positions at the start of the array.
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }

    /**
      * Adds two numbers and returns the details of each digit-addition step.
     * 
      * @param stn1 first operand
      * @param stn2 second operand
      * @return the result and its calculation steps
     */
    public AdditionResult sumWithSteps(String stn1, String stn2) {
          log.info("Starting sumWithSteps(): stn1='{}', stn2='{}'", stn1, stn2);

        int len1 = stn1.length();
        int len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen;
        int carry = 0;

        // Store the details of each digit-addition step.
        List<AdditionStep> steps = new ArrayList<>();

        // Keep loop variables outside the loop.
        int i, j, digit1, digit2, total, digitWritten, carryOut;
        String partial; // Partial result at each step.

        for (int k = 0; (k < maxLen) || (carry > 0); k++) {
            i = len1 - 1 - k;
            j = len2 - 1 - k;

            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            total        = digit1 + digit2 + carry;
            digitWritten = total % 10;
            carryOut     = total / 10;

            resultChars[writePos] = (char) (digitWritten + '0');
            
            partial = new String(resultChars, writePos, maxLen + 1 - writePos);

            steps.add(new AdditionStep(
                    k + 1, digit1, digit2, carry,
                    total, digitWritten, carryOut, partial));

            carry = carryOut;
            writePos--;
        }

        String finalResult = new String(resultChars, writePos + 1, maxLen - writePos);
        log.info("Completed sumWithSteps(): '{}' + '{}' = '{}'", stn1, stn2, finalResult);

        return new AdditionResult(stn1, stn2, finalResult, steps);
    }
}
