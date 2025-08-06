package base.APITestBase;


import base.TestDataBase.TestDataSetBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import helpers.DateHelper;
import helpers.EmailHelper;
import helpers.ReadWriteHelper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import report.ExtentManager;

import java.io.File;
import java.util.*;

//@Listeners({WebTestListener.class})
public class ApiTestBase extends TestDataSetBase {

    RequestSpecification httpRequest;
    protected String endPoint;
    protected Map headers;
    protected String accept;
    protected String contentType;
    protected String auth;
    protected String requestBody = "";
    public String username;
    public String password;
    public String otp;
    protected Faker faker = new Faker();
    public Map requestInfo = new HashMap();

    protected static ThreadLocal<String> tokenSafeThread = new ThreadLocal<>();
    protected static ThreadLocal<String> endPointSafeThread = new ThreadLocal<>();
    protected static ThreadLocal<String> contentTypeSafeThread = new ThreadLocal<>();
    protected static ThreadLocal<String> acceptLanguageSafeThread = new ThreadLocal<>();
    protected static ThreadLocal<String> acceptSafeThread = new ThreadLocal<>();
    protected static ThreadLocal<String> refererSafeThread = new ThreadLocal<>();
    protected static Map<String, String> url = new Hashtable<>();


    public void setMultipartRequest(String baseURI, String endPoint, String token,
                                    String headerKey1, String headerValue1,
                                    String headerKey2, String headerValue2,
                                    String headerKey3, String headerValue3) {
        RestAssured.baseURI = baseURI;
        RestAssured.basePath = endPoint;
        Header acceptHeader1 = new Header(headerKey1, headerValue1);
        Header acceptHeader2 = new Header(headerKey2, headerValue2);
        Header acceptHeader3 = new Header(headerKey3, headerValue3);
        httpRequest = RestAssured.
                given().
                header("token", token).
                header(acceptHeader1).
                header(acceptHeader2).
                header(acceptHeader3);
    }

    public Response uploadFileRequest(String baseUrl, Map reqData, String filePath) {
        File file = new File(filePath);
        Response response = requestHeaders(baseUrl, reqData)
                .multiPart("attachments", file)
                .when()
                .post((String) reqData.get("endPoint"))
                .andReturn();
        return response;
    }


    public Response sendRequest(String baseUrl, String reqFunction, Map reqData) {
        Response response;
        ApiRequest apiRequest = null;
        if (reqFunction.equalsIgnoreCase(RequestFunction.POST.key)) {
            apiRequest = new PostRequest();
        } else if (reqFunction.equalsIgnoreCase(RequestFunction.GET.key)) {
            apiRequest = new GetRequest();
        } else if (reqFunction.equalsIgnoreCase(RequestFunction.PUT.key)) {
            apiRequest = new PutRequest();
        } else if (reqFunction.equalsIgnoreCase(RequestFunction.DEL.key)) {
            apiRequest = new DelRequest();
        }
        response = apiRequest.sendRequest(baseUrl, reqData);
        return response;
    }


    public Response sendRequest(String baseUrl, Map reqData, Map queryParam) {
        ApiRequest apiRequest = new GetRequest();
        return apiRequest.sendRequest(baseUrl, reqData, queryParam);
    }

    protected RequestSpecification requestHeaders(String baseUrl, Map reqData) {
        RequestSpecification reqSpec = RestAssured.given().relaxedHTTPSValidation().baseUri(baseUrl);
        Iterator keys = reqData.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equalsIgnoreCase("requestBody") || key.equalsIgnoreCase("endPoint")) {
                continue;
            }
            Object value = reqData.get(key);
            reqSpec.header(key, value);
        }

        return reqSpec;
    }

    protected Map getApiSchemaData(String jsonFilePath) {
        Map schemaData = new Hashtable();
        Map data = ReadWriteHelper.getDataFromJson(jsonFilePath);
        endPoint = (String) data.get("endPoint");
        schemaData.put("endPoint", endPoint);

        Map headers = (Map) data.get("headers");
        Iterator keys = headers.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = headers.get(key);
            schemaData.put(key, value);
        }

        Object reqBody = data.get("body");
        schemaData.put("body", reqBody);

        return schemaData;
    }


    protected String returnValueAsString(Map object) {
        String value = null;
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {
            value = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

    protected String returnValueAsString(List object) {
        String value = null;
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {
            value = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return value;
    }


    @Parameters({"service"})
    @AfterSuite(enabled = true)
    public void sendEmailNotification(@Optional("optional") String service) {
        DateHelper date = new DateHelper();


        List<String> recipients = (List<String>) testConfig.get("recipients");


        // send notification email
        if ((Boolean) testConfig.get("sendEmail")) {
            String recipientList = "";
            for (int i = 0; i < recipients.size(); i++) {
                recipientList += "," + recipients.get(i);
            }
            if (service.equalsIgnoreCase("optional")) {
                EmailHelper.sendEmail(recipientList, ExtentManager.getReportPath(), testConfig.get("AUT") + " Service ");

            } else {
                EmailHelper.sendEmail(recipientList, ExtentManager.getReportPath(), service + " " + "Service");

            }
        }
    }
}
