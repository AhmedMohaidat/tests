package APISchema.TopUp;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class AddFundsAPI extends ApiTestBase {

    private void setFundsData() {
        LoginAPI loginAPI = new LoginAPI();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/TopUp/TopUp.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");

        // Get token from LoginAPI (works with both real and mock)
        String token = loginAPI.submitRequest();
        auth = "Bearer " + token;

        Map body = (Map) data.get("body");
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

        // Debug logging
        LOGGER.info("Using baseUrl: {}", baseUrl);
        LOGGER.info("Endpoint: {}", endPoint);
        LOGGER.info("Token: {}", token);
    }

    public void addFunds() {
        setFundsData();
        Response response = sendRequest(baseUrl, "POST", requestInfo);

        // Enhanced logging
        LOGGER.info("TopUp Response Status: {}", response.statusCode());
        LOGGER.info("TopUp Response Body: {}", response.asPrettyString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response: " + response.asPrettyString());
    }
}