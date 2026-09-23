# AI-Assisted Code Review: WorkOrderController

## Scope and Review Order

The following comments review the vulnerable controller supplied in the LAB2.2 exercise. They are ordered by specification delta, security, validation and testing, complexity, and coding style. The Work Order controller is an analysis sample and is not part of the current Add2Num production domain.

## Review Comments

### Specification Delta

1. **[Spec delta] The endpoint contract is not established by the raw requirement.** The implementation invents `/create-wo`, `ApiResponse`, `ErrorResponse`, and a `status` value of `NEW` without an approved API specification. Confirm the resource path, request fields, response fields, and status lifecycle before implementation. Prefer `POST /api/work-orders` only after the contract is approved.

2. **[Spec delta] The code assumes that authentication and authorization can be skipped.** The business statement says complex authorization may not be required, not that authentication and access checks may be removed. Require an authenticated user and confirm whether technician and supervisor permissions differ.

### Security

3. **[Security] `JWT_SECRET` is hardcoded in source code.** This exposes a credential through source control and packaged artifacts. Remove it, rotate the exposed value, and load secrets from an environment variable or managed secret store.

4. **[Security] The SQL statement is vulnerable to SQL injection.** `equipmentId`, `description`, and `priority` are concatenated into a SQL string and executed with `Statement`. Use Spring Data repository methods or a `PreparedStatement` with bound parameters.

5. **[Security] The controller trusts an unverified equipment identifier and priority.** Verify that the equipment exists and is active, constrain priority to an enum, and authorize the operation for the authenticated technician before persistence.

6. **[Security] The error response exposes the SQL query and internal exception message.** These values can disclose schema details, input data, and implementation paths. Log a correlation identifier server-side without sensitive values and return a generic RFC 7807 Problem Details response.

### Validation and Testing

7. **[Validation and testing] `@RequestBody` is not combined with `@Valid` and field constraints.** Add a typed request DTO with constraints for required values, length limits, allowed priority values, and the approved equipment identifier format. Add tests for blank, oversized, malformed, and injection-like input.

8. **[Validation and testing] There are no tests for authentication, authorization, or business validation.** Add tests proving unauthenticated requests are rejected, unauthorized roles are rejected, inactive equipment is rejected, valid requests are persisted, and duplicate or retry behavior follows the approved requirements.

### Complexity

9. **[Complexity] The controller owns connection management, SQL construction, persistence, validation, and response mapping.** Split responsibilities into a controller, application service, repository, and exception handler. This makes each security boundary independently testable.

10. **[Complexity] `ResponseEntity<?>` and loosely defined response classes make the contract difficult to reason about.** Use explicit request and response DTOs and a typed `ResponseEntity<WorkOrderResponse>` for successful creation.

### Coding Style and Best Practices

11. **[Style] Field injection makes dependencies mutable and hides the controller's required collaborators.** Use constructor injection with `private final` dependencies.

12. **[Style] `System.out.println` bypasses structured application logging.** Use an SLF4J logger, but never include raw descriptions, credentials, tokens, or other sensitive values in log messages.

13. **[Style] The catch-all `Exception` handler hides failure categories and can misclassify errors.** Catch or map specific domain and persistence exceptions in a centralized exception handler, while preserving a generic client-facing error message.

14. **[Style] The sample has no transaction boundary or explicit repository abstraction.** Let a service define the transaction boundary and use a repository with parameterized persistence operations.

## Suggested Safe Structure

```text
workorder/
├── controller/
│   └── WorkOrderController.java
├── dto/
│   ├── CreateWorkOrderRequest.java
│   └── WorkOrderResponse.java
├── service/
│   └── WorkOrderService.java
├── repository/
│   └── WorkOrderRepository.java
├── domain/
│   ├── WorkOrder.java
│   └── WorkOrderPriority.java
└── error/
    └── GlobalExceptionHandler.java
```

### Safe Flow

1. `WorkOrderController` accepts a typed `@Valid` request and obtains the authenticated principal.
2. `WorkOrderService` checks the caller's role and verifies that the equipment exists and is active.
3. `WorkOrderRepository` persists through parameterized repository methods or ORM operations.
4. `GlobalExceptionHandler` maps validation, authorization, not-found, conflict, and unexpected errors to RFC 7807 Problem Details.
5. Tests cover the approved contract, security boundaries, validation, persistence behavior, and non-disclosure of internal details.

## Review Conclusion

The sample must not be deployed. It contains specification gaps, a hardcoded secret, SQL injection, missing authentication and authorization enforcement, missing validation, sensitive error disclosure, weak exception handling, and excessive controller responsibility. The safe structure above addresses those risks while preserving a clear API boundary.
