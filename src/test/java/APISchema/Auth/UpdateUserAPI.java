package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class UpdateUserAPI extends ApiTestBase {

    private void setUpdateUserData() {

        LoginAPI loginAPI = new LoginAPI();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/UpdateUserData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " +  loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("password", faker.internet().password(8, 10, true,true,true));
        body.put("firstName", faker.name().firstName());
        body.put("lastName", faker.name().lastName());
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void submitUpdateRequest() {
        setUpdateUserData();
        Response response = sendRequest(baseUrl, "POST", requestInfo);
    }

}
