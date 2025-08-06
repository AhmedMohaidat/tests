package APISchema.Auth;

import base.APITestBase.ApiTestBase;
import base.APITestBase.RequestInfo;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class RegisterAPI extends ApiTestBase {

    private void setRegisterData() {

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Auth/RegisterData.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");

        Map body = (Map) data.get("body");
        body.put("email", faker.internet().emailAddress());
        body.put("firstName", faker.name().firstName());
        body.put("lastName", faker.name().lastName());
        body.put("role", faker.number().numberBetween(0,2));

        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("requestBody", requestBody);

    }

    public void submitRequest() {
        setRegisterData();
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());


    }


}
