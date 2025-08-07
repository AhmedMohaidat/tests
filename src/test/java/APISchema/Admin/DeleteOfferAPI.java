package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class DeleteOfferAPI extends ApiTestBase {

     /* I should be able to fetch data from the DB to get the available IDs
    and use them here, but I don't have access.
     */

    LoginAPI loginAPI;

    private void setDeleteOfferData() {
        loginAPI = new LoginAPI();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/DeleteOffer.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + loginAPI.submitRequest();

        Map body = (Map) data.get("body");
        body.put("id", 4);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public void deleteOffer() {
        setDeleteOfferData();
        Response response = sendRequest(baseUrl, "DEL", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
