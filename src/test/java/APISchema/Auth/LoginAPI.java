package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

public class LoginAPI extends ApiTestBase {


    private void setLoginData(){

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/loginData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");

        Map body = (Map) data.get("body");
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("requestBody", requestBody);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);

    }


    public String submitRequest(){
        setLoginData();

        // DEBUG: Print what we're about to send
        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("baseUrl: " + baseUrl);
        System.out.println("endPoint: " + endPoint);
        System.out.println("Full URL will be constructed by ApiTestBase");
        System.out.println("requestBody: " + requestBody);
        System.out.println("==================");

        Response response = sendRequest(
                baseUrl,
                "POST",
                requestInfo
        );

        System.out.println("=== LOGIN RESPONSE ===");
        System.out.println("Login Response Status: " + response.statusCode());
        System.out.println("Login Response Body: " + response.asPrettyString());
        System.out.println("Response Headers: " + response.headers());
        System.out.println("=====================");

        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        String token = jsonPath.getString("token");
        System.out.println("Extracted token: " + token);

        return token;
    }
}
