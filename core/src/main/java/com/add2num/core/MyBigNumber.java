package com.add2num.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp xử lý phép cộng 2 số siêu lớn (Big Number).
 *
 * <p>Triển khai thuật toán cộng từng chữ số theo chuẩn toán học cấp 1 trên nền tảng chuỗi (String),
 * cho phép cộng các số có độ dài tuỳ ý mà không bị tràn bộ nhớ nguyên (integer overflow).
 *
 * <p>Được đóng gói thành {@code add2num-core-0.0.1.jar} và tái sử dụng bởi
 * ứng dụng Spring Boot {@code add2num-web} (Task 2).
 *
 * <p>Cách sử dụng:
 * <pre>{@code
 *   MyBigNumber calc = new MyBigNumber();
 *   MyBigNumber.AdditionResult result = calc.sum("1234", "897");
 *   System.out.println(result.getResult()); // "2131"
 * }</pre>
 */
public class MyBigNumber {

    private static final Logger log = LoggerFactory.getLogger(MyBigNumber.class);

    // ── DTO Cấu trúc dữ liệu bên trong ─────────────────────────────────────────────────

    /** 
     * Đại diện cho một bước xử lý chi tiết trong quá trình cộng từng chữ số. 
     * (Dùng để hiển thị lịch sử tính toán ra màn hình Console hoặc vẽ Animation trên Website)
     */
    public static class AdditionStep {
        private final int    stepNumber;      // Số thứ tự của bước tính toán (bắt đầu từ 1)
        private final int    digit1;          // Chữ số lấy từ chuỗi số thứ nhất
        private final int    digit2;          // Chữ số lấy từ chuỗi số thứ hai
        private final int    carryIn;         // Số nhớ từ bước trước chuyển sang
        private final int    total;           // Tổng của (digit1 + digit2 + carryIn)
        private final int    digitWritten;    // Chữ số được ghi lại vào kết quả (total % 10)
        private final int    carryOut;        // Số nhớ chuyển sang bước tiếp theo (total / 10)
        private final String partialResult;   // Kết quả chuỗi tạm thời tính đến bước hiện tại

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
     * Đối tượng chứa kết quả tổng hợp bao gồm: chuỗi ban đầu, kết quả cuối cùng 
     * và danh sách toàn bộ các bước tính toán chi tiết.
     */
    public static class AdditionResult {
        private final String             stn1;    // Chuỗi số thứ nhất
        private final String             stn2;    // Chuỗi số thứ hai
        private final String             result;  // Chuỗi kết quả sau khi cộng hoàn tất
        private final List<AdditionStep> steps;   // Danh sách chi tiết từng bước tính toán (chỉ đọc)

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

    // ── Giao diện API công khai ──────────────────────────────────────────────────────────

