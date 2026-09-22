# Java Coding & Logging Rules (WorkOrder Module)


1. **Language & Framework:** Use Java 17+ and Spring Boot 3.3+.

2. **Naming Conventions:** Use `PascalCase` for classes, `camelCase` for methods and variables, `UPPER_SNAKE_CASE` for constants.

3. **Exception Handling:** Never throw generic `RuntimeException` or `Exception`. Use specific custom exceptions or standard ones like `IllegalArgumentException`.

4. **Logging Standards:**

- DO NOT log sensitive data or PII (Personally Identifiable Information like raw passwords, emails, phone numbers).

- Use SLF4J logger instantiated as `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`.

5. **Dependency Injection:** Always use constructor injection. Avoid field injection (`@Autowired` on fields).

6. **Code Simplicity:** Avoid over-engineering, design patterns factories unless requested. Write clean, flat vertical slices.

7. **Variable Declaration Scope:**

- **DO NOT** declare variables inside loops (`for`, `while`, `do-while`). All loop-related or temporary variables must be declared outside the loop block to avoid unnecessary memory reallocation overhead, improve readability, and maintain clean scope management.

#### Example:

- **[GOOD]:**

```java

// Variable declared outside the loop

String item = "";

for (int i = 0; i < list.size(); i++) {

item = list.get(i);

process(item);

}

#### Example:

- **[GOOD (Dependency Injection)]:**

```java

public class WorkOrderService {

private final WorkOrderRepository repository;

public WorkOrderService(WorkOrderRepository repository) {

this.repository = repository;

}

}

# API Design Rules
1. **REST Resource Naming:** Use plural nouns for resources (e.g., `/api/workorders`, not `/api/workOrder`).

2. **HTTP Verbs:** Use standard verbs: `POST` for creation, `GET` for retrieval, `PUT`/`PATCH` for updates, `DELETE` for removal.

3. **Strict Schema Conformance:** DO NOT invent extra JSON fields or properties that are not explicitly defined in the requirements or API spec.

4. **Error Responses (RFC 7807):** All error responses must return Problem Details with fields: `type`, `title`, `status`, and `detail`.

5. **Validation:** All incoming request bodies must be annotated with `@Valid` and appropriate constraints (`@NotNull`, `@NotBlank`).


#### Example:

- **[GOOD]:** Returning `ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails)`

- **[BAD]:** Returning raw strings or generic stack traces on validation failure.
# Security Rules


1. **No Hardcoded Secrets:** Never hardcode API keys, passwords, connection strings, or tokens in source code or comments. Use environment variables.

2. **Input Sanitization & Validation:** Always validate and sanitize inputs at the controller boundary. Never trust client payloads.

3. **SQL / Injection Protection:** Use Spring Data JPA / Hibernate parameterized queries or ORM methods.
Never concatenate strings to build native SQL or JPQL queries.

4. **Authorization Checks:** Ensure proper role-based access control (RBAC) is declared on endpoints (e.g., `@PreAuthorize("hasRole('TECHNICIAN')")`).