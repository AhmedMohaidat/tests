package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class ApproveDepositAPI extends ApiTestBase {

    /* I should be able to fetch data from the DB to get the available IDs
    and use them here, but I don't have access.
     */

    LoginAPI loginAPI;
    DateHelper dateHelper;

    private void setApprovedOfferData() {
        loginAPI = new LoginAPI();
        dateHelper = new DateHelper();
        String startDate = dateHelper.generateFutureISODate();
        String maturityDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/ApproveDeposit.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("depositId", 1);
        body.put("startDate", startDate);
        body.put("maturityDate", maturityDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void submitRequest() {
        setApprovedOfferData();
        Response response = sendRequest(baseUrl, "PUT", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
