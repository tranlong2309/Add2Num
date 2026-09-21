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
public class    MyBigNumber {

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
     * Strict requirement implementation: Adds two large numbers and returns the result string.
     * @param stn1 first operand
     * @param stn2 second operand
     * @return the sum as a string
     */
    public String sum(String stn1, String stn2) {
        int len1 = stn1.length();
        int len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        
        // Cấp phát mảng char một lần duy nhất. Kích thước maxLen + 1 để chứa carry cuối (nếu có).
        // Cách này giải quyết triệt để bài toán 1 tỷ số, tối ưu hoàn toàn RAM (O(N) memory), không cần dùng StringBuilder.reverse().
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen;
        
        int i = len1 - 1;
        int j = len2 - 1;
        int carry = 0;
        int step = 1;

        // Khai báo biến bên ngoài vòng lặp theo chuẩn Clean Code
        int digit1, digit2, total, digitWritten, carryOut;

        // Dùng vòng for thay vì while, bổ sung ngoặc () rõ ràng cho các biểu thức điều kiện
        for (; (i >= 0) || (j >= 0) || (carry > 0); i--, j--, step++) {
            // Không đổi String sang Int bằng các hàm parse nặng nề, chỉ tính toán trực tiếp trên mã ASCII của char
            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            total        = digit1 + digit2 + carry;
            digitWritten = total % 10;
            carryOut     = total / 10;

            // Ghi trực tiếp ký tự char vào mảng từ phải qua trái
            resultChars[writePos] = (char) (digitWritten + '0');
            
            log.info("Step {}: {} + {} + carry({}) = {} => write {}, carry_out={}",
                    step, digit1, digit2, carry, total, digitWritten, carryOut);

            carry = carryOut;
            writePos--;
        }

        // Khởi tạo String một lần duy nhất từ mảng char, không cần reverse.
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }

    /**
     * Dành riêng cho giao diện Web (Task 2) để vẽ Animation.
     * Thuật toán tối ưu tương tự hàm sum(), nhưng lưu thêm các bước vào List.
     */
    public AdditionResult sumWithSteps(String stn1, String stn2) {
        log.info("sumWithSteps() start: stn1='{}', stn2='{}'", stn1, stn2);

        int len1 = stn1.length();
        int len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen;
        
        int i = len1 - 1;
        int j = len2 - 1;
        int carry = 0;
        int step = 1;

        List<AdditionStep> steps = new ArrayList<>();

        int digit1, digit2, total, digitWritten, carryOut;
        String partial;

        for (; (i >= 0) || (j >= 0) || (carry > 0); i--, j--, step++) {
            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            total        = digit1 + digit2 + carry;
            digitWritten = total % 10;
            carryOut     = total / 10;

            resultChars[writePos] = (char) (digitWritten + '0');

            // Cắt ra chuỗi kết quả một phần để UI hiển thị (không tốn chi phí reverse)
            partial = new String(resultChars, writePos, maxLen + 1 - writePos);

            steps.add(new AdditionStep(
                    step, digit1, digit2, carry,
                    total, digitWritten, carryOut, partial));

            carry = carryOut;
            writePos--;
        }

        String finalResult = new String(resultChars, writePos + 1, maxLen - writePos);
        log.info("sumWithSteps() done: '{}' + '{}' = '{}'", stn1, stn2, finalResult);

        return new AdditionResult(stn1, stn2, finalResult, steps);
    }
}
