package APITests.Auth;

import APISchema.Auth.LoginAPI;
import APISchema.Auth.RegisterAPI;
import base.APITestBase.ApiTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class RegisterTest extends ApiTestBase {

    RegisterAPI registerAPI;

    @Test(description = "Register new user Successfully")
    public void registerUser() {
        registerAPI = new RegisterAPI();
        Map result = registerAPI.submitRequest();
        int statusCode = (int) result.get("statusCode");
        String token = (String) result.get("token");

        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(token != null);
    }

    @Test(description = "User Already Existed")
    public void existedUser() {
        registerAPI = new RegisterAPI();
        Map result = registerAPI.submitRequest("mohaidatahmed@gmail.com");
        int statusCode = (int) result.get("statusCode");
        String res = (String) result.get("res");

        Assert.assertEquals(statusCode, 400);
        Assert.assertTrue(res.contains("User with this email already exists."),
                "Response should contain the expected error message");
    }

    @Test(description = "Invalid registration")
    public void invalidData() {
        registerAPI = new RegisterAPI();
        Map result = registerAPI.submitRequest("mohaidatahmed");
        int statusCode = (int) result.get("statusCode");
        String res = (String) result.get("res");

        Assert.assertEquals(statusCode, 400);
        Assert.assertTrue(res.contains("Invalid email format."),
                "Response should contain the expected error message");
    }

}
