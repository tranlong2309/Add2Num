# LAB2 Execution Log

## Role

You are a Senior Software Engineer, Mentor, and Technical Writer. You work in a highly structured manner, follow procedures, and have strong pedagogical skills.

## Task

Read and understand the content of the two practical exercises, `lab2.1` and `lab2.2`. Then work with me to apply their requirements to the `Add2Num` project in the correct order.

The first step is to create this document to preserve the execution context. After that, guide me through the work one step at a time.

## Context Files

- The detailed content of `lab2.1` and `lab2.2`.
- The complete source code and directory structure of the `Add2Num` project.

## Constraints

### Language Policy

All code comments, commit messages, and content in documentation files created during this work MUST be written in English. Guidance and explanations to the user may be written in Vietnamese.

### Documenting Prompt

As the FIRST STEP, create a file named `LAB2_EXECUTION_LOG.md`. This file must contain the complete structure of the prompt translated into English and outline the steps that will be performed so another user can review the process later.

### Required Order

Analyze and execute the lab requirements in the exact order stated in the exercises. Do not skip, combine, or reorder steps.

### Two-Way Interaction

If any part of the lab is unclear or the current project does not match the lab requirements, stop and ask the user immediately. Do not make assumptions.

### Detailed Manual Guidance

Clearly distinguish between code generated automatically by AI and actions the user must perform manually. For manual actions, identify the file to open, the relevant line or section, the required UI operation, or the exact terminal command to type.

### Commit Milestones

At the end of each lab milestone, provide terminal commands for the user to save the work:

```bash
git add .
git commit -m "English commit message"
```

## Definition of Done

- `LAB2_EXECUTION_LOG.md` has been created successfully and its content is in English.
- All requirements from `lab2.1` and `lab2.2` have been completed in the `Add2Num` project, and the project runs without errors.
- The project contains clear milestone commits, and all code comments are professional and written entirely in English.

## Execution Roadmap

1. Create and maintain this execution log as the single trace of the lab work.
2. Extract and read the complete requirements of `lab2.1`.
3. Inspect the current `Add2Num` project against the first lab requirement.
4. Stop and ask the user about any ambiguity or mismatch before making a change.
5. Execute each `lab2.1` step in its stated order.
6. Validate the result after each focused milestone.
7. Record the milestone and provide the exact manual commit commands.
8. Extract and read the complete requirements of `lab2.2`.
9. Inspect the project and the completed `lab2.1` result against the first `lab2.2` requirement.
10. Stop and ask the user about any ambiguity or mismatch before making a change.
11. Execute each `lab2.2` step in its stated order.
12. Validate the complete project, including build, tests, and runtime behavior required by the labs.
13. Record the final validation results and provide the final milestone commit commands.

## Execution State

- [x] Create `LAB2_EXECUTION_LOG.md`.
- [x] Read and summarize `lab2.1`.
- [x] Validate the project against `lab2.1`.
- [x] Implement `lab2.1` in order.
- [x] Read and summarize `lab2.2`.
- [x] Validate the project against `lab2.2`.
- [x] Implement `lab2.2` in order.
- [x] Run final build, test, and runtime validation.
- [ ] Record final results and milestone commit commands.

## Notes

This log was created before implementation begins. Requirements from the attached documents must be transcribed or summarized here only after their complete contents have been read.

## Initial Requirement Analysis

### Lab 2.1 Summary

Lab 2.1 requires three separate rule files: `docs/coding-rules.md`, `docs/api-rules.md`, and `docs/security-rules.md`. The coding rules must contain at least 8-12 rules with correct and incorrect examples. The API rules must define REST resource naming, RFC 7807 Problem Details, validation, and strict schema conformance. The security rules must cover secret management, input validation, injection protection, and authorization. The lab also requires a scratch handler, a constrained Copilot draft prompt, and a scorecard with at least 10 compliance criteria.

### Lab 2.2 Summary

Lab 2.2 requires global and workspace custom instructions, a business requirements analysis in `docs/br-analysis-wo.md`, and an AI-assisted security code review. The review must contain at least 8 comments ordered by specification delta, security, validation and testing, complexity, and coding style. The lab also requires a safe replacement structure for the reviewed code.

