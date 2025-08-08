package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApproveDepositAPI extends ApiTestBase {

    /* I should be able to fetch data from the DB to get the available IDs
    and use them here, but I don't have access.
     */

    DateHelper dateHelper = new DateHelper();

    private void setApprovedOfferData(String token, int offerId) {
        String startDate = dateHelper.generateFutureISODate();
        String maturityDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/ApproveDeposit.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + token;

        Map body = (Map) data.get("body");
        body.put("depositId", offerId);
        body.put("startDate", startDate);
        body.put("maturityDate", maturityDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public Map submitRequest(String token, int offerId) {
        setApprovedOfferData(token, offerId);
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
