# Bài học kinh nghiệm: Kiểm soát rác sinh ra bởi AI và Templates

**Ngày lập**: 2026-09-24
**Người lập**: AI Assistant / Team Lead

## 1. Sự cố / Hiện tượng quan sát được
Trong quá trình khởi tạo Test Project (.NET xUnit) ở Lab 4.1 và sinh code bằng AI, có 2 điểm bất cập:
- IDE/AI tự động tạo file `UnitTest1.cs` chứa những hàm test vô nghĩa, tự khẳng định (tautological tests) kiểu `Assert.True(true)`.
- Các thư mục chứa build cache của C# là `obj/` và `bin/` vô tình bị AI `git add` và commit thẳng lên kho lưu trữ vì thiếu cấu hình `.gitignore` triệt để ngay từ đầu.

## 2. Phân tích Nguyên nhân (RCA)
- **Thiếu System Prompts / Rules rõ ràng**: Trợ lý AI không được dặn trước về việc xóa các file template rác mặc định của framework.
- **Git Hook & Ignore**: Repository chưa có rule `.gitignore` chuyên dụng cho C# (chỉ là repo Markdown/Java ban đầu), dẫn tới việc AI thực thi lệnh `git add .` ôm luôn toàn bộ cache của quá trình `dotnet build / dotnet test`.

## 3. Hành động khắc phục (Compounding)
- Đã xóa sạch `obj/`, `bin/` khỏi Git tracking và cập nhật `.gitignore`.
- Cập nhật **Harness Files (`docs/coding-rules.md`)** bổ sung thêm quy tắc nghiêm ngặt dành cho AI để ngăn chặn tái diễn.
