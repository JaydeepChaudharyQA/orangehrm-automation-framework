# OrangeHRM Automation Framework

[![UI Tests](https://github.com/JaydeepChaudharyQA/orangehrm-automation-framework/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/JaydeepChaudharyQA/orangehrm-automation-framework/actions/workflows/ui-tests.yml)
![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.50-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-7.12-red)

An end-to-end UI test automation framework for the
[OrangeHRM demo application](https://opensource-demo.orangehrmlive.com/), built with
**Java, Selenium WebDriver, TestNG and the Page Object Model**.

It's built as a reusable, scalable framework rather than a set of standalone scripts. Adding a new test or a whole new module only means adding a page class and a test class; the framework code doesn't change.

**Author:** Jaydeep Chaudhary · [Portfolio](https://jaydeepchaudharyqa.github.io/portfolio/) · [LinkedIn](https://www.linkedin.com/in/jaydeepchaudharyqa)

---

## Highlights

| Area | What the framework does |
|---|---|
| **Design** | Page Object Model with an inheritance hierarchy of pages and reusable components |
| **OOP** | Encapsulation, inheritance, polymorphism and abstraction applied throughout ([details](#oop-concepts-in-this-framework)) |
| **Browsers** | Chrome, Firefox and Edge; headless or headed; drivers handled automatically by Selenium Manager |
| **Synchronisation** | Only explicit, condition-based waits: no `Thread.sleep`, no implicit waits |
| **Stability** | Automatic retry on stale elements, waits for table refreshes and loaders, and an optional test retry |
| **Test data** | JSON files mapped to Java objects, plus generated unique data so runs never collide |
| **Reporting** | ExtentReports HTML report with steps and failure screenshots, plus TestNG's own reports |
| **Logging** | Log4j2: INFO to the console, full DEBUG detail to `logs/automation.log`; passwords are always masked |
| **Execution** | Smoke and regression suites, parallel execution, and run-time overrides from the command line |
| **CI** | GitHub Actions runs the suite on every push and keeps the reports as downloadable artifacts |

## Test coverage

| Module | Scenarios |
|---|---|
| **Login** | Valid login · invalid credentials (data-driven: wrong password, unknown user, empty fields) · logout |
| **Dashboard** | Core widgets displayed · logged-in user shown |
| **Navigation** | Every main module opens from the side menu (data-driven) · menu search filters items |
| **PIM (end to end)** | Add employee → verify profile → search by ID → delete → verify removed · required-field validation |
| **Admin** | Search user by username · filter by role · no results for an unknown user |

**21 test executions** in the regression suite, of which **5 core flows** form the smoke suite.

---

## Tech stack

Java 17 · Selenium WebDriver 4 · TestNG 7 · Maven (via the Maven Wrapper) · ExtentReports 5 · Log4j2 · Jackson · GitHub Actions

## Getting started

**Prerequisites:** JDK 17 or newer and Google Chrome (or Firefox / Edge). You don't need to install Maven; the included wrapper (`mvnw`) downloads it automatically.

```bash
git clone https://github.com/JaydeepChaudharyQA/orangehrm-automation-framework.git
cd orangehrm-automation-framework

# Run the full regression suite (default)
./mvnw test                      # Windows: mvnw.cmd test

# Run only the smoke suite, headless
./mvnw test -Dsuite=smoke -Dheadless=true

# Run on another browser
./mvnw test -Dbrowser=firefox

# Run a single test class
./mvnw test -Dtest=LoginTests
```

> **Windows tip:** if `git clone` fails with *Filename too long*, run `git config --global core.longpaths true` once and clone again.

Any setting in `config.properties` can be overridden with `-Dkey=value` or an environment variable (for example `BROWSER=edge`).

## Reports

After a run, open:

| Report | Location |
|---|---|
| **Extent HTML report**: steps, timings, categories, embedded failure screenshots | `reports/extent-report.html` |
| **TestNG reports**: built-in summary and detailed results | `target/surefire-reports/index.html` and `emailable-report.html` |
| **Failure screenshots** (PNG) | `reports/screenshots/` |
| **Execution log** | `logs/automation.log` |

In GitHub Actions, the same files are attached to each run as a downloadable **test-reports** artifact.

---

## Project structure

```
src
├── main/java/io/github/jaydeepchaudharyqa/orangehrm      ← the reusable framework
│   ├── config/        ConfigReader: settings from file, -D flags or environment variables
│   ├── constants/     FrameworkConstants: report, screenshot and data paths
│   ├── driver/        BrowserDriver (abstract) + Chrome/Firefox/Edge, DriverFactory, DriverManager (ThreadLocal)
│   ├── enums/         BrowserType
│   ├── exceptions/    FrameworkException and its specific subclasses
│   ├── listeners/     TestListener (reports + screenshots), RetryAnalyzer, RetryTransformer
│   ├── models/        Employee (Builder), LoginData, ModuleData: typed test data
│   ├── pages/         Page Objects
│   │   ├── BasePage, AuthenticatedPage, BaseListPage   ← abstract parents
│   │   ├── LoginPage, DashboardPage
│   │   ├── pim/       EmployeeListPage, AddEmployeePage, PersonalDetailsPage
│   │   ├── admin/     SystemUsersPage
│   │   └── components/ TopBar, SideMenu: reusable UI parts
│   ├── reports/       ExtentManager, ExtentTestManager, StepLogger
│   └── utils/         WaitUtils, ElementActions, ScreenshotUtils, JsonDataReader, TestDataGenerator
│
└── test
    ├── java/.../orangehrm
    │   ├── base/          BaseTest: browser set-up/tear-down and shared helpers
    │   ├── dataproviders/ TestNG data providers backed by JSON files
    │   └── tests/         auth, dashboard, navigation, pim, admin test classes
    └── resources
        ├── config/config.properties
        ├── testdata/      invalid-logins.json, modules.json, employees.json
        ├── suites/        smoke.xml, regression.xml
        └── log4j2.xml
```

## What each component does

| Component | Purpose |
|---|---|
| **ConfigReader** | Singleton that reads `config.properties`. Each key can be overridden by a `-D` system property or an environment variable, so CI and local runs need no file edits. Typed getters (`browser()`, `explicitWait()`) hide the raw properties. |
| **BrowserDriver + subclasses** | One class per browser that knows its own options (headless, window size, pop-up blocking). The shared start-up steps (timeouts, maximise) live in the parent. |
| **DriverFactory** | Turns a `BrowserType` into a running WebDriver. Callers never deal with browser-specific classes. |
| **DriverManager** | Stores the WebDriver in a `ThreadLocal`, so each parallel test thread has its own browser. |
| **WaitUtils** | All synchronisation: visibility, clickability, URL changes, OrangeHRM loading spinners, async field values, and custom conditions that ignore stale elements. |
| **ElementActions** | Reusable click/type/read methods that wait first, retry on stale elements, fall back to a JavaScript click if something overlays the element, and log every action (secrets masked). |
| **BasePage** | Gives every page the driver, waits, actions and logger. Each page declares only *what* identifies it (URL fragment and a key element); `BasePage` implements *how* to wait for it. |
| **AuthenticatedPage** | Parent of all pages after login. Provides the top bar and side menu components, toast messages, typed navigation (`goToPim()`, `goToAdmin()`) and logout. |
| **BaseListPage** | Shared logic for search screens: filter inputs, search with table-refresh detection, reading columns, deleting rows. |
| **Components** | `TopBar` and `SideMenu` appear on every page; they are written once and composed into pages. |
| **Models** | `Employee` (immutable, Builder pattern), `LoginData` and `ModuleData` (Java records) represent test data as typed objects instead of loose strings. |
| **JsonDataReader / TestDataProviders** | Load JSON test data into models and feed it to data-driven tests, so new cases are added in JSON without code changes. |
| **TestDataGenerator** | Creates unique IDs and names so tests don't clash with each other or with other users of the shared demo, including when a test is retried. |
| **TestListener** | Hooks into TestNG events: creates a report entry per test, records pass/fail/skip, and attaches failure screenshots to both the Extent and TestNG reports. |
| **RetryAnalyzer / RetryTransformer** | Re-runs a failed test (`retry.max.count`, default 1) to absorb occasional slowness of the public demo server; applied to every test automatically. |
| **StepLogger** | One `step("…")` call writes the step to the log file, the Extent report and TestNG's report. |
| **BaseTest** | Opens a fresh browser before each test, captures a screenshot on failure before closing it, and provides helpers such as `loginAsAdmin()`. |

## How it works end to end

```
mvnw test -Dsuite=smoke
   │
   ├─ Maven Surefire loads suites/smoke.xml ──► TestNG selects tests in the "smoke" group
   │                                            and registers TestListener + RetryTransformer
   ├─ TestListener.onStart      → ExtentReports is created
   │
   │  For each test (2 classes in parallel):
   ├─ BaseTest.setUp            → ConfigReader picks the browser → DriverFactory → DriverManager (ThreadLocal)
   ├─ TestListener.onTestStart  → report entry created
   ├─ Test method               → page objects (LoginPage → DashboardPage → EmployeeListPage …)
   │                               each page waits until loaded; actions use WaitUtils + ElementActions
   │                               step("…") writes to log + Extent + TestNG reports
   ├─ Assertions                → TestNG Assert / SoftAssert
   ├─ BaseTest.tearDown         → on failure: screenshot captured, then browser closed
   ├─ TestListener              → pass/fail recorded, screenshot embedded; failed tests retried once
   │
   └─ TestListener.onFinish     → reports/extent-report.html written
```

## OOP concepts in this framework

| Concept | Where it is applied |
|---|---|
| **Encapsulation** | Locators are `private static final` inside each page; tests call business methods only (`loginAs`, `searchByEmployeeId`). `Employee` has private final fields set only through its Builder. `ConfigReader` hides its `Properties` behind typed getters. |
| **Inheritance** | `BasePage → AuthenticatedPage → BaseListPage → EmployeeListPage / SystemUsersPage`; `BaseComponent → TopBar / SideMenu`; `BrowserDriver → Chrome/Firefox/EdgeBrowserDriver`; `FrameworkException → ConfigurationException / ElementInteractionException`; every test class extends `BaseTest`. |
| **Polymorphism** | `DriverFactory` works with the `BrowserDriver` type, and each subclass overrides `createDriver()`. Every page overrides `urlFragment()` and `pageIdentifier()`, and `BasePage.waitForPageToLoad()` calls the right version at run time. Listeners implement TestNG interfaces (`ITestListener`, `IRetryAnalyzer`, `IAnnotationTransformer`). |
| **Abstraction** | `BasePage`, `AuthenticatedPage`, `BaseListPage`, `BaseComponent`, `BrowserDriver` and `BaseTest` are abstract: they define *what* a page, browser or test must provide and hide *how* the shared work is done. |

Other patterns: **Singleton** (`ConfigReader`, `ExtentManager`), **Factory** (`DriverFactory`), **Builder** (`Employee`), **Template Method** (`BrowserDriver.start()`), **Fluent interface** (page methods return the next page).

## Adding a new test

1. **New page?** Create a class in `pages/` that extends `AuthenticatedPage` (or `BaseListPage` for a search screen). Add private locators, implement `urlFragment()` and `pageIdentifier()`, call `waitForPageToLoad()` in the constructor, and expose business methods.
2. **New test?** Create a class in `tests/` that extends `BaseTest`, tag methods with `groups = "regression"` (and `"smoke"` if it is critical), and use `step("…")` for readable reports.
3. **New data?** Add rows to a JSON file in `testdata/`, or a new file plus a `@DataProvider`.

## Notes

- The application under test is the **public OrangeHRM demo**. The credentials in `config.properties` are the public demo credentials shown on its login page. For any real system, pass credentials with `-Dadmin.password=…` or an environment variable, and never commit them.
- The demo is shared by many users and resets periodically, so tests create uniquely named data and clean up after themselves.
