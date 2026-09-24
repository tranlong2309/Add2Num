# API Design Rules

1. Use plural nouns for REST resources, such as `/api/work-orders`.
2. Use `POST` for creation, `GET` for retrieval, `PUT` or `PATCH` for updates, and `DELETE` for removal.
3. Do not invent JSON fields that are not defined by the applicable requirement or API specification.
4. Return RFC 7807 Problem Details for errors with `type`, `title`, `status`, and `detail` fields.
5. Validate request bodies with `@Valid` and appropriate constraints such as `@NotBlank` and `@NotNull`.
6. Return an HTTP status that accurately represents the result of the operation.
7. Keep request and response schemas explicit and separate from persistence entities.
8. Do not return stack traces, SQL statements, or internal exception messages to clients.

## Correct and Incorrect Examples

Correct:

```java
@PostMapping
public ResponseEntity<WorkOrderResponse> create(
        @Valid @RequestBody CreateWorkOrderRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
}
```

Incorrect:

```java
@PostMapping("/create")
public ResponseEntity<String> create(@RequestBody Map<String, Object> payload) {
    return ResponseEntity.ok("created");
}
```

Correct error response:

```json
{
  "type": "https://example.com/problems/validation-error",
  "title": "Validation failed",
  "status": 400,
  "detail": "The request contains invalid fields."
}
```

Incorrect error response:

```text
java.sql.SQLException: INSERT statement failed at line 42
```

11. Always check for null entities when querying by ID and return standard HTTP 404 Not Found instead of causing internal server errors.
