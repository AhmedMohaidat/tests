package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CrowdEnrollAPI extends ApiTestBase {


    DateHelper dateHelper;

    private void setCrowdEnroll(String token, int offerId) {

        dateHelper = new DateHelper();
        String futureDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/CrowdEnroll.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + token;

        Map body = (Map) data.get("body");
        body.put("offerId", offerId);
        body.put("rolloverUntilDate", futureDate);
        System.out.println("Date ==> " + futureDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public Map submitRequest(String token, int offerId) {
        setCrowdEnroll(token, offerId);
        Response response = sendRequest(baseUrl, "POST", requestInfo);
        Map result = new HashMap();
        try {
            result.put("endPoint", endPoint);
            result.put("statusCode", response.statusCode());
            result.put("responseBody", response.asPrettyString());
        } catch (Exception exception) {
        }
        return result;
    }
}
