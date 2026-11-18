---
name: test-generation
description: Generate automated tests for changed Java classes.
---

# Test Generation Skill

## Objective

Analyze changes introduced into the main branch and
generate automated tests for the affected Java classes.

## Rules

1. Analyze the Git diff before generating tests.
2. Prioritize business logic and service classes.
3. Use JUnit 5 and Spring Boot Test.
4. Use Mockito only when dependencies need isolation.
5. Follow the existing test conventions.
6. Do not modify production code.
7. Do not modify database migrations.
8. Avoid tests that depend on execution order.
9. Execute ./mvnw test after generating tests.
10. Never remove existing tests.

## Expected output

- New test classes under src/test/java.
- Tests covering relevant positive and negative scenarios.
- A summary of the generated tests.
- A report of any remaining test failures.
