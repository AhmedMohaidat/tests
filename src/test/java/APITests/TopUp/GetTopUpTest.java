package APITests.TopUp;

import APISchema.Deposit.GetTopUpAPI;
import org.testng.annotations.Test;

public class GetTopUpTest {

    GetTopUpAPI topUp;

    @Test(description = "Get TopUps details successfully")
    public void getTopUps() {
        topUp = new GetTopUpAPI();
        topUp.getTopUp();
    }
}
