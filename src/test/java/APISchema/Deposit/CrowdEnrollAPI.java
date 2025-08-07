package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class CrowdEnrollAPI extends ApiTestBase {

    LoginAPI loginAPI;
    DateHelper dateHelper;

    private void setCrowdEnroll() {
        loginAPI = new LoginAPI();
        dateHelper = new DateHelper();
        String futureDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/CrowdEnroll.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("rolloverUntilDate", futureDate);
        System.out.println("Date ==> " + futureDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void submitRequest() {
        setCrowdEnroll();
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());

    }
}
