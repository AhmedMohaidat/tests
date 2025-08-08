package APITests.Admin;

import APISchema.Admin.ApproveDepositAPI;
import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.Map;

public class ApproveDepositTest extends Precondition {


    @Test(description = "Approve a custom deposit request successfully")
    public void approveCustomDeposit() {
        Map result = approveDeposit.submitRequest(token, offerId);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Custom deposit request has been approved'",
                responseBody.equalsIgnoreCase("Custom deposit request has been approved")
        );
    }

    @Test(description = "Invalid approval request")
    public void invalidData() {

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        Map result = approveDeposit.submitRequest(null, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        token = (String) loginAPI.submitRequest("").get("token");

        Map result = approveDeposit.submitRequest(token, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "Custom deposit request not found")
    public void notFound() {
        Map result = approveDeposit.submitRequest(token, 0);
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