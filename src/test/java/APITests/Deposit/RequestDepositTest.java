package APITests.Deposit;

import APISchema.Deposit.RequestDepositAPI;
import org.testng.annotations.Test;

public class RequestDepositTest {

    RequestDepositAPI requestDepositAPI;

    @Test(description = "Request a custom deposit successfully")
    public void requestDeposit() {
        requestDepositAPI = new RequestDepositAPI();
        requestDepositAPI.createCustomRequest();
    }
}
