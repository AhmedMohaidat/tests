package base.MockServer;

import base.TestDataBase.TestDataSetBase;
import io.restassured.RestAssured;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class MockServerConfig extends TestDataSetBase {

    private static ClientAndServer mockServer;
    private static final int DEFAULT_MOCK_PORT = 1080;
    private int mockServerPort;

    public MockServerConfig() {
        super();
        this.mockServerPort = testConfig.containsKey("mockServerPort")
                ? ((Long) testConfig.get("mockServerPort")).intValue()
                : DEFAULT_MOCK_PORT;
    }

    public void startMockServer() {
        if (mockServer == null || !mockServer.isRunning()) {
            mockServer = startClientAndServer(mockServerPort);
            configureMockServerUrl();
            setupDefaultMocks();
            LOGGER.info("MockServer started on port: {}", mockServerPort);
        }
    }

    public void stopMockServer() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.stop();
            LOGGER.info("MockServer stopped");
        }
    }

    public void resetMockServer() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.reset();
            setupDefaultMocks();
            LOGGER.debug("MockServer reset completed");
        }
    }

    public void addMockExpectation(HttpRequest request, HttpResponse response) {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.when(request).respond(response);
            LOGGER.debug("Added mock expectation: {} -> {}",
                    request.getPath(), response.getStatusCode());
        }
    }

    private void setupDefaultMocks() {
        // Health check endpoint
        addMockExpectation(
                request()
                        .withMethod("GET")
                        .withPath("/health"),
                response()
                        .withStatusCode(200)
                        .withBody("{\"status\":\"ok\"}")
        );
    }

    public void clearAndResetMocks() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.reset();
            setupDefaultMocks();
            LOGGER.debug("All mock expectations cleared and defaults restored");
        }
    }

    // ========== TOP UP MOCKS ==========

    public void setupTopUpMocks() {
        System.out.println("Setting up TopUp mocks...");

        // Simplified approach - handle most common cases first

        // 1. SUCCESS CASE - Valid request with Authorization (catch-all for valid requests)
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"message\": \"Account Successfully Topped Up, New Balance is 1500\"}")
        );

        // Handle endpoint without leading slash - SUCCESS
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"message\": \"Account Successfully Topped Up, New Balance is 1500\"}")
        );

        System.out.println("TopUp success mocks setup completed");
    }

    // Separate method for error scenarios
    public void setupTopUpValidationMocks() {
        System.out.println("Setting up TopUp validation mocks...");

        // Clear existing mocks first and re-setup essentials
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.reset();
            setupDefaultMocks();
            setupAuthMocks(); // Re-setup auth mocks
        }

        // Simple approach - use exact body matching instead of regex

        // 1. Exact match for "x" amount
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{\"amount\":\"x\"}")),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Non-numeric amount\"}")
        );

        // 2. Alternative format with spaces
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{ \"amount\" : \"x\" }")),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Non-numeric amount\"}")
        );

        // 3. Handle without leading slash - same patterns
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{\"amount\":\"x\"}")),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Non-numeric amount\"}")
        );

        // 4. Other common invalid values
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{\"amount\":\"abc\"}")),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Non-numeric amount\"}")
        );

        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{\"amount\":\"0\"}")), // Zero as string
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Amount cannot be zero\"}")
        );

        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*")
                        .withBody(exact("{\"amount\":0}")), // Zero as number
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request - Amount cannot be zero\"}")
        );

        // 5. SUCCESS - Fallback for any other requests with valid authorization
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"message\": \"Account Successfully Topped Up, New Balance is 1500\"}")
        );

        // Handle without leading slash - success fallback
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"message\": \"Account Successfully Topped Up, New Balance is 1500\"}")
        );

        System.out.println("TopUp validation mocks setup completed");
        System.out.println("DEBUG: Using exact body matching for reliability");
    }

    public void setupTopUpErrorMocks() {
        System.out.println("Setting up TopUp error mocks...");

        // 2. UNAUTHORIZED CASE - Missing or invalid Authorization header
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                // No Authorization header or invalid token
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        // Handle endpoint without leading slash - UNAUTHORIZED
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        System.out.println("TopUp error mocks setup completed");
    }

    // Method to setup specific test scenarios
    public void setupTopUpTestScenario(String scenario) {
        System.out.println("Setting up TopUp scenario: " + scenario);

        switch (scenario.toLowerCase()) {
            case "success":
                setupTopUpMocks();
                break;
            case "unauthorized":
                setupTopUpUnauthorizedMocks();
                break;
            case "validation":
            case "badrequest":
            case "invalid":
                // For now, just use the simple success mocks to avoid hanging
                setupTopUpMocks();
                break;
            default:
                setupTopUpMocks(); // Default to success
        }
    }

    public void setupTopUpUnauthorizedMocks() {
        System.out.println("Setting up TopUp unauthorized mocks...");

        // Clear existing mocks first and re-setup essentials
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.reset();
            setupDefaultMocks();
            setupAuthMocks(); // Re-setup auth mocks
        }

        // 1. No Authorization header at all
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                // No Authorization header specified
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        // Handle without leading slash
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        // 2. Invalid Authorization header
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "Bearer invalid-token"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        // 3. Malformed Authorization header
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Authorization", "InvalidFormat"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Unauthorized\"}")
        );

        System.out.println("TopUp unauthorized mocks setup completed");
    }

    private void setupTopUpBadRequestMocks() {
        // Override with bad request response for any TopUp request
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp"),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request\"}")
        );

        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp"),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{\"error\": \"Bad Request\"}")
        );
    }

    // ========== AUTH MOCKS ==========

    public void setupAuthMocks() {
        System.out.println("Setting up auth mocks...");

        // Specific login with exact credentials
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/Auth/login")
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"email\":\"mohaidatahmed@gmail.com\",\"password\":\"Pass123@\"}"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"token\":\"eyJhbGciOiJIUzI1NiJ9.mock-token\","
                                + "\"status\":\"success\""
                                + "}")
        );

        // General login mock
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/Auth/login")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"token\":\"eyJhbGciOiJIUzI1NiJ9.mock-token\","
                                + "\"status\":\"success\""
                                + "}")
        );

        // Handle without leading slash
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/Auth/login")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"token\":\"eyJhbGciOiJIUzI1NiJ9.mock-token\","
                                + "\"status\":\"success\""
                                + "}")
        );

        System.out.println("Auth mocks setup completed");
    }

    public void setupAuthErrorMocks() {
        // Failed login
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/auth/login")
                        .withBody("{\"username\":\"invalid\",\"password\":\"wrong\"}"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"INVALID_CREDENTIALS\","
                                + "\"message\":\"Invalid username or password\","
                                + "\"code\":\"AUTH_001\""
                                + "}")
        );

        // Unauthorized access
        addMockExpectation(
                request()
                        .withMethod("GET")
                        .withPath("/api/auth/user"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"Unauthorized\","
                                + "\"message\":\"Authentication required\","
                                + "\"code\":\"AUTH_REQUIRED\""
                                + "}")
        );

        // Invalid token
        addMockExpectation(
                request()
                        .withMethod("GET")
                        .withPath("/api/auth/user")
                        .withHeader("Authorization", "Bearer invalid-token"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"INVALID_TOKEN\","
                                + "\"message\":\"Token is invalid or malformed\","
                                + "\"code\":\"AUTH_003\""
                                + "}")
        );
    }

    // ========== UTILITY METHODS ==========

    public boolean wasEndpointCalled(String method, String path) {
        if (mockServer != null && mockServer.isRunning()) {
            return mockServer.retrieveRecordedRequests(
                    request()
                            .withMethod(method)
                            .withPath(path)
            ).length > 0;
        }
        return false;
    }

    public void printRecordedRequests() {
        if (mockServer != null && mockServer.isRunning()) {
            var requests = mockServer.retrieveRecordedRequests(null);
            LOGGER.info("Recorded requests count: {}", requests.length);
            System.out.println("=== MOCK SERVER DEBUG ===");
            System.out.println("Total recorded requests: " + requests.length);
            for (var req : requests) {
                System.out.println("Method: " + req.getMethod());
                System.out.println("Path: " + req.getPath());
                System.out.println("Headers: " + req.getHeaders());
                System.out.println("Body: " + req.getBodyAsString());
                System.out.println("------------------------");
                LOGGER.info("Request: {} {} - Body: {}",
                        req.getMethod(), req.getPath(), req.getBodyAsString());
            }
            System.out.println("=========================");
        }
    }

    private void configureMockServerUrl() {
        String mockServerUrl = "http://localhost:" + mockServerPort;
        testConfig.put("mockUrl", mockServerUrl);

        if ("mock".equalsIgnoreCase(thirdParty)) {
            baseUrl = mockServerUrl;
            configureRestAssured();
            LOGGER.info("Framework configured to use mock server: {}", mockServerUrl);
        }
    }

    private void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = mockServerPort;
    }

    // ========== GETTERS ==========

    public ClientAndServer getMockServer() {
        return mockServer;
    }

    public int getMockServerPort() {
        return mockServerPort;
    }

    public boolean isMockServerRunning() {
        return mockServer != null && mockServer.isRunning();
    }

    public String getMockServerUrl() {
        return "http://localhost:" + mockServerPort;
    }
}