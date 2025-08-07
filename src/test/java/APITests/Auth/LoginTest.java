package APITests.Auth;

import APISchema.Auth.LoginAPI;
import base.APITestBase.ApiTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends ApiTestBase {
    LoginAPI loginAPI;

    @Test(description = "Generate access token successfully")
    public void generateToken(){
        loginAPI = new LoginAPI();
        loginAPI.submitRequest();
        String token = loginAPI.submitRequest();
        Assert.assertTrue(token != null);
    }

    @Test(description = "Invalid credentials")
    public void invalidCred() {
        loginAPI = new LoginAPI();
        Map result = loginAPI.submitRequest("test@test.com");
        int statusCode = (int) result.get("statusCode");
        String response = (String) result.get("res");
        Assert.assertEquals(statusCode, 401);
        Assert.assertTrue(response != null);
    }

}
