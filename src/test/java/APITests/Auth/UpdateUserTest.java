package APITests.Auth;

import APISchema.Auth.LoginAPI;
import APISchema.Auth.UpdateUserAPI;
import base.APITestBase.ApiTestBase;
import helpers.ReadWriteHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class UpdateUserTest extends ApiTestBase {

    LoginAPI loginAPI;
    UpdateUserAPI updateAPI;
    List emails, passwords;
    String token;

    @BeforeMethod
    public void setTestData() {
        loginAPI = new LoginAPI();
        token = loginAPI.submitRequest();
        updateAPI = new UpdateUserAPI(token);
        Map testData = ReadWriteHelper.getDataFromJson("src/test/resources/TestData/Auth/UpdateUserTestData.json");
        emails = (List) testData.get("emails");
        passwords = (List) testData.get("passwords");
    }

    @Test(description = "Update User information Successfully")
    public void updateUser() {
        Map result = updateAPI.submitUpdateRequest((String) emails.get(0), (String) passwords.get(0));
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("res");

        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(message.contains("Updated"),
                "Response should contain the expected message");
    }


    @Test(description = "Invalid Update data")
    public void invalidData() {
        Map result = updateAPI.submitUpdateRequest((String) emails.get(1), (String) passwords.get(1));
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("res");

        Assert.assertEquals(statusCode, 400);
        Assert.assertTrue(message.contains("Object reference not set to an instance of an object."),
                "Response should contain the expected error message");

    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        updateAPI = new UpdateUserAPI(null);
        Map result = updateAPI.submitUpdateRequest((String) emails.get(0), (String) passwords.get(0));
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("statusMessage");

        Assert.assertEquals(statusCode, 401);
        Assert.assertTrue(message.contains("Unauthorized"),
                "Response should contain the expected error message");
    }

    @Test(description = "Password must be at least 8 characters long")
    public void passwordFormat() {
        Map result = updateAPI.submitUpdateRequest((String) emails.get(0), (String) passwords.get(1));
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("res");

        Assert.assertEquals(statusCode, 400);
        Assert.assertTrue(message.contains("Password must be at least 8 characters long."),
                "Response should contain the expected error message");
    }

}
