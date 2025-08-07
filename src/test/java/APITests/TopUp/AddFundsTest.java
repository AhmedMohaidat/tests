package APITests.TopUp;

import APISchema.TopUp.AddFundsAPI;
import base.APITestBase.ApiTestBase;
import org.testng.annotations.Test;

public class AddFundsTest extends ApiTestBase {

    AddFundsAPI addFunds;
    @Test(description = "Add funds to the authenticated user's wallet Successfully")
    public void addFundsBalance() {
        addFunds = new AddFundsAPI();
        addFunds.addFunds();
    }

    @Test(description = "Invalid top-up request or amount")
    public void invalidData() {
        addFunds = new AddFundsAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        addFunds = new AddFundsAPI();

    }
}