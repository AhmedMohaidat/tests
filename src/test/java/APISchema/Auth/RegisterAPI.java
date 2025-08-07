package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterAPI extends ApiTestBase {

    Map data;
    Map body;
    String res = null;

    public RegisterAPI() {
        data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/RegisterData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        body = (Map) data.get("body");
        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
    }

    private void setRegisterData() {
        body.put("email", faker.internet().emailAddress());
        body.put("firstName", faker.name().firstName());
        body.put("lastName", faker.name().lastName());
        body.put("role", faker.number().numberBetween(0, 2));
        requestBody = returnValueAsString(body);
        requestInfo.put("requestBody", requestBody);

    }

    private void setRegisterData(String email) {
        body.put("email", email);
        body.put("firstName", faker.name().firstName());
        body.put("lastName", faker.name().lastName());
        body.put("role", faker.number().numberBetween(0, 2));
        requestBody = returnValueAsString(body);
        requestInfo.put("requestBody", requestBody);
    }

    private Map getResponse() {
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        int statusCode = response.statusCode();

        Map result = new HashMap();
        result.put("statusCode", statusCode);

        try {
            res = response.asPrettyString();
            System.out.println(res);
            result.put("res", res);

            JsonPath jsonPath = new JsonPath(res);
            String token = jsonPath.getString("token");
            if (token != null) {
                result.put("token", token);
            }

        } catch (Exception exception) {
            result.put("res", response.asPrettyString());
        }

        return result;
    }

    public Map submitRequest() {
        setRegisterData();
        return getResponse();
    }

    public Map submitRequest(String email) {
        setRegisterData(email);
        return getResponse();
    }


}
