package APISchema.Admin;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.DateHelper;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CreateOfferAPI extends ApiTestBase {

    DateHelper dateHelper = new DateHelper();

    private void setCrowdOfferData(
            String token,
            Map offerData
    ) {
        String startDate = dateHelper.generateFutureISODate();
        String maturityDate = dateHelper.generateFutureISODate();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Admin/CreateOffer.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        contentType = (String) headers.get("Content-Type");
        auth = "Bearer " + token;

        Map body = (Map) data.get("body");
        body.put("amount", offerData.get("amount"));
        body.put("period", offerData.get("period"));
        body.put("startDate", startDate);
        body.put("maturityDate", maturityDate);
        requestBody = returnValueAsString(body);

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Content-Type", contentType);
        requestInfo.put("Authorization", auth);
        requestInfo.put("requestBody", requestBody);

    }

    public Map submitOffer(
            String token,
            Map offerData
    ) {
        setCrowdOfferData(
                token,
                offerData
        );
        Response response = sendRequest(baseUrl, "POST", requestInfo);

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
