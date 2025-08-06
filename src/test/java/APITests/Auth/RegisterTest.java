package APITests.Auth;

import APISchema.Auth.RegisterAPI;
import org.testng.annotations.Test;

public class RegisterTest {

    RegisterAPI registerAPI;

    @Test(description = "Register new user Successfully")
    public void registerUser() {
        registerAPI = new RegisterAPI();
        registerAPI.submitRequest();
    }
}
