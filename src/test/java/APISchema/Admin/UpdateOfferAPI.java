package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class UpdateOfferAPI extends ApiTestBase {

    LoginAPI loginAPI;
    DateHelper dateHelper;

    private void setUpdateOfferData() {
        loginAPI = new LoginAPI();
        dateHelper = new DateHelper();
        String startDate = dateHelper.generateFutureISODate();
        String maturityDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/UpdateOffer.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("startDate", startDate);
        body.put("maturityDate", maturityDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void updateOffer() {
        setUpdateOfferData();
        Response response = sendRequest(baseUrl, "PUT", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
