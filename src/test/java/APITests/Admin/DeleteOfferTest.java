package APITests.Admin;

import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.Map;

public class DeleteOfferTest extends Precondition {


    @Test(description = "Delete a crowd Deposit offer successfully")
    public void deleteDepositOffer() {
        setOfferId();
        Map result = deleteOffer.deleteOffer(token, offerId);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Crowd deposit offer successfully deleted'",
                responseBody.equalsIgnoreCase("Crowd deposit offer successfully deleted")
        );
    }

    @Test(description = "Invalid delete request")
    public void invalidData() {
    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        setOfferId();
        Map result = deleteOffer.deleteOffer(null, offerId);
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

        Map result = deleteOffer.deleteOffer(token, offerId);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "Offer not found")
    public void notFound() {
        Map result = deleteOffer.deleteOffer(token, 1000000);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Crowd deposit offer not found'",
                responseBody.contains("Crowd deposit offer not found")
        );
    }
}
