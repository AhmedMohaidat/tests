package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class RejectDepositAPI extends ApiTestBase {

    /* I should be able to fetch data from the DB to get the available IDs
    and use them here, but I don't have access.
     */

    private void setRejectOfferData(String token, int offerId) {

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/RejectDeposit.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + token;

        Map body = (Map) data.get("body");
        body.put("depositId", offerId);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public Map submitRequest(String token, int offerId) {
        setRejectOfferData(token, offerId);
        Response response = sendRequest(baseUrl, "PUT", requestInfo);
        Map result = new HashMap();
        try{
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("responseBody", response.asPrettyString());
        }catch (Exception exception){
        }

        return result;
    }
}
