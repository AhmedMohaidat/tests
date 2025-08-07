package APITests.Deposit;

import APISchema.Deposit.GetDepositIDAPI;
import org.testng.annotations.Test;

public class GetDepositIDTest {

    GetDepositIDAPI depositId;

    @Test(description = "Get Deposit By ID Successfully")
    public void getDepositByID() {
        depositId = new GetDepositIDAPI();
        depositId.getDepositsByID();
    }
}
