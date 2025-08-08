package APITests.Deposit;

import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class AvailableDepositTest extends Precondition {
    List offers;

    @Test(description = "Get Available Deposits Successfully")
    public void getAvailableDeposits() {
        Map result = available.getAvailableDeposit(token);

        int statusCode = (int) result.get("statusCode");
        offers = (List) result.get("offersList");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                "Offers list size is " + offers.size(),
                "Response body for '" + result.get("endPoint") + "' shouldn't be empty",
               offers.size() > 0
        );
    }
}
