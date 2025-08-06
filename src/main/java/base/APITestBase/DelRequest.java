package base.APITestBase;

import io.restassured.response.Response;

import java.util.Map;

public class DelRequest extends ApiTestBase implements ApiRequest{
    @Override
    public Response sendRequest(String baseUrl, Map reqData) {
        Response response = requestHeaders(baseUrl, reqData)
                .body(reqData.get("requestBody"))
                .delete((String) reqData.get("endPoint"));
        return response;
    }

    @Override
    public Response sendRequest(String baseUrl, Map reqData, Map queryParams) {
        return null;
    }
}
