package APITests.Deposit;

import APISchema.Deposit.RequestDepositAPI;
import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.Map;

public class RequestDepositTest extends Precondition {

    int duration;
    int statusCode;
    String message;
    Map result;

    @Test(description = "Create custom deposit request successfully")
    public void requestDeposit() {
        duration = faker.number().numberBetween(1, 30);
        result = requestDeposit.createCustomRequest(token, duration);
        statusCode = (int) result.get("statusCode");
        message = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                message,
                "Response message of '" + result.get("endPoint") + "' should include 'Your deposit request has been created'",
                message != null
        );
    }

    @Test(description = "Invalid request data")
    public void invalidData() {
        result = requestDeposit.createCustomRequest(token, 0);
        statusCode = (int) result.get("statusCode");
        message = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );
        verifyResult(
                message,
                "Response message of '" + result.get("endPoint") + "' should include 'Duration In Days' must be greater than '0'.",
                message != null
        );
    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        duration = faker.number().numberBetween(1, 30);
        result = requestDeposit.createCustomRequest(null, duration);
        statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

}
