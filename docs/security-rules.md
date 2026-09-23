# Security Rules

1. Never hardcode passwords, API keys, JWT secrets, connection strings, or tokens in source code or comments.
2. Load secrets from environment variables or a managed secret store.
3. Validate and sanitize all untrusted input at the application boundary.
4. Use parameterized queries or ORM methods; never concatenate untrusted values into SQL or JPQL.
5. Enforce authentication and authorization on protected endpoints.
6. Apply least privilege to users, service accounts, database roles, and external integrations.
7. Do not expose secrets, personal data, SQL statements, stack traces, or internal paths in logs or API responses.
8. Use secure defaults for transport, cookies, headers, and error handling.
9. Add tests for unauthorized access, invalid input, injection payloads, and sensitive-data exposure.
10. Review dependencies and configuration for known security risks before release.

## Correct and Incorrect Examples

Correct:

```java
repository.findByEquipmentId(equipmentId);
```

Incorrect:

```java
entityManager.createNativeQuery("SELECT * FROM equipment WHERE id = '" + equipmentId + "'");
```

Correct:

```java
private final String jwtSecret = environment.getRequiredProperty("APP_JWT_SECRET");
```

Incorrect:

```java
private static final String JWT_SECRET = "hardcoded-secret";
```
