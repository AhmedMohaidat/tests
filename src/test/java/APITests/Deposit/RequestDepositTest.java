package APITests.Deposit;

import APISchema.Deposit.RequestDepositAPI;
import org.testng.annotations.Test;

public class RequestDepositTest {

    RequestDepositAPI requestDepositAPI;

    @Test(description = "Create custom deposit request successfully")
    public void requestDeposit() {
        requestDepositAPI = new RequestDepositAPI();
        requestDepositAPI.createCustomRequest();
    }

    @Test(description = "Invalid request data")
    public void invalidData() {

    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {

    }

}
