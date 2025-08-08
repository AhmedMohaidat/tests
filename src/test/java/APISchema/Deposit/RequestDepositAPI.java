package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class RequestDepositAPI extends ApiTestBase {

    private void setRequestDepositData(String token, int duration) {
        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/RequestCustom.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " +  token;

        Map body = (Map) data.get("body");
        body.put("durationInDays", duration);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public Map createCustomRequest(String token, int duration){
        setRequestDepositData(token, duration);
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        Map result = new HashMap<>();
        try {
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("responseBody", response.asPrettyString());
        } catch (Exception exception) {}
        return result;
    }
}
