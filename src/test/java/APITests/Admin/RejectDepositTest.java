package APITests.Admin;

import APISchema.Admin.RejectDepositAPI;
import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.Map;

public class RejectDepositTest extends Precondition {

    @Test(description = "Reject a custom deposit request successfully")
    public void rejectCustomDeposit() {
        setOfferId();
        Map result = rejectDeposit.submitRequest(token, offerId);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Custom deposit request has been rejected'",
                responseBody.equalsIgnoreCase("Custom deposit request has been rejected")
        );
    }

    @Test(description = "Invalid rejection request")
    public void invalidData() {

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        setOfferId();
        Map result = rejectDeposit.submitRequest(null, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        setOfferId();
        token = (String) loginAPI.submitRequest("").get("token");

        Map result = rejectDeposit.submitRequest(token, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "Custom deposit request not found")
    public void notFound() {
        Map result = rejectDeposit.submitRequest(token, 0);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Custom deposit request not found'",
                responseBody.contains("Custom deposit request not found")
        );

    }
}
