package APITests.TopUp;

import base.MockServer.MockServerConfig;
import APISchema.TopUp.AddFundsAPI;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AddFundsTest {

    private static MockServerConfig mockServerConfig;
    private AddFundsAPI addFunds;

    @BeforeClass
    public void setupMockServer() {
        // Start mock server
        mockServerConfig = new MockServerConfig();
        mockServerConfig.startMockServer();

        // Setup mocks using methods in MockServerConfig (NOT separate classes)
        mockServerConfig.setupAuthMocks();
        mockServerConfig.setupTopUpMocks();

        System.out.println("Mock server started on port: " + mockServerConfig.getMockServerPort());
        System.out.println("Mock server URL: " + mockServerConfig.getMockServerUrl());
    }

    @AfterClass
    public void tearDownMockServer() {
        if (mockServerConfig != null) {
            mockServerConfig.stopMockServer();
        }
    }

    @Test(description = "Add Funds to user Successfully")
    public void addFundsBalance() {
        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }

    @Test(description = "Add Funds with insufficient balance")
    public void addFundsInsufficientBalance() {
        // Setup error scenarios using MockServerConfig method
        mockServerConfig.setupTopUpErrorMocks();

        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }

    @Test(description = "Add Funds with different amounts")
    public void addFundsVariousAmounts() {
        // Add custom mock for specific test scenarios (using correct endpoint)
        mockServerConfig.addMockExpectation(
                org.mockserver.model.HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/api/TopUp")  // Changed to match your TopUp.json
                        .withBody(".*\"amount\"\\s*:\\s*1000.*"),
                org.mockserver.model.HttpResponse.response()
                        .withStatusCode(200)
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody("{"
                                + "\"status\":\"SUCCESS\","
                                + "\"amount\":1000,"
                                + "\"newBalance\":2000,"
                                + "\"transactionId\":\"TXN_1000\""
                                + "}")
        );

        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }
}