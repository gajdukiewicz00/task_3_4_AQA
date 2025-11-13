# BDD Implementation with Cucumber

## 📋 Overview

This project now includes a BDD (Behavior-Driven Development) layer using Cucumber for testing defects and enhancements on the InMotion Hosting website.

## 🏗️ Architecture

### Components Added:

1. **Cucumber Dependencies** (`pom.xml`)
   - `cucumber-java` (7.18.0)
   - `cucumber-testng` (7.18.0)
   - `cucumber-picocontainer` (7.18.0) - for dependency injection
   - `allure-cucumber7-jvm` (2.27.0) - for Allure integration

2. **Cucumber Runner** (`CucumberTestRunner.java`)
   - Extends `AbstractTestNGCucumberTests`
   - Configured to run features from `src/test/resources/features`
   - Integrated with Allure reporting

3. **Hooks** (`CucumberHooks.java`)
   - `@Before` hook: Initializes WebDriver and navigates to homepage
   - `@After` hook: Takes screenshots on failure and cleans up WebDriver

4. **Feature Files**
   - `search_functionality.feature` - Tests search functionality
   - `cookie_banner.feature` - Tests cookie banner behavior

5. **Step Definitions**
   - `SearchStepDefinitions.java` - Implements search-related steps
   - `CookieBannerStepDefinitions.java` - Implements cookie banner-related steps

## 🐛 Defects/Enhancements Covered

### Defect 1: Search Functionality
**Location**: `search_functionality.feature`

**Scenarios**:
- Search should display relevant results for valid queries
- Search should handle different search terms (Scenario Outline)
- Search should handle empty search gracefully

**Potential Issues Tested**:
- Search field visibility and accessibility
- Search results relevance
- Empty search handling
- Search URL navigation

### Defect 2: Cookie Banner Behavior
**Location**: `cookie_banner.feature`

**Scenarios**:
- Cookie banner should be dismissible and stay dismissed
- Cookie banner should have accept and decline options
- Cookie banner should work with different actions (Scenario Outline)

**Potential Issues Tested**:
- Cookie banner persistence after refresh (defect: banner may reappear)
- Missing decline/reject option (enhancement opportunity)
- Cookie consent storage

**Important Note**: The `BasePage` automatically accepts cookie banners via `CookieBanner.acceptIfPresent()`. Some cookie banner tests may pass or fail depending on timing. This automatic dismissal represents the framework's current behavior, and the tests document potential improvements to cookie consent handling.

## 🚀 Running BDD Tests

### Run all BDD tests:
```bash
mvn test -Dtest=CucumberTestRunner
```

### Run specific feature:
```bash
mvn test -Dcucumber.filter.tags="@defect-1"
mvn test -Dcucumber.filter.tags="@defect-2"
```

### Run with TestNG:
```bash
mvn test
```
This will run both smoke tests and BDD tests as configured in `testng.xml`

## 📊 Reports

### Cucumber HTML Report
Location: `target/cucumber-reports/index.html`

### Allure Report
```bash
mvn allure:serve
```

## 📝 Feature File Structure

### Tags Used:
- `@bdd` - All BDD scenarios
- `@defect-1` - Search functionality tests
- `@defect-2` - Cookie banner tests
- `@search` - Search-related scenarios
- `@cookie` - Cookie banner-related scenarios

### Scenario Outline Usage:
Both feature files use `Scenario Outline` to test multiple variations:
- Search functionality tests different search terms
- Cookie banner tests different action buttons

## 🔧 Integration with Existing Framework

The BDD layer integrates seamlessly with the existing framework:
- Uses the same `DriverFactory` for WebDriver management
- Uses the same `PageFactoryManager` for page objects
- Uses the same `Waiter` utilities
- Uses the same `Config` for configuration
- Integrates with Allure reporting
- Works alongside existing TestNG tests

## 📌 Notes

1. **Search Functionality**: The step definitions include fallback mechanisms to navigate directly to search URLs if the search UI is not found, ensuring tests can still validate search behavior.

2. **Cookie Banner**: Tests are designed to identify defects such as:
   - Banner reappearing after refresh (preference not persisted)
   - Missing decline/reject options (enhancement opportunity)

3. **Error Handling**: All step definitions include robust error handling and fallback mechanisms to handle various UI states.

4. **Screenshots**: Screenshots are automatically captured on test failure and attached to both Cucumber and Allure reports.

