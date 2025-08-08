package APITests.Deposit;

import APISchema.Deposit.CrowdEnrollAPI;
import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.Map;

public class CrowdEnrollTest extends Precondition {

    Map result;

    @Test(description = "Submit Enroll Request Successfully")
    public void submitCrowdEnroll() {
        setOfferId();
        result = enrollAPI.submitRequest(token, offerId);
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                message,
                "Response message of '" + result.get("endPoint") + "' should include 'Enrolled'",
                message != null
        );
    }

    @Test(description = "Invalid enrollment request")
    public void invalidData() {

        result = enrollAPI.submitRequest(token, 0);
        int statusCode = (int) result.get("statusCode");
        String message = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );
        verifyResult(
                message,
                "Response message of '" + result.get("endPoint") + "' should include 'Crowd deposit offer not found'",
                message != null
        );
    }

    @Test(description = "User not authenticated")
    public void unAuthorizedUser() {
        setOfferId();
        result = enrollAPI.submitRequest(null, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }
}
