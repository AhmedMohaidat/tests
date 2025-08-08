# RestAssured Test Suite

A comprehensive API testing framework built with RestAssured, Java, and TestNG for automated testing of REST APIs.

## 🚀 Quick Start

### Prerequisites

- **Java 23** or higher
- **Maven 3.6+**
- **Git**

### Clone and Setup

```bash
git clone <repository-url>
cd <repository-name>
mvn clean compile
```

## 🧪 Running Tests

### Local Execution

#### Run All Regression Tests (Default)
```bash
mvn clean install -Dfile="src/test/resources/TestSuites/regressionTest.xml"
```

#### Run Regression Tests
```bash
mvn clean install -Dfile="src/test/resources/TestSuites/regressionTest.xml"
```

### CI/CD Execution

The project uses GitHub Actions for automated testing:

- **Automatic Triggers**: Tests run on push/PR to `main` or `develop` branches
- **Manual Execution**: Use GitHub Actions "Run workflow" button to run the regression test suite
- **Test Suite**: Regression Test Suite

## 📁 Project Structure

```
├── src/
│   └── test/
│       ├── java/              # Test classes
│       └── resources/
│           └── TestSuites/    # TestNG XML files
│               └── regressionTest.xml
├── target/
│   └── surefire-reports/      # Test execution reports
├── ExtentReports/             # Extent HTML reports
├── test-output/               # TestNG output
└── pom.xml                    # Maven configuration
```

## 📊 Test Reports

After test execution, reports are available in:

- **Surefire Reports**: `target/surefire-reports/`
- **Extent Reports**: `ExtentReports/` or `reports/`
- **TestNG Output**: `test-output/`

### Viewing Reports in CI/CD

1. Go to **Actions** tab in GitHub
2. Select the workflow run
3. Download artifacts:
    - `test-results` - Contains surefire reports and TestNG output
    - `extent-reports` - Contains HTML reports with detailed test results

## 🔧 Configuration

### Test Suites

Test suites are configured in `src/test/resources/TestSuites/`:

- **regressionTest.xml** - Complete regression test suite

### Maven Configuration

Key dependencies (configured in `pom.xml`):
- RestAssured for API testing
- TestNG for test framework
- Extent Reports for reporting

## 🏃‍♂️ GitHub Actions Workflow

### Manual Test Execution

1. Navigate to **Actions** tab
2. Select "RestAssured Test Suite" workflow
3. Click "Run workflow"
4. Click "Run workflow" button to execute the regression test suite

### Automated Execution

Tests automatically run when:
- Code is pushed to `main` or `develop` branches
- Pull requests are created targeting `main` or `develop`

## 📝 Development Guidelines

### Adding New Tests

1. Create test classes in `src/test/java/`
2. Add test methods to the regression test suite in `src/test/resources/TestSuites/regressionTest.xml`
3. Follow RestAssured patterns for API testing
4. Ensure proper test annotations and groups

### Test Data Management

- Store test data in appropriate resource files
- Use TestNG data providers for parameterized tests
- Maintain environment-specific configurations

## 🐛 Troubleshooting

### Common Issues

**Build Failures:**
- Ensure Java 23 is installed and configured
- Verify Maven dependencies are resolved: `mvn dependency:resolve`

**Test Failures:**
- Check API endpoint availability
- Verify test data and configurations
- Review logs in surefire-reports for detailed error information

### Getting Help

1. Check test execution logs in `target/surefire-reports/`
2. Review Extent Reports for detailed test results
3. Verify API endpoints are accessible
4. Ensure test environment is properly configured

## 🤝 Contributing

1. Create feature branch from `develop`
2. Add/modify tests as needed
3. Ensure all tests pass locally
4. Create pull request to `develop` branch
5. CI/CD will automatically run tests on PR

---

**Happy Testing!** 🎯