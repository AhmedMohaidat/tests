package APITests.Auth;

import APISchema.Auth.UpdateUserAPI;
import org.testng.annotations.Test;

public class UpdateUserTest {

    UpdateUserAPI updateAPI;

    @Test(description = "Update User Successfully")
    public void updateUser() {
        updateAPI = new UpdateUserAPI();
        updateAPI.submitUpdateRequest();
    }
}
