package APITests.Deposit;

import APISchema.Deposit.AvailableDepositAPI;
import org.testng.annotations.Test;

public class AvailableDepositTest {

    AvailableDepositAPI available;

    @Test(description = "Get Available Deposits Successfully")
    public void getAvailableDeposits() {
        available = new AvailableDepositAPI();
        available.getAvailableDeposit();
    }
}
