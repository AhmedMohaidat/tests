package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class GetAllDepositsAPI extends ApiTestBase {

    private void setAllDepositsData() {
        LoginAPI loginAPI = new LoginAPI();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/GetAllDeposit.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " + loginAPI.submitRequest();

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);

    }

    public void getAllDeposits() {
        setAllDepositsData();
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
