package APISchema.Deposit;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import io.restassured.response.Response;

import java.util.Map;

public class GetTopUpAPI extends ApiTestBase {

    private void setTopUpDetails() {

        LoginAPI loginAPI = new LoginAPI();

        Map data = ReadWriteHelper.getDataFromJson("src/main/resources/ApiDataSchema/TopUp/TopUp.json");
        endPoint = (String) data.get("endPoint");
        headers = (Map) data.get("headers");
        accept = (String) headers.get("accept");
        auth = "Bearer " +  loginAPI.submitRequest();

        requestInfo.put("endPoint", endPoint);
        requestInfo.put("accept", accept);
        requestInfo.put("Authorization", auth);


    }

    public void getTopUp(){
        setTopUpDetails();
        Response response = sendRequest(baseUrl, "GET", requestInfo);
        System.out.println(response.statusCode());
        System.out.println(response.asPrettyString());
    }
}
