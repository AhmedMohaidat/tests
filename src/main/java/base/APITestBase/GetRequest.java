package base.APITestBase;

import io.restassured.response.Response;

import java.util.Map;

public class GetRequest extends ApiTestBase implements ApiRequest{
    @Override
    public Response sendRequest(String baseUrl, Map reqData) {

        Response response = requestHeaders(baseUrl, reqData)
                .get((String) reqData.get("endPoint"));

        return response;
    }

    @Override
    public Response sendRequest(String baseUrl, Map reqData, Map queryParams) {

        Response response = requestHeaders(baseUrl, reqData)
                .queryParams(queryParams)
                .get((String) reqData.get("endPoint"));

        return response;
    }
}
