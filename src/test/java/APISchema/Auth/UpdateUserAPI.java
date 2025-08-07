package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserAPI extends ApiTestBase {

    Map data, body;
    String res;

    public UpdateUserAPI(String token) {
        data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/UpdateUserData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " +  token;
        body = (Map) data.get("body");

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
    }



    private void setUpdateUserData(String email, String password) {
        body.put("email", email);
        body.put("password", password);
        body.put("firstName", faker.name().firstName());
        body.put("lastName", faker.name().lastName());
        requestBody = returnValueAsString(body);

        requestInfo.put("requestBody", requestBody);
    }

    private Map getResponse() {
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        int statusCode = response.statusCode();
        String statusMessage = response.statusLine();


        Map result = new HashMap();
        result.put("statusCode", statusCode);
        result.put("statusMessage", statusMessage);

        try {
            res = response.asPrettyString();
            System.out.println(res);
            result.put("res", res);

        } catch (Exception exception) {
        }

        return result;
    }


    public Map submitUpdateRequest(String email, String password) {
        setUpdateUserData(email, password);
        return getResponse();
    }
}
