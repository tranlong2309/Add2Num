# Add2Num — Add Two Large Numbers

> Internship assignment: implement the elementary-school digit-by-digit addition algorithm for arbitrarily large integers represented as strings.

---

## Repository Structure

```
AddTwoNumber/
├── AddTwoNumber.Core/        # Task 1 – C# .NET 9 Class Library
├── AddTwoNumber.Tests/       # Task 1 – xUnit unit tests
├── AddTwoNumber.sln          # .NET solution file
├── add2num-web/              # Task 2 – Spring Boot web application
│   ├── src/
│   └── pom.xml
└── README.md
```

## Branch Strategy

| Branch | Content |
|--------|---------|
| `core` | C# .NET 9 core library + unit tests (Task 1) |
| `main` | Spring Boot web application (Task 2) |

Tags: `v0.0.1-core` (Task 1), `v0.0.1` (Task 2)

---

## Task 1 — C# Core Library (`core` branch)

### Prerequisites

| Tool | Version |
|------|---------|
| .NET SDK | 9.0+ |

### Build

```bash
dotnet build AddTwoNumber.sln
```

### Run Unit Tests

```bash
dotnet test --logger "console;verbosity=detailed"
```

Expected output:

```
Total tests: 8
     Passed: 8
 Total time: ~0.5 Seconds
```

### Test Cases

| # | Input | Expected |
|---|-------|----------|
| 1 | `"1234"`, `"897"` | `"2131"` |
| 2 | `"999"`, `"1"` | `"1000"` |
| 3 | `"1"`, `"999999"` | `"1000000"` |
| 4 | `"5"`, `"5"` | `"10"` |
| 5 | `"99999999999999999999"`, `"1"` | `"100000000000000000000"` |
| 6 | `"0"`, `"123"` | `"123"` |
| 7 | `"0"`, `"0"` | `"0"` |
| 8 | `"500000000000000000000"`, `"500000000000000000000"` | `"1000000000000000000000"` |

### Key Class

```
AddTwoNumber.Core.MyBigNumber
  └── string Sum(string stn1, string stn2)
```

Each operation is logged step-by-step via **Serilog** (console + rolling file at `AddTwoNumber.Tests/logs/`).

---

## Task 2 — Spring Boot Web App (`main` branch)

### Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.8+ |

### Build

```bash
cd add2num-web
mvn clean package -DskipTests
```

### Run

```bash
cd add2num-web
mvn spring-boot:run
```

Then open your browser at: **http://localhost:8080**

### Features

- Enter two arbitrarily large numbers (digits only)
- See the **step-by-step calculation** animated live in the browser
- **Progress bar** tracks how many steps have completed
- **History panel** shows the last 10 calculations
- Server-side validation (non-digits rejected)
- Calculation history logged via **SLF4J/Logback** to `add2num-web/logs/add2num-web.log`

### Technology Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.3.4 |
| Templating | Thymeleaf |
| UI | Bootstrap 5 + Bootstrap Icons |
| Fonts | Google Fonts (Inter, JetBrains Mono) |
| Logging | SLF4J + Logback |
| Build | Maven 3 |

---

## Algorithm

The addition follows the elementary-school method:

1. Align both strings from the **right** (least significant digit).
2. At each position: `sum = digit1 + digit2 + carry_in`
3. `digit_written = sum % 10`, `carry_out = sum / 10`
4. Repeat until all digits and the final carry are consumed.
5. Reverse the collected digits to form the result.

This approach handles numbers of **arbitrary size** — no integer overflow possible.

---

## Clone Instructions

```bash
# Clone repository (replace <YOUR_ACCOUNT> with your GitHub username)
git clone https://github.com/<YOUR_ACCOUNT>/AddTwoNumber \
    ~/Projects/github.com/<YOUR_ACCOUNT>/AddTwoNumber

cd ~/Projects/github.com/<YOUR_ACCOUNT>/AddTwoNumber

# --- Task 1 (core branch) ---
git checkout core
dotnet test

# --- Task 2 (main branch) ---
git checkout main
cd add2num-web
mvn spring-boot:run
```

---

## Author

Developed as part of an internship evaluation exercise.
