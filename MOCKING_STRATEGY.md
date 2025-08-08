# API Mocking Strategy Guide

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Configuration](#configuration)
4. [Mock Setup Strategies](#mock-setup-strategies)
5. [Best Practices](#best-practices)
6. [Usage Guidelines](#usage-guidelines)
7. [Troubleshooting](#troubleshooting)
8. [Contributing](#contributing)

## Overview

This document outlines our API mocking strategy using MockServer to enable reliable, fast, and independent API testing. Our framework supports both real API testing and mocked API testing through configuration switches.

### Benefits of Our Mocking Strategy
- **Environment Independence**: Tests run without external API dependencies
- **Consistent Response Times**: Predictable performance for reliable test execution
- **Scenario Control**: Easy simulation of edge cases and error conditions
- **Parallel Execution**: No conflicts with shared test data
- **Development Velocity**: Faster feedback loops during development

## Architecture

### Framework Structure Overview
```
src/main/java/
├── base/
│   ├── APITestBase/ApiTestBase
│   ├── MockServer/MockServerConfig  
│   └── TestDataBase/TestDataSetBase
├── APISchema/ (API Classes)
│   ├── Admin/
│   ├── Auth/ 
│   ├── Deposit/
│   └── TopUp/
├── APITests/ (Test Classes)
│   ├── Admin/
│   ├── Auth/
│   ├── Deposit/ 
│   └── TopUp/
├── config/testConfig.json
└── resources/ApiDataSchema/ (Test Data)
```

### Component Flow
```
TestDataSetBase (Base Configuration)
    ↓
ApiTestBase (API Test Foundation)  
    ↓
MockServerConfig (Mock Management)
    ↓
APISchema Classes (API Implementation)
    ↓
APITests Classes (Test Execution)
```

### Key Components

#### 1. TestDataSetBase (`base/TestDataBase/`)
- **Purpose**: Base configuration and mock server lifecycle management
- **Responsibilities**:
    - Initialize configuration from `testConfig.json`
    - Start/stop mock server based on `thirdParty` setting
    - Manage common test data and utilities
    - Handle test reporting and logging

#### 2. ApiTestBase (`base/APITestBase/`)
- **Purpose**: Foundation for all API test classes
- **Responsibilities**:
    - Inherit mock server configuration
    - Provide common API testing utilities
    - Manage test execution context

#### 3. MockServerConfig (`base/MockServer/`)
- **Purpose**: Centralized mock server configuration and management
- **Responsibilities**:
    - Mock server startup/shutdown
    - Endpoint mock setup and management
    - Request/response pattern definitions
    - Debug utilities for troubleshooting

#### 4. APISchema Classes (`APISchema/`)
- **Purpose**: API implementation and request/response handling
- **Responsibilities**:
    - Define API endpoints and methods
    - Handle request construction
    - Process API responses
    - Work with both real and mocked endpoints

#### 5. APITests Classes (`APITests/`)
- **Purpose**: Test implementation and execution
- **Responsibilities**:
    - Define test scenarios and assertions
    - Use APISchema classes for API calls
    - Leverage mock configurations for different test scenarios

#### 6. Configuration Management
Uses `testConfig.json` to control execution mode:
- `"thirdParty": "real"` → Tests run against real APIs
- `"thirdParty": "mock"` → Tests run against MockServer

## Configuration

### testConfig.json Structure
```json
{
  "baseUrl": "http://localhost:5000",
  "mockUrl": "http://localhost:1080", 
  "thirdParty": "mock",  // "real" or "mock"
  "retries": 0,
  "mockServerPort": 1080
}
```

### Environment Setup
1. **Development/CI**: Use `"thirdParty": "mock"` for fast, reliable testing
2. **Integration Testing**: Use `"thirdParty": "real"` for end-to-end validation
3. **Local Development**: Switch between modes as needed

## Mock Setup Strategies

### 1. Scenario-Based Mock Setup

Our framework uses scenario-based mock configuration to handle different test cases:

```java
public void setupTopUpTestScenario(String scenario) {
    switch (scenario.toLowerCase()) {
        case "success":
            setupTopUpMocks();
            break;
        case "unauthorized":
            setupTopUpUnauthorizedMocks();
            break;
        case "validation":
            setupTopUpValidationMocks();
            break;
    }
}
```

### 2. Hierarchical Mock Patterns

Mocks are organized in order of specificity:
1. **Exact Match Mocks**: Specific request body and headers
2. **Pattern-Based Mocks**: Header-only or path-only matching
3. **Fallback Mocks**: General catch-all responses

### Framework-Specific Mock Scenarios

Based on your API structure, here are the recommended mock scenarios for each module:

#### Admin Module Mocks
```java
public void setupAdminTestScenario(String scenario) {
    switch (scenario.toLowerCase()) {
        case "approve-deposit":
            setupApproveDepositMocks();
            break;
        case "create-offer":
            setupCreateOfferMocks(); 
            break;
        case "delete-offer":
            setupDeleteOfferMocks();
            break;
        case "reject-deposit":
            setupRejectDepositMocks();
            break;
        case "update-offer":
            setupUpdateOfferMocks();
            break;
        case "unauthorized":
            setupAdminUnauthorizedMocks();
            break;
    }
}
```

#### Auth Module Mocks
```java
public void setupAuthTestScenario(String scenario) {
    switch (scenario.toLowerCase()) {
        case "login-success":
            setupLoginSuccessMocks();
            break;
        case "login-invalid":
            setupLoginInvalidMocks();
            break;
        case "register-success":
            setupRegisterSuccessMocks();
            break;
        case "register-duplicate":
            setupRegisterDuplicateMocks();
            break;
        case "update-user-success":
            setupUpdateUserMocks();
            break;
    }
}
```

#### Deposit Module Mocks
```java
public void setupDepositTestScenario(String scenario) {
    switch (scenario.toLowerCase()) {
        case "available-deposits":
            setupAvailableDepositsMocks();
            break;
        case "crowd-enroll":
            setupCrowdEnrollMocks();
            break;
        case "get-all-deposits":
            setupGetAllDepositsMocks();
            break;
        case "get-deposit-by-id":
            setupGetDepositByIdMocks();
            break;
        case "request-deposit":
            setupRequestDepositMocks();
            break;
    }
}
```

#### TopUp Module Mocks (Already Implemented)
```java
public void setupTopUpTestScenario(String scenario) {
    switch (scenario.toLowerCase()) {
        case "success":
            setupTopUpMocks();
            break;
        case "unauthorized":  
            setupTopUpUnauthorizedMocks();
            break;
        case "validation":
            setupTopUpValidationMocks();
            break;
    }
}
```

## Best Practices

### 1. Mock Design Principles

#### Exact Body Matching for Reliability
```java
// Preferred: Exact matching for predictable behavior
.withBody(exact("{\"amount\":\"x\"}"))

// Avoid: Regex patterns that might be too broad
.withBody(matching(".*amount.*"))
```

#### Handle Path Variations
```java
// Always handle both path formats
.withPath("/api/TopUp")     // With leading slash
.withPath("api/TopUp")      // Without leading slash
```

#### Progressive Mock Specificity
```java
// Order from most specific to most general
1. Exact body + specific headers
2. Header-only matching  
3. Path-only matching (fallback)
```

### 2. Mock Lifecycle Management

#### Setup Strategy
1. **Reset Before Setup**: Always reset existing mocks before adding new ones
2. **Restore Defaults**: Re-add essential mocks after reset
3. **Scenario Isolation**: Each test scenario gets clean mock state

```java
public void setupTopUpValidationMocks() {
    // Reset and restore essentials
    if (mockServer != null && mockServer.isRunning()) {
        mockServer.reset();
        setupDefaultMocks();
        setupAuthMocks();
    }
    
    // Add scenario-specific mocks
    addValidationMocks();
}
```

#### Test Class Integration
```java
@BeforeClass(alwaysRun = true)
public void setupMockServer() {
    if (thirdParty.equalsIgnoreCase("mock")) {
        mockServerConfig.setupTopUpTestScenario("validation");
    }
}
```

### 3. Response Design

#### Consistent Response Format
```java
// Success responses
{
  "message": "Account Successfully Topped Up, New Balance is 1500"
}

// Error responses  
{
  "error": "Bad Request - Non-numeric amount"
}

// Auth responses
{
  "token": "eyJhbGciOiJIUzI1NiJ9.mock-token",
  "status": "success"
}
```

#### Realistic Status Codes
- `200`: Successful operations
- `400`: Client errors (validation, malformed requests)
- `401`: Authentication/authorization failures
- `500`: Server errors (when testing error handling)

### 4. Debug and Monitoring

#### Request Logging
```java
public void printRecordedRequests() {
    var requests = mockServer.retrieveRecordedRequests(null);
    for (var req : requests) {
        System.out.println("Method: " + req.getMethod());
        System.out.println("Path: " + req.getPath());
        System.out.println("Body: " + req.getBodyAsString());
    }
}
```

#### Endpoint Verification
```java
public boolean wasEndpointCalled(String method, String path) {
    return mockServer.retrieveRecordedRequests(
        request().withMethod(method).withPath(path)
    ).length > 0;
}
```

## Usage Guidelines

### 1. Adding New API Module Mocks

When adding support for a new API module (e.g., Payments, Notifications):

1. **Create Module-Specific Setup Methods**:
   ```java
   // In MockServerConfig.java
   public void setupPaymentMocks() {
       addMockExpectation(
           request()
               .withMethod("POST")
               .withPath("/api/Payment")
               .withHeader("Authorization", "Bearer .*"),
           response()
               .withStatusCode(200)
               .withContentType(MediaType.APPLICATION_JSON)
               .withBody("{\"message\": \"Payment processed successfully\"}")
       );
   }
   
   public void setupPaymentErrorMocks() {
       // Various error scenarios
   }
   
   public void setupPaymentTestScenario(String scenario) {
       switch (scenario.toLowerCase()) {
           case "success": setupPaymentMocks(); break;
           case "insufficient-funds": setupPaymentInsufficientFundsMocks(); break;
           case "invalid-card": setupPaymentInvalidCardMocks(); break;
           case "unauthorized": setupPaymentUnauthorizedMocks(); break;
       }
   }
   ```

2. **Create APISchema Class** (`APISchema/Payment/PaymentAPI.java`):
   ```java
   public class PaymentAPI extends ApiTestBase {
       
       public void processPayment(String amount, String method) {
           // Implementation that works with both real and mock APIs
           Response response = given()
               .header("Authorization", "Bearer " + getAuthToken())
               .header("Content-Type", "application/json")
               .body(buildPaymentRequest(amount, method))
               .post("/api/Payment");
               
           // Handle response
       }
   }
   ```

3. **Create Test Class** (`APITests/Payment/PaymentTest.java`):
   ```java
   public class PaymentTest extends ApiTestBase {
       
       PaymentAPI paymentAPI;
       
       @BeforeMethod
       public void setupMocks() {
           if (thirdParty.equalsIgnoreCase("mock")) {
               mockServerConfig.setupPaymentTestScenario("success");
           }
       }
       
       @Test(description = "Process payment successfully")
       public void processPaymentSuccess() {
           paymentAPI = new PaymentAPI();
           paymentAPI.processPayment("100.00", "credit_card");
       }
       
       @Test(description = "Payment with insufficient funds")
       public void paymentInsufficientFunds() {
           if (thirdParty.equalsIgnoreCase("mock")) {
               mockServerConfig.setupPaymentTestScenario("insufficient-funds");
           }
           
           paymentAPI = new PaymentAPI();
           paymentAPI.processPayment("10000.00", "credit_card");
       }
   }
   ```

4. **Add Test Data** (`resources/ApiDataSchema/Payment/`):
   ```json
   // PaymentData.json
   {
     "validPayment": {
       "amount": "100.00",
       "method": "credit_card",
       "currency": "USD"
     },
     "invalidPayment": {
       "amount": "invalid",
       "method": "unknown_method"
     }
   }
   ```

### 2. Framework Integration Pattern

Based on your framework structure, here's how to integrate mocks:

#### APISchema Class (`APISchema/TopUp/AddFundsAPI.java`)
```java
public class AddFundsAPI extends ApiTestBase {
    
    public void addFunds() {
        // This method works with both real and mock endpoints
        // The baseUrl is automatically set based on thirdParty config
        
        // API implementation that works with both real and mock
        Response response = given()
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .post("/api/TopUp");
            
        // Validation logic
    }
}
```

#### Test Class (`APITests/TopUp/AddFundsTest.java`)
```java
public class AddFundsTest extends ApiTestBase {
    
    AddFundsAPI addFunds;
    
    @BeforeMethod
    public void setupScenario() {
        if (thirdParty.equalsIgnoreCase("mock")) {
            // Setup specific scenario for each test method
            mockServerConfig.setupTopUpTestScenario("success");
        }
    }
    
    @Test(description = "Add funds to the authenticated user's wallet Successfully")
    public void addFundsBalance() {
        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }
    
    @Test(description = "Invalid top-up request or amount")  
    public void invalidData() {
        if (thirdParty.equalsIgnoreCase("mock")) {
            mockServerConfig.setupTopUpTestScenario("validation");
        }
        
        addFunds = new AddFundsAPI();
        // Test with invalid data
    }
    
    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        if (thirdParty.equalsIgnoreCase("mock")) {
            mockServerConfig.setupTopUpTestScenario("unauthorized");
        }
        
        addFunds = new AddFundsAPI();
        // Test without proper authentication
    }
}
```

### 3. Configuration Management

#### Development Environment
```json
{
  "thirdParty": "mock",
  "mockServerPort": 1080
}
```

#### CI/CD Pipeline
```json
{
  "thirdParty": "mock",
  "mockServerPort": 1081  // Different port to avoid conflicts
}
```

#### Integration Testing
```json
{
  "thirdParty": "real",
  "baseUrl": "http://staging-api.company.com"
}
```

## Troubleshooting

### Common Issues and Solutions

#### 1. Mock Not Matching Requests
**Symptoms**: Tests failing with unexpected responses or timeouts

**Debug Steps**:
1. Enable request logging: `mockServerConfig.printRecordedRequests()`
2. Verify exact request format matches mock expectations
3. Check path variations (leading slash, case sensitivity)
4. Validate header matching patterns

**Solution**: Use exact matching instead of regex patterns for reliability

#### 2. Mock Server Port Conflicts
**Symptoms**: `java.net.BindException: Address already in use`

**Solutions**:
- Change `mockServerPort` in testConfig.json
- Ensure proper cleanup with `@AfterSuite` methods
- Use dynamic port allocation for parallel execution

#### 3. Test Isolation Issues
**Symptoms**: Tests affecting each other, inconsistent results

**Solutions**:
- Always reset mocks between test classes
- Use `@BeforeMethod` for scenario-specific setup
- Implement proper mock lifecycle management

#### 4. Missing Authorization Mocks
**Symptoms**: 401 errors in mock mode when expecting success

**Solutions**:
- Ensure `setupAuthMocks()` is called after mock reset
- Verify Authorization header patterns match exactly
- Check token format expectations

### Debug Checklist
- [ ] Verify `thirdParty` configuration setting
- [ ] Check mock server startup logs
- [ ] Review recorded requests vs. expectations
- [ ] Validate response format matches test expectations
- [ ] Ensure proper mock reset/setup sequence

## Contributing

### Adding New Mocks
1. Follow the established naming conventions: `setup[Feature][Scenario]Mocks()`
2. Always handle both path formats (`/api/endpoint` and `api/endpoint`)
3. Include comprehensive error scenarios
4. Add debug logging for troubleshooting
5. Update this documentation with new patterns

### Code Review Guidelines
- Verify mock isolation between test scenarios
- Ensure realistic response formats
- Check for proper cleanup and lifecycle management
- Validate debug utilities are included

### Testing Your Mocks
1. Test in both `mock` and `real` modes
2. Verify all scenario combinations
3. Check edge cases and error conditions
4. Validate cleanup and reset functionality

---

## Quick Reference

### Key Configuration Files
- `config/testConfig.json`: Environment and mode configuration
- `base/MockServer/MockServerConfig.java`: Mock server setup and management
- `base/TestDataBase/TestDataSetBase.java`: Base test configuration and lifecycle
- `base/APITestBase/ApiTestBase.java`: API test foundation class

### Framework Directory Structure
- `APISchema/`: API implementation classes (work with both real and mock APIs)
- `APITests/`: Test execution classes (inherit from ApiTestBase)
- `resources/ApiDataSchema/`: Test data and JSON schemas organized by module
- `resources/TestData.Auth/`: Authentication-specific test data
- `resources/TestSuites/`: Test suite configurations

### Essential Methods
- `setupMockServer()`: Initialize mock server
- `setupTestScenario(String scenario)`: Configure specific test scenarios
- `printRecordedRequests()`: Debug request matching issues
- `resetMockServer()`: Clean slate for new test scenarios

### Common Mock Patterns
- Exact body matching: `.withBody(exact("{\"field\":\"value\"}"))`
- Header patterns: `.withHeader("Authorization", "Bearer .*")`
- Path handling: Always include both `/api/path` and `api/path` variants