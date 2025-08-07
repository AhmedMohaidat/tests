package APITests.TopUp;

import APISchema.TopUp.AddFundsAPI;
import org.testng.annotations.Test;

public class AddFundsTest {

    AddFundsAPI addFunds;

    @Test(description = "Add Funds to user Successfully")
    public void addFundsBalance() {
        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }
}
