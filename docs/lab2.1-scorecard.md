# LAB2.1 Scratch Draft Scorecard

## Scope

This scorecard evaluates `ScratchHandler.java` against the rules pack created for the Add2Num project. The handler is a learning artefact and must not be copied into production without a project-specific API decision.

|   # | Criterion                                        | Result  | Evidence                                                                                    |
| --: | ------------------------------------------------ | ------- | ------------------------------------------------------------------------------------------- |
|   1 | Uses a plural REST resource name                 | Pass    | The endpoint is `/api/additions`.                                                           |
|   2 | Uses the correct HTTP verb for creation          | Pass    | The handler uses `POST`.                                                                    |
|   3 | Uses constructor dependency injection            | Pass    | `MyBigNumber` is supplied through the constructor.                                          |
|   4 | Validates the request body                       | Pass    | The request uses `@Valid`, `@NotBlank`, and `@Pattern`.                                     |
|   5 | Restricts input to the specified digit format    | Pass    | Both operands accept digits only.                                                           |
|   6 | Reuses the existing core library                 | Pass    | The handler delegates addition to `MyBigNumber`.                                            |
|   7 | Avoids invented request fields                   | Pass    | The request contains only `number1` and `number2`.                                          |
|   8 | Returns a typed response                         | Pass    | The endpoint returns `AdditionResponse`.                                                    |
|   9 | Avoids generic exception handling                | Pass    | No broad catch block or generic exception is introduced.                                    |
|  10 | Avoids sensitive-data logging                    | Pass    | The draft does not log request values.                                                      |
|  11 | Keeps production code separate from scratch code | Pass    | The file is under `docs/`, outside the production source set.                               |
|  12 | Defines an explicit error contract               | Partial | Validation is present, but the application still needs a global RFC 7807 exception handler. |

## Findings

The draft passes the core coding, API, and security checks. The only partial result is the error contract because Spring's application-level Problem Details configuration is not part of this scratch file. A production endpoint must add and test a global exception handler before this draft can be considered complete.

## Manual Review Action

Open `ScratchHandler.java` in VS Code and compare each row with the applicable rule file. Mark a row as `Fail` if the code is changed and no longer satisfies its evidence statement.
