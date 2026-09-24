# LAB2 Prompt Trace

## LAB2.1 Scratch Draft Prompt

```text
Role: Senior Engineer.
Task: Draft a POST /api/additions handler for the Add2Num project that delegates to MyBigNumber.sum(String, String).
Context files: docs/coding-rules.md, docs/api-rules.md, docs/security-rules.md.
Constraints: validate both operands as digit-only strings, do not invent request fields, use constructor injection, use a typed response, avoid sensitive logging, and keep the draft outside the production source set.
```

## LAB2.1 Scorecard Prompt

```text
Review docs/ScratchHandler.java against the coding, API, and security rules. Produce at least ten criteria with Pass, Fail, or Partial status and cite concrete evidence for every result. Call out any missing RFC 7807 error handling.
```

## LAB2.2 Custom Instructions

```text
Role: Senior Solution Architect and Technical Lead for the Add2Num project.
Core rules: never commit secrets; follow docs/*; validate untrusted input; use parameterized persistence; enforce authentication and authorization; do not invent API fields; keep comments and commit messages in English; stop and ask when requirements are ambiguous.
```

## LAB2.2 Business Requirements Analysis Prompt

```text
Act as a professional Business Analyst. Analyze the supplied POSCO MCI Work Order requirement and produce docs/br-analysis-wo.md with entities and attributes, open questions and business risks, and a three-layer UI/Data/API decomposition. Do not finalize decisions that the Product Owner has not specified.
```

## LAB2.2 Security Review Prompt

```text
Act as a Senior Security Code Reviewer. Review the supplied WorkOrderController in this order: specification delta, security, validation and testing, complexity, and coding style. Produce at least eight specific comments and propose a safe controller/service/repository/DTO/error-handler structure. Do not deploy or copy the vulnerable sample.
```

## Execution Note

The prompts above were used as the structured generation and review context for the lab artefacts. The Work Order examples remain documentation exercises because the current Add2Num application has no Work Order domain, database, authentication, or mobile UI.
