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

### Component Overview
```
TestDataSetBase (Base Configuration)
    ↓
MockServerConfig (Mock Management)
    ↓
API Test Classes (Test Implementation)
```

### Key Components

#### 1. TestDataSetBase
- **Purpose**: Base configuration and mock server lifecycle management
- **Responsibilities**:
    - Initialize configuration from `testConfig.json`
    - Start/stop mock server based on `thirdParty` setting
    - Manage common test data and utilities

#### 2. MockServerConfig
- **Purpose**: Centralized mock server configuration and management
- **Responsibilities**:
    - Mock server startup/shutdown
    - Endpoint mock setup and management
    - Request/response pattern definitions
    - Debug utilities for troubleshooting

#### 3. Configuration Switch
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

### 3. Mock Categories

#### Authentication Mocks (`setupAuthMocks()`)
- Valid login scenarios
- Token generation
- Authorization header validation

#### TopUp Mocks
- **Success Scenarios**: Valid requests with proper authorization
- **Validation Scenarios**: Invalid amounts, non-numeric values
- **Error Scenarios**: Missing authorization, malformed requests

#### Default Mocks
- Health check endpoints
- Common utility endpoints

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

### 1. Adding New API Mocks

When adding support for a new API endpoint:

1. **Create Setup Method**:
   ```java
   public void setupNewApiMocks() {
       // Success scenario
       addMockExpectation(request().withMethod("POST").withPath("/api/newendpoint"), 
                         response().withStatusCode(200));
   }
   ```

2. **Add Error Scenarios**:
   ```java
   public void setupNewApiErrorMocks() {
       // Various error conditions
   }
   ```

3. **Update Scenario Manager**:
   ```java
   public void setupNewApiTestScenario(String scenario) {
       switch (scenario) {
           case "success": setupNewApiMocks(); break;
           case "error": setupNewApiErrorMocks(); break;
       }
   }
   ```

### 2. Test Class Implementation

```java
public class NewApiTest extends ApiTestBase {
    
    @BeforeMethod
    public void setupScenario() {
        if (thirdParty.equalsIgnoreCase("mock")) {
            mockServerConfig.setupNewApiTestScenario("success");
        }
    }
    
    @Test
    public void testSuccessScenario() {
        // Test implementation
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
- `testConfig.json`: Environment and mode configuration
- `MockServerConfig.java`: Mock server setup and management
- `TestDataSetBase.java`: Base test configuration and lifecycle

### Essential Methods
- `setupMockServer()`: Initialize mock server
- `setupTestScenario(String scenario)`: Configure specific test scenarios
- `printRecordedRequests()`: Debug request matching issues
- `resetMockServer()`: Clean slate for new test scenarios

### Common Mock Patterns
- Exact body matching: `.withBody(exact("{\"field\":\"value\"}"))`
- Header patterns: `.withHeader("Authorization", "Bearer .*")`
- Path handling: Always include both `/api/path` and `api/path` variants