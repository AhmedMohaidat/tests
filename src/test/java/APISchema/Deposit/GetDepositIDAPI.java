package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class GetDepositIDAPI extends ApiTestBase {


    private void setDepositIdData(String token, int id ) {
        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/GetAllDeposit.json");
        endPoint = (String) data.get("endPoint") + "/" +id;
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " + token;

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);

    }

    public Map getDepositsByID(String token, int id) {
        setDepositIdData(token, id);
        Response response = sendRequest(baseUrl, "GET", requestInfo);

        Map result = new HashMap<>();
        try {
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("responseBody", response.asPrettyString());
        } catch (Exception exception){

        }
        return result;
    }
}