    /**
     * Hàm chính thực hiện phép cộng 2 chuỗi số lớn.
     * (Đáp ứng chuẩn xác Task 1: Chỉ trả về kết quả là chuỗi String đơn thuần).
     * 
     * @param stn1 Toán hạng thứ nhất (dạng chuỗi)
     * @param stn2 Toán hạng thứ hai (dạng chuỗi)
     * @return Chuỗi kết quả của phép cộng
     */
    public String sum(String stn1, String stn2) {
        int len1 = stn1.length();
        int len2 = stn2.length();
        // Tìm độ dài lớn nhất giữa 2 chuỗi để xác định số vòng lặp tối đa
        int maxLen = Math.max(len1, len2);
        
        // Cấp phát mảng char duy nhất một lần. Kích thước maxLen + 1 để phòng trường hợp 
        // phép cộng cuối cùng sinh ra phần nhớ (ví dụ: 9 + 9 = 18 cần thêm 1 ô để chứa số 1).
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen; // Con trỏ ghi ký tự vào mảng (ghi lùi dần từ phải sang trái)
        int carry = 0;         // Biến lưu trữ số nhớ

        // Khai báo toàn bộ biến ra ngoài vòng lặp để giữ Clean Code và tối ưu hóa cấp phát RAM
        int i, j, digit1, digit2, total, digitWritten, carryOut;

        // Vòng lặp for duy nhất: Duyệt k từ 0 (chữ số cuối cùng bên phải) lên dần phía trước.
        // Điều kiện dừng: Khi đã duyệt qua hết chữ số của 2 chuỗi (k < maxLen) VÀ không còn số nhớ nào (carry == 0).
        for (int k = 0; (k < maxLen) || (carry > 0); k++) {
            // Tính toán chỉ số (index) lấy ký tự của 2 chuỗi (đi lùi từ cuối chuỗi)
            i = len1 - 1 - k;
            j = len2 - 1 - k;

            // Nếu chỉ số hợp lệ (i >= 0), ta lấy ký tự tại vị trí đó và ép kiểu về số nguyên (trừ đi mã ASCII của '0').
            // Nếu chỉ số bị âm (đã hết chuỗi), ta coi chữ số đó là 0.
            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            // Tổng của 2 chữ số cộng thêm số nhớ từ bước trước chuyển qua
            total        = digit1 + digit2 + carry;
            // Chữ số thực tế được ghi vào chuỗi kết quả (lấy hàng đơn vị bằng phép chia lấy dư % 10)
            digitWritten = total % 10;
            // Số nhớ chuyển sang bước sau (lấy phần chục bằng phép chia nguyên / 10)
            carryOut     = total / 10;

            // Ép phần dư thành ký tự char và lưu vào mảng kết quả
            resultChars[writePos] = (char) (digitWritten + '0');
            
            // Ghi log (nhật ký) chi tiết vào Console cho từng bước tính toán (Thỏa mãn yêu cầu của Task 1)
            log.info("Bước {}: {} + {} + nhớ({}) = {} => Ghi {}, nhớ sang bước sau={}",
                    k + 1, digit1, digit2, carry, total, digitWritten, carryOut);

            // Cập nhật lại số nhớ cho vòng lặp tiếp theo
            carry = carryOut;
            // Lùi con trỏ mảng đi 1 vị trí (chuyển sang tính hàng chục, hàng trăm...)
            writePos--;
        }

        // Tạo ra chuỗi String từ mảng char. Bỏ qua các vị trí trống (chứa ký tự null) ở đầu mảng.
        return new String(resultChars, writePos + 1, maxLen - writePos);
    }

    /**
     * Hàm phụ trợ dành riêng cho giao diện Web Frontend (Task 2) để vẽ Animation sinh động.
     * 
     * Kế thừa hoàn toàn thuật toán Clean Code tối ưu của hàm sum(), nhưng bổ sung thêm thao tác 
     * đóng gói dữ liệu của từng bước tính toán (AdditionStep) vào một danh sách List.
     * 
     * @param stn1 Toán hạng thứ nhất
     * @param stn2 Toán hạng thứ hai
     * @return Đối tượng AdditionResult chứa mảng kết quả và chi tiết lịch sử cộng
     */
    public AdditionResult sumWithSteps(String stn1, String stn2) {
        log.info("Bắt đầu hàm sumWithSteps(): stn1='{}', stn2='{}'", stn1, stn2);

        int len1 = stn1.length();
        int len2 = stn2.length();
        int maxLen = Math.max(len1, len2);
        
        char[] resultChars = new char[maxLen + 1];
        int writePos = maxLen;
        int carry = 0;

        // Khởi tạo mảng lưu trữ lịch sử tính toán
        List<AdditionStep> steps = new ArrayList<>();

        // Khai báo biến bên ngoài vòng lặp
        int i, j, digit1, digit2, total, digitWritten, carryOut;
        String partial; // Biến lưu chuỗi kết quả tạm thời ở mỗi bước

        for (int k = 0; (k < maxLen) || (carry > 0); k++) {
            i = len1 - 1 - k;
            j = len2 - 1 - k;

            digit1 = (i >= 0) ? (stn1.charAt(i) - '0') : 0;
            digit2 = (j >= 0) ? (stn2.charAt(j) - '0') : 0;

            total        = digit1 + digit2 + carry;
            digitWritten = total % 10;
            carryOut     = total / 10;

            resultChars[writePos] = (char) (digitWritten + '0');
            
            // Khởi tạo chuỗi kết quả phụ (partial) tính từ vị trí con trỏ writePos đến cuối mảng
            partial = new String(resultChars, writePos, maxLen + 1 - writePos);

            // Đóng gói tất cả thông tin của vòng lặp hiện tại vào đối tượng AdditionStep và đưa vào List
            steps.add(new AdditionStep(
                    k + 1, digit1, digit2, carry,
                    total, digitWritten, carryOut, partial));

            carry = carryOut;
            writePos--;
        }

        String finalResult = new String(resultChars, writePos + 1, maxLen - writePos);
        log.info("Hoàn tất hàm sumWithSteps(): '{}' + '{}' = '{}'", stn1, stn2, finalResult);

        return new AdditionResult(stn1, stn2, finalResult, steps);
    }
}
