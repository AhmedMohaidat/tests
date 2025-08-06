package APITests.Auth;

import APISchema.Auth.LoginAPI;
import org.testng.annotations.Test;

public class LoginTest {
    LoginAPI loginAPI;

    @Test(description = "Generate access token successfully")
    public void generateToken(){
        loginAPI = new LoginAPI();

        loginAPI.submitRequest();

    }
}
