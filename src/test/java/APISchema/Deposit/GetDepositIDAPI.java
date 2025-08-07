package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class GetDepositIDAPI extends ApiTestBase {

    LoginAPI loginAPI;

    private void setDepositIdData() {
        loginAPI = new LoginAPI();
        int id = 1;

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/Deposit/GetAllDeposit.json");
        endPoint = (String) data.get("endPoint") + "/" +id;
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " + loginAPI.submitRequest();

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);

    }

    public void getDepositsByID() {
        setDepositIdData();
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