### Current Project Mismatch Requiring User Decision

The project currently contains `core/docs/coding-rules.md`, which combines coding, API, and security material in one file. The lab refers to a `docs/` directory and requires three separate files. The project also has a restored `web/` module, while the root Maven POM currently declares only the `core` module.

Implementation is paused until the user confirms whether the lab documentation should be created at the project root (`docs/`) or under the existing module directory (`core/docs/`). No lab implementation step has been performed yet.

## Milestone 1: Rules Pack Structure

The user continued with the proposed project-root `docs/` location. The following files were created in the first lab step:

- `docs/coding-rules.md`
- `docs/api-rules.md`
- `docs/security-rules.md`

All three files are written in English, contain actionable rules, and include correct and incorrect examples. Validation completed successfully with no editor errors and a clean `git diff --check` result.

Manual checkpoint command:

```bash
git add .
git commit -m "docs: add foundational rules pack"
```

## Milestone 2: Lab 2.1 Draft and Scorecard

The Add2Num project does not contain a Work Order domain, so the lab's second example was adapted to the existing number-addition domain without inventing unrelated production features. `docs/ScratchHandler.java` is a non-production Spring MVC draft that validates two digit-only operands and delegates calculation to `MyBigNumber`. `docs/lab2.1-scorecard.md` evaluates the draft against 12 criteria. Eleven criteria pass and the RFC 7807 error contract is marked partial because a global exception handler has not been added to the web application.

Validation completed successfully:

- Core tests: 8 tests, 0 failures, 0 errors.
- Web package: `BUILD SUCCESS`.
- Documentation and scratch files: clean `git diff --check`.

Manual checkpoint command:

```bash
git add .
git commit -m "docs: add LAB2.1 draft and scorecard"
```

## Lab 2.2 Execution Notes

The workspace instructions will be stored in `.github/copilot-instructions.md`. Global Copilot instructions cannot be reliably changed through repository files; the user must add the equivalent English rules through the VS Code settings UI. The lab's Work Order business example and vulnerable controller are analysis inputs only because the current Add2Num project has no Work Order persistence or security domain.

## Milestone 3: Lab 2.2 Context and Business Analysis

The following artifacts were created in English:

- `.github/copilot-instructions.md` for workspace-level Copilot instructions.
- `docs/lab2-prompts.md` with the English prompt trace used for the draft, scorecard, instructions, business analysis, and security review.
- `docs/br-analysis-wo.md` with entities, open questions, risks, and UI/Data/API decomposition.
- `docs/work-order-code-review.md` with 14 prioritized review comments and a safe replacement structure.

The root Maven POM was also updated to include the restored `web` module. Full reactor validation passed: parent, core, and web all reported `SUCCESS`; the core suite reported 8 tests with 0 failures and 0 errors.

Manual global-instructions action:

1. Open VS Code Settings with `Cmd+,`.
2. Search for `Chat: Instructions` or open the Copilot Chat settings section.
3. Add the same English rules from `.github/copilot-instructions.md` to the global instructions field.
4. Save the setting and open a new Copilot Chat conversation to verify that the instructions are active.

Manual checkpoint command:

```bash
git add .
git commit -m "docs: add LAB2.2 instructions and analysis"
```

## Final Validation State

- [x] Create `LAB2_EXECUTION_LOG.md`.
- [x] Read and summarize both lab documents.
- [x] Create and validate the LAB2.1 rules pack.
- [x] Create and score the LAB2.1 scratch draft.
- [x] Create workspace instructions for LAB2.2.
- [x] Create the LAB2.2 business analysis.
- [x] Create the LAB2.2 security code review.
- [x] Record the structured prompts used for the lab artefacts.
- [x] Include `web` in the root Maven reactor.
- [x] Run the full Maven reactor test.
- [x] Start the web application and verify the home page with HTTP 200 on port 8889.
- [ ] Perform the manual global Copilot instructions setup.
- [ ] Commit the milestones manually.

Runtime note: the configured port 8888 was already occupied, so the smoke test used port 8889 with `-Dspring-boot.run.arguments=--server.port=8889`. The application started successfully and returned the expected home page. The test process was stopped after verification.
