package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class CreateOfferAPI extends ApiTestBase {

    LoginAPI loginAPI;
    DateHelper dateHelper;

    private void setCrowdOfferData() {
        loginAPI = new LoginAPI();
        dateHelper = new DateHelper();
        String startDate = dateHelper.generateFutureISODate();
        String maturityDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/CreateOffer.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("startDate", startDate);
        body.put("maturityDate", maturityDate);
        System.out.println("Date ==> " + startDate);
        System.out.println("Date ==> " + maturityDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void submitOffer() {
        setCrowdOfferData();
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
