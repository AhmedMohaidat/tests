package base.MockServer;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import base.TestDataBase.TestDataSetBase;
import io.restassured.RestAssured;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

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

    /**
     * Add a custom mock expectation
     */
    public void addMockExpectation(HttpRequest request, HttpResponse response) {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.when(request).respond(response);
            LOGGER.debug("Added mock expectation: {} -> {}",
                    request.getPath(), response.getStatusCode());
        }
    }

    /**
     * Setup default mocks that are common across all tests
     */
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

    /**
     * Clear all mock expectations and setup fresh defaults
     */
    public void clearAndResetMocks() {
        if (mockServer != null && mockServer.isRunning()) {
            mockServer.reset();
            setupDefaultMocks();
            LOGGER.debug("All mock expectations cleared and defaults restored");
        }
    }

    // ========== TOP UP MOCKS ==========

    public void setupTopUpMocks() {
        // AddFunds - Success response (matching your TopUp.json: "api/TopUp")
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"transactionId\":\"TXN_" + System.currentTimeMillis() + "\","
                                + "\"amount\":500,"
                                + "\"previousBalance\":1000,"
                                + "\"newBalance\":1500,"
                                + "\"status\":\"SUCCESS\","
                                + "\"timestamp\":\"" + java.time.Instant.now().toString() + "\","
                                + "\"message\":\"Funds added successfully\""
                                + "}")
        );

        // Handle endpoint without leading slash (in case your framework sends it differently)
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("api/TopUp")
                        .withHeader("Content-Type", "application/json"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"transactionId\":\"TXN_" + System.currentTimeMillis() + "\","
                                + "\"amount\":500,"
                                + "\"status\":\"SUCCESS\","
                                + "\"message\":\"Funds added successfully\""
                                + "}")
        );

        // Get TopUp balance
        addMockExpectation(
                request()
                        .withMethod("GET")
                        .withPath("/api/TopUp/balance")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"currentBalance\":1500,"
                                + "\"currency\":\"USD\","
                                + "\"availableBalance\":1450,"
                                + "\"pendingAmount\":50"
                                + "}")
        );

        // Get TopUp history
        addMockExpectation(
                request()
                        .withMethod("GET")
                        .withPath("/api/TopUp/history")
                        .withHeader("Authorization", "Bearer .*"),
                response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"topUps\":["
                                + "{"
                                + "\"id\":\"TOP_001\","
                                + "\"amount\":200,"
                                + "\"timestamp\":\"2024-01-15T10:30:00Z\","
                                + "\"status\":\"COMPLETED\","
                                + "\"method\":\"CREDIT_CARD\""
                                + "},"
                                + "{"
                                + "\"id\":\"TOP_002\","
                                + "\"amount\":300,"
                                + "\"timestamp\":\"2024-01-14T15:45:00Z\","
                                + "\"status\":\"COMPLETED\","
                                + "\"method\":\"BANK_TRANSFER\""
                                + "}"
                                + "],"
                                + "\"totalCount\":2,"
                                + "\"totalAmount\":500"
                                + "}")
        );
    }

    public void setupTopUpErrorMocks() {
        // Invalid amount (0 or negative)
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withBody(".*\"amount\"\\s*:\\s*(0|-\\d+).*"),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"INVALID_AMOUNT\","
                                + "\"message\":\"Amount must be greater than 0\","
                                + "\"code\":\"TOPUP_002\","
                                + "\"minAmount\":1,"
                                + "\"maxAmount\":10000"
                                + "}")
        );

        // Large amount - insufficient balance
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")
                        .withBody(".*\"amount\"\\s*:\\s*(10000|9999).*"),
                response()
                        .withStatusCode(400)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"INSUFFICIENT_BALANCE\","
                                + "\"message\":\"Insufficient funds for this transaction\","
                                + "\"currentBalance\":100,"
                                + "\"requestedAmount\":10000,"
                                + "\"code\":\"TOPUP_001\""
                                + "}")
        );

        // Unauthorized access (missing or invalid token)
        addMockExpectation(
                request()
                        .withMethod("POST")
                        .withPath("/api/TopUp"),
                response()
                        .withStatusCode(401)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"error\":\"Unauthorized\","
                                + "\"message\":\"Authentication required\","
                                + "\"code\":\"AUTH_REQUIRED\""
                                + "}")
        );
    }

    // ========== AUTH MOCKS ==========
    public void setupAuthMocks() {
        System.out.println("Setting up auth mocks...");


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

        // Also add a broader mock without body matching
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

    /**
     * Verify if a specific endpoint was called
     */
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

    /**
     * Get all recorded requests for debugging
     */
    public void printRecordedRequests() {
        if (mockServer != null && mockServer.isRunning()) {
            var requests = mockServer.retrieveRecordedRequests(null);
            LOGGER.info("Recorded requests count: {}", requests.length);
            for (var req : requests) {
                LOGGER.info("Request: {} {} - Body: {}",
                        req.getMethod(), req.getPath(), req.getBodyAsString());
            }
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
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
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