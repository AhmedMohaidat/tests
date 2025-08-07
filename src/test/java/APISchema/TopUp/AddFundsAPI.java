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
        auth = "Bearer " +  loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void addFunds() {
        setFundsData();
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
