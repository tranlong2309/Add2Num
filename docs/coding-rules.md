# Java Coding and Logging Rules

This file defines the baseline coding rules for the Add2Num project.

## Rules

1. Use Java 17 or newer and Spring Boot 3.3 or newer where Spring Boot is required.
2. Use `PascalCase` for classes, `camelCase` for methods and variables, and `UPPER_SNAKE_CASE` for constants.
3. Use specific exceptions instead of generic `Exception` or `RuntimeException`.
4. Use constructor injection and keep dependencies in `private final` fields.
5. Use SLF4J for application logging and never log secrets or personally identifiable information.
6. Keep methods focused on one responsibility and avoid unnecessary abstractions.
7. Validate data at the boundary before passing it to domain logic.
8. Keep public methods and classes documented when their behavior is not self-evident.
9. Avoid declaring reusable temporary variables inside loops when the project performance rules require stable loop scope.
10. Write unit tests for normal cases, boundary cases, and invalid input.

## Correct and Incorrect Examples

### Dependency Injection

Correct:

```java
public WorkOrderService(WorkOrderRepository repository) {
    this.repository = repository;
}
```

Incorrect:

```java
@Autowired
private WorkOrderRepository repository;
```

### Logging

Correct:

```java
log.info("Number addition completed");
```

Incorrect:

```java
log.info("Addition completed for password={}", password);
```

### Exception Handling

Correct:

```java
throw new IllegalArgumentException("Input must contain digits only");
```

Incorrect:

```java
throw new RuntimeException("Something went wrong");
```
