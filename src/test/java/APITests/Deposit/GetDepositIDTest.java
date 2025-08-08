package APITests.Deposit;

import APISchema.Deposit.GetDepositIDAPI;
import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GetDepositIDTest extends Precondition {

    String depositInfo;
    @Test(description = "Get Deposit By ID Successfully")
    public void getDepositByID() {
        setDepositID();
        Map result = depositId.getDepositsByID(token, depositID);
        int statusCode = (int) result.get("statusCode");
        depositInfo = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                depositInfo,
                "Response body for '" + result.get("endPoint"),
                depositInfo != null
        );

    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        setDepositID();
        Map result = depositId.getDepositsByID(null, depositID);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "Deposit not found")
    public void notFoundDeposit() {
        setDepositID();
        Map result = depositId.getDepositsByID(token, 0);
        int statusCode = (int) result.get("statusCode");
        depositInfo = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );
        verifyResult(
                depositInfo,
                "Response body for '" + result.get("endPoint") + "' Sequence contains no elements.'",
                depositInfo != null
        );
    }
}
