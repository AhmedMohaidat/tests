package APITests.Deposit;

import APISchema.Deposit.CrowdEnrollAPI;
import org.testng.annotations.Test;

public class CrowdEnrollTest {

    CrowdEnrollAPI enrollAPI;

    @Test(description = "Submit Enroll Request Successfully")
    public void submitCrowdEnroll() {
        enrollAPI = new CrowdEnrollAPI();
        enrollAPI.submitRequest();
    }
}
