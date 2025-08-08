package APITests.Deposit;

import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GetAllDepositsTest extends Precondition {

    List deposits;

    @Test(description = "Get All Deposits Successfully")
    public void getAllDeposits() {
        Map result = getDeposits.getAllDeposits(token);
        int statusCode = (int) result.get("statusCode");
        deposits = (List) result.get("response");

        verifyResult(String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                "Deposits List size is " + deposits.size(),
                "Response body for '" + result.get("endPoint") + "' shouldn't be empty",
                deposits.size() > 0
        );
    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        Map result = getDeposits.getAllDeposits(null);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );

    }

}
