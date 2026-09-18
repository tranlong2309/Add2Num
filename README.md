# Add2Num — Add Two Large Numbers

> Internship assignment: implement the elementary-school digit-by-digit addition algorithm for arbitrarily large integers.

---

## Repository Structure

```
Add2Num/
├── pom.xml                   # Parent POM (Maven multi-module)
├── core/                     # Task 1 — Java library (add2num-core-0.0.1.jar)
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/add2num/core/
│       │   └── MyBigNumber.java        # class MyBigNumber { sum(stn1, stn2) }
│       └── test/java/com/add2num/core/
│           └── MyBigNumberTest.java    # JUnit 5 unit tests
├── web/                      # Task 2 — Spring Boot web app
│   ├── pom.xml               #   depends on add2num-core as .jar library
│   └── src/main/
│       ├── java/com/add2num/web/
│       │   ├── Add2NumApplication.java
│       │   └── controller/AddNumberController.java
│       └── resources/templates/index.html
└── README.md
```

## Branch Strategy

| Branch | Content |
|--------|---------|
| `core` | Task 1 — Java core library (`core/` module) |
| `main` | Task 2 — Full multi-module project (core + web) |

Tags: `v0.0.1-core` (Task 1), `v0.0.1` (Task 2)

---

## How Task 1 is reused in Task 2

Task 2 (`add2num-web`) declares `add2num-core` as a Maven dependency in `web/pom.xml`:

```xml
<dependency>
    <groupId>com.add2num</groupId>
    <artifactId>add2num-core</artifactId>
    <version>0.0.1</version>
</dependency>
```

`AddNumberController` calls `myBigNumber.sum(a, b)` directly from the library — **no code duplication**.

---

## Task 1 — Core Library (`core` branch)

### Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+     |
| Maven | 3.8+  |

### Build & Install jar

```bash
cd core
mvn clean install
# Installs add2num-core-0.0.1.jar into local Maven repository
```

### Run Unit Tests

```bash
cd core
mvn test
```

Expected output:

```
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
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
com.add2num.core.MyBigNumber
  └── AdditionResult sum(String stn1, String stn2)
```

Each step is logged via **SLF4J**.

---

## Task 2 — Spring Boot Web App (`main` branch)

### Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+     |
| Maven | 3.8+  |

### Build & Run (from project root)

```bash
# Step 1: install core library to local Maven repo
mvn clean install -pl core

# Step 2: run the web app (uses core.jar from local repo)
mvn spring-boot:run -pl web
```

Then open: **http://localhost:8888**

### Features

- Enter two arbitrarily large numbers (digits only)
- **Step-by-step animated cards** showing each digit addition
- **Progress bar** tracking completion
- **History panel** for the last 10 calculations
- Server-side validation; logs via SLF4J/Logback → `web/logs/add2num-web.log`

### Technology Stack

| Layer | Technology |
|-------|-----------|
| Core library | Java 17 + SLF4J (`add2num-core-0.0.1.jar`) |
| Framework | Spring Boot 3.3.4 |
| Templating | Thymeleaf |
| UI | Bootstrap 5 + Bootstrap Icons |
| Build | Maven 3 (multi-module) |

---

## Algorithm

Elementary-school right-to-left addition:

1. Traverse both strings from the **right** (least significant digit).
2. At each position: `total = digit1 + digit2 + carry_in`
3. `digit_written = total % 10`, `carry_out = total / 10`
4. Repeat until all digits and the remaining carry are consumed.
5. Reverse the collected digits → final result.

Handles numbers of **arbitrary size** — no integer overflow.

---

## Clone Instructions

```bash
# Clone (replace <YOUR_ACCOUNT> with your GitHub username)
git clone https://github.com/<YOUR_ACCOUNT>/Add2Num \
    ~/Projects/github.com/<YOUR_ACCOUNT>/Add2Num

cd ~/Projects/github.com/<YOUR_ACCOUNT>/Add2Num

# --- Task 1 (core branch) ---
git checkout core
cd core
mvn clean install   # builds + tests + installs .jar
cd ..

# --- Task 2 (main branch) ---
git checkout main
mvn clean install -pl core          # install core jar first
mvn spring-boot:run -pl web         # start web app
```

---

## Author

Developed as part of an internship evaluation exercise.
