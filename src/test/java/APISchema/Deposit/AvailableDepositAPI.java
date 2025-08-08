package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AvailableDepositAPI extends ApiTestBase {

    private void setAvailableData(String token){
        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/GetAllDeposit.json");
        endPoint = (String) data.get("availableEndPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " + token;

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);

    }

    public Map getAvailableDeposit(String token){
        setAvailableData(token);
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        Map result = new HashMap();
        try{
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("offersList", jsonPath.getList("crowdDepositOffers"));
        }catch (Exception exception){
        }

        return result;
    }

    public Map getFirstOfferId(String token){
        setAvailableData(token);
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        Map result = new HashMap();
        try{
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("offerId", jsonPath.getInt("crowdDepositOffers[0].id"));
        }catch (Exception exception){
        }

        return result;
    }
}
