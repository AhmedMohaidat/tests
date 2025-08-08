package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class GetAllDepositsAPI extends ApiTestBase {

    private void setAllDepositsData(String token) {
        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/GetAllDeposit.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " + token;

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);

    }

    public Map getAllDeposits(String token) {
        setAllDepositsData(token);
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        Map result = new HashMap();
        try {
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("response", jsonPath.getList("response"));
        } catch (Exception exception) {
        }
        return result;
    }

    public Map getFirstDepositId(String token) {
        setAllDepositsData(token);
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        JsonPath jsonPath = new JsonPath(response.asPrettyString());
        Map result = new HashMap();
        try {
            result.put("statusCode", response.statusCode());
            result.put("endPoint", endPoint);
            result.put("depositId", jsonPath.getInt("[0].id"));
        } catch (Exception exception) {
        }
        return result;
    }
}
