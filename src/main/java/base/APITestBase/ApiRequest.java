package base.APITestBase;

import io.restassured.response.Response;

import java.util.Map;

public interface ApiRequest {

    public Response sendRequest(String basUrl, Map reqData);
    public Response sendRequest(String baseUrl, Map reqData, Map queryParams);
}
