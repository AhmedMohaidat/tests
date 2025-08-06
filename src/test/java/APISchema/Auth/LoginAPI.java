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
        Response response = sendRequest(
                baseUrl,
                "POST",
                requestInfo
        );

        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        String token = jsonPath.getString("token");

        System.out.println(token);

        return token;
    }
}
