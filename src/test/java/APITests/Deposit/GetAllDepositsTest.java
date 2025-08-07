package APITests.Deposit;

import APISchema.Deposit.GetAllDepositsAPI;
import org.testng.annotations.Test;

public class GetAllDepositsTest {

    GetAllDepositsAPI getDeposits;

    @Test(description = "Get All Deposits Successfully")
    public void getAllDeposits() {
        getDeposits = new GetAllDepositsAPI();
        getDeposits.getAllDeposits();
    }
}
