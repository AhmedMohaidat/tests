# API Testing Mocking Strategy Documentation

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Implementation](#implementation)
4. [Configuration](#configuration)
5. [Usage Examples](#usage-examples)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)
8. [Maintenance](#maintenance)

## Overview

### Purpose
This document outlines the mocking strategy implemented for API regression testing, enabling:
- **Isolated Testing**: Tests run independently of external services
- **Faster Execution**: No network latency or service dependencies
- **Reliable Results**: Consistent responses regardless of external service status
- **Cost Efficiency**: Reduced load on real services during development/testing

### Key Benefits
- ✅ **Deterministic**: Same input always produces same output
- ✅ **Fast**: Local mock server responses in milliseconds
- ✅ **Isolated**: No dependency on external services
- ✅ **Flexible**: Easy to simulate error conditions
- ✅ **Maintainable**: Centralized mock configuration

## Architecture

### High-Level Architecture
```
Test Suite
    ↓
TestDataSetBase (Config Management)
    ↓
MockServerConfig (Mock Server Control)
    ↓
MockServer (Port 1080)
    ↓
API Classes (LoginAPI, AddFundsAPI, etc.)
```

### Component Responsibilities

| Component | Responsibility |
|-----------|----------------|
| `TestDataSetBase` | Configuration management, environment detection |
| `MockServerConfig` | Mock server lifecycle, mock setup, request/response definitions |
| `MockServer` | HTTP server simulation, request matching, response serving |
| `API Classes` | API request construction, response handling |

## Implementation

### Core Components

#### 1. TestDataSetBase
```java
// Automatically switches between real and mock based on configuration
public TestDataSetBase() {
    thirdParty = (String) testConfig.get("thirdParty");
    baseUrl = (String) testConfig.get("baseUrl");
    if (thirdParty.equalsIgnoreCase("mock")){
        baseUrl = (String) testConfig.get("mockUrl");
    }
}
```

#### 2. MockServerConfig Structure
```java
public class MockServerConfig extends TestDataSetBase {
    // Server Management
    public void startMockServer()
    public void stopMockServer()
    public void resetMockServer()
    
    // Mock Setup
    public void setupAuthMocks()
    public void setupTopUpMocks()
    public void setupTopUpErrorMocks()
    
    // Utilities
    public void addMockExpectation(HttpRequest, HttpResponse)
    public boolean wasEndpointCalled(String method, String path)
    public void printRecordedRequests()
}
```

#### 3. Mock Response Patterns
```java
// Success Response Pattern
response()
    .withStatusCode(200)
    .withContentType(MediaType.APPLICATION_JSON)
    .withBody(jsonResponseBody)

// Error Response Pattern  
response()
    .withStatusCode(400)
    .withContentType(MediaType.APPLICATION_JSON)
    .withBody("{\"error\":\"ERROR_CODE\",\"message\":\"Error description\"}")
```

## Configuration

### Environment Configuration (`testConfig.json`)

#### For Mock Testing
```json
{
  "baseUrl": "http://localhost:5000",
  "mockUrl": "http://localhost:1080", 
  "thirdParty": "mock",
  "retries": 0,
  "sendEmail": false,
  "recipients": ["mohaidatahmed@gmail.com"]
}
```

#### For Real API Testing
```json
{
  "baseUrl": "http://localhost:5000",
  "mockUrl": "http://localhost:1080",
  "thirdParty": "real",
  "retries": 0,
  "sendEmail": false,
  "recipients": ["mohaidatahmed@gmail.com"]
}
```

### API Data Configuration

#### Login Data (`ApiDataSchema/Auth/loginData.json`)
```json
{
  "endPoint": "api/Auth/login",
  "headers": {
    "accept": "*/*",
    "Content-Type": "application/json"
  },
  "body": {
    "email": "mohaidatahmed@gmail.com",
    "password": "Pass123@"
  }
}
```

#### TopUp Data (`ApiDataSchema/TopUp/TopUp.json`)
```json
{
  "endPoint": "api/TopUp",
  "headers": {
    "accept": "*/*",
    "Content-Type": "application/json"
  },
  "body": {
    "amount": 500
  }
}
```

## Usage Examples

### Basic Test Setup
```java
public class AddFundsTest {
    private static MockServerConfig mockServerConfig;

    @BeforeClass
    public void setupMockServer() {
        mockServerConfig = new MockServerConfig();
        mockServerConfig.startMockServer();
        mockServerConfig.setupAuthMocks();
        mockServerConfig.setupTopUpMocks();
    }

    @AfterClass  
    public void tearDownMockServer() {
        if (mockServerConfig != null) {
            mockServerConfig.stopMockServer();
        }
    }
}
```

### Test Implementation
```java
@Test(description = "Add Funds to user Successfully")
public void addFundsBalance() {
    AddFundsAPI addFunds = new AddFundsAPI();
    addFunds.addFunds();
    
    // Verify mock was called
    assertTrue(mockServerConfig.wasEndpointCalled("POST", "/api/TopUp"));
}
```

### Custom Mock for Specific Test
```java
@Test(description = "Test insufficient balance scenario")
public void testInsufficientBalance() {
    // Setup error scenario mock
    mockServerConfig.setupTopUpErrorMocks();
    
    AddFundsAPI addFunds = new AddFundsAPI();
    addFunds.addFunds();
}
```

### Adding Custom Mock Expectation
```java
mockServerConfig.addMockExpectation(
    request()
        .withMethod("POST")
        .withPath("/api/TopUp")
        .withBody(".*\"amount\"\\s*:\\s*1000.*"),
    response()
        .withStatusCode(200)
        .withBody("{\"status\":\"SUCCESS\",\"amount\":1000}")
);
```

## Best Practices

### 1. Mock Organization
- **Group related mocks** into logical methods (`setupAuthMocks()`, `setupTopUpMocks()`)
- **Use descriptive names** for mock methods and response builders
- **Keep mocks simple** but realistic

### 2. Response Design
- **Match real API structure** as closely as possible
- **Include all required fields** that your tests depend on
- **Use realistic data** (timestamps, IDs, amounts)
- **Handle both success and error scenarios**

### 3. Test Structure
- **Setup mocks in @BeforeClass** for efficiency
- **Clean up resources in @AfterClass** to prevent port conflicts
- **Use specific mocks for error testing** rather than modifying global mocks
- **Add debug logging** when troubleshooting

### 4. Maintenance
- **Update mocks when APIs change** to maintain test relevance
- **Version control mock configurations** alongside tests
- **Document mock limitations** and differences from real APIs
- **Regular review** of mock accuracy vs real API behavior

## Troubleshooting

### Common Issues and Solutions

#### Issue: JsonPathException - Failed to parse JSON
**Symptom**: `groovy.json.JsonException: Lexing failed on line: 1, column: 1, while reading 'e'`

**Possible Causes**:
- Mock server not receiving requests (wrong endpoint path)
- API class using wrong baseUrl
- Mock response not matching expected format

**Debug Steps**:
1. Add debug logging to see actual URLs being called
2. Check `mockServerConfig.printRecordedRequests()`
3. Verify endpoint paths match between API data JSON and mocks
4. Confirm `thirdParty: "mock"` in config

#### Issue: No requests reaching mock server
**Debug Steps**:
```java
@Test
public void debugTest() {
    System.out.println("Mock server running: " + mockServerConfig.isMockServerRunning());
    System.out.println("Base URL: " + baseUrl);
    
    // Run your test
    addFunds.addFunds();
    
    // Check what requests were received
    mockServerConfig.printRecordedRequests();
}
```

#### Issue: Mock not matching request
**Common Mismatches**:
- Case sensitivity (`/api/Auth/login` vs `/api/auth/login`)
- Leading slash (`api/TopUp` vs `/api/TopUp`)
- Request body format
- Headers (Content-Type, etc.)

**Solution**: Use broader matchers initially, then narrow down
```java
// Broad matcher - catches any POST to login
.withMethod("POST")
.withPath(".*login.*")

// Specific matcher - exact path match
.withMethod("POST") 
.withPath("/api/Auth/login")
```

### Debugging Techniques

#### 1. Request Logging
```java
public void printRecordedRequests() {
    var requests = mockServer.retrieveRecordedRequests(null);
    LOGGER.info("Recorded requests count: {}", requests.length);
    for (var req : requests) {
        LOGGER.info("Request: {} {} - Body: {}", 
            req.getMethod(), req.getPath(), req.getBodyAsString());
    }
}
```

#### 2. Response Verification
```java
// In your test
Response response = // your API call
System.out.println("Status: " + response.statusCode());
System.out.println("Body: " + response.asPrettyString());
```

#### 3. Mock Verification
```java
// Verify specific endpoint was called
assertTrue(mockServerConfig.wasEndpointCalled("POST", "/api/Auth/login"));
```

## Maintenance

### Regular Tasks

#### 1. API Contract Validation
- **Monthly**: Compare mock responses with real API responses
- **On API updates**: Update corresponding mocks
- **Before releases**: Validate critical path mocks

#### 2. Performance Monitoring
- Monitor mock server startup/shutdown times
- Track test execution time improvements
- Identify flaky tests that might indicate mock issues

#### 3. Mock Coverage Review
- Ensure all API endpoints have corresponding mocks
- Verify error scenario coverage
- Document any limitations or differences from real APIs

### Version Control Best Practices
- Keep mock configurations in version control
- Tag mock versions with corresponding API versions
- Document breaking changes in mock behavior
- Maintain backward compatibility when possible

### Documentation Updates
- Update this document when adding new mock patterns
- Document any custom mock utilities or helpers
- Maintain troubleshooting section with new issues/solutions
- Keep configuration examples current

---

## Appendix

### Dependencies Required
```xml
<dependency>
    <groupId>org.mock-server</groupId>
    <artifactId>mockserver-netty</artifactId>
    <version>5.15.0</version>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.5</version>
</dependency>
```

### File Structure
```
src/
├── main/
│   ├── java/
│   │   └── config/
│   │       └── testConfig.json
│   └── resources/
│       └── ApiDataSchema/
│           ├── Auth/
│           │   └── loginData.json
│           └── TopUp/
│               └── TopUp.json
└── test/
    └── java/
        ├── base/
        │   ├── MockServer/
        │   │   └── MockServerConfig.java
        │   └── TestDataBase/
        │       └── TestDataSetBase.java
        └── APITests/
            └── TopUp/
                └── AddFundsTest.java
```

### Contact Information
- **Framework Owner**: [Your Name]
- **Documentation Version**: 1.0
- **Last Updated**: [Current Date]