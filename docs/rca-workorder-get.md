# Root Cause Analysis (RCA) - Lỗi 500 khi GET Work Order chi tiết

**Ngày**: 2026-09-24
**Sự cố**: Hệ thống trả về lỗi HTTP 500 Internal Server Error thay vì HTTP 404 khi truy vấn một `WorkOrderId` không tồn tại hoặc ID thiết bị bị null trong dữ liệu.

## 1. Triệu chứng (Symptom)
Khi gọi API `GET /api/work-orders/{id}` với một ID giả, người dùng nhận được phản hồi lỗi 500 thay vì mã lỗi 404 chuẩn RESTful. Trong log hệ thống ghi nhận lỗi `NullReferenceException`.

## 2. Nguyên nhân trực tiếp (Direct Cause)
Lớp Controller/Application đã cố gắng truy cập thuộc tính của Entity trả về từ Repository mà không kiểm tra xem Entity đó có bị `null` (hoặc `Optional.isPresent()`) hay không.

## 3. Nguyên nhân gốc rễ (Contributing Cause)
- **Thiếu kiểm định (Testing)**: Không có Unit Test nào mô phỏng trường hợp Repository trả về null.
- **Quy tắc API (API Rules)**: Đặc tả và prompt cho AI không nhấn mạnh việc phải xử lý ngoại lệ (Exception Handling) hoặc trả về 404 một cách tường minh cho tài nguyên không tìm thấy.

## 4. Biện pháp khắc phục & Phòng ngừa (Preventive Actions)
- **Khắc phục ngay lập tức**: Bổ sung kiểm tra `if (workOrder == null) return NotFound();` trong Controller.
- **Phòng ngừa (Compounding)**: 
  - Đã cập nhật `docs/api-rules.md` yêu cầu "Luôn luôn bắt null khi truy vấn theo ID và trả về 404".
  - Thêm một test case xUnit `GetWorkOrder_WithInvalidId_ReturnsNotFound`.
