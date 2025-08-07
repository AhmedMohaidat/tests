package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class LoginAPI extends ApiTestBase {
    Map data;
    Map body;
    String token = null;

    public LoginAPI() {
        data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/loginData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        body = (Map) data.get("body");
    }

    private void setLoginData() {
        requestBody = returnValueAsString(body);
        requestInfo.put("requestBody", requestBody);
    }

    private void setLoginData(String email) {
        body.put("email", email);
        requestBody = returnValueAsString(body);
        requestInfo.put("requestBody", requestBody);
    }

    private Map getResponse() {
        Response response = sendRequest(
                baseUrl,
                "POST",
                requestInfo
        );
        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        int statusCode = response.statusCode();
        try {
            token = jsonPath.getString("token");
        } catch (Exception exception) {

        }
        Map result = new HashMap();
        result.put("token", token);
        result.put("statusCode", statusCode);

        return result;
    }

    public String submitRequest() {
        setLoginData();
        return (String) getResponse().get("token");
    }

    public Map submitRequest(String email) {
        setLoginData(email);
        return getResponse();
    }


}
