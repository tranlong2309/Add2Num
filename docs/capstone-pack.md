# Capstone Presentation Pack - Đồ án Cuối khóa
**Dự án**: Add2Num - Work Order Management System
**Nhóm**: Tran Long
**Ngày**: 2026-09-24

## 1. Thành quả dự án (Project Overview)
Xây dựng thành công tính năng "Quản lý Phiếu công việc" (Work Order) áp dụng toàn bộ quy trình AI-Native SDLC:
- **Repository**: [Add2Num GitHub Repo](https://github.com/tranlong2309/Add2Num)
- **Mã nguồn**: Tuân thủ tuyệt đối Clean Architecture, CQRS, và C# .NET 9.

## 2. Hệ thống tài liệu (Documentation)
1. [Domain Model (Lab 2.3)](./domain-model.md)
2. [API Specification (Lab 3.1)](./api-spec.yaml)
3. [Bảng KPI Đo lường (Lab 5.1)](./kpi.md)

## 3. Quá trình kiểm soát và đánh giá
- **Bảo mật OWASP**: Đã rà soát và fix lỗi A01, A04 (Missing auth, thiếu chặn length max).
- **Test Harness & CI/CD**: Các Unit Test (xUnit) chạy hoàn toàn tự động trên GitHub Actions (Green CI). Tích hợp Rule chặn Merge nếu test thất bại.

## 4. Quá trình Lũy tích Kiến thức (Compounding & RCA)
Xử lý các bài học thực tế, trực tiếp cải thiện hệ thống Rule của nhóm:
- [Bài học kiểm soát Ảo giác AI (Lab 5.1)](./lessons/2026-09-24-ai-tautological-tests-and-cache.md) - Cập nhật `coding-rules.md`.
- [Phân tích nguyên nhân gốc rễ RCA (Lab 5.2)](./rca-workorder-get.md) - Cập nhật `api-rules.md` về xử lý HTTP 404 thay vì 500.

**=> Đánh giá chung:** Luồng công việc End-to-End đã được chứng minh hiệu quả thực tế và sẵn sàng cho buổi demo Capstone!
