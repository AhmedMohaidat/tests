package APITests.Admin;

import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UpdateOfferTest extends Precondition {

    @Test(description = "Update an existing crowd deposit offer successfully")
    public void UpdateDepositOffer() {
        testData = new HashMap();
        testData.put("id", offerId);
        testData.put("amount", faker.number().numberBetween(100, 100000));

        Map result = updateOffer.updateOffer(token, testData);
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Crowd deposit offer updated'",
                responseBody.equalsIgnoreCase("Crowd deposit offer updated")
        );
    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        testData = new HashMap();
        testData.put("id", offerId);
        testData.put("amount", faker.number().numberBetween(100, 100000));

        Map result = updateOffer.updateOffer(null, testData);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 200
        );
    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        testData = new HashMap();
        testData.put("id", offerId);
        testData.put("amount", faker.number().numberBetween(100, 100000));

        token = (String) loginAPI.submitRequest("").get("token");

        Map result = updateOffer.updateOffer(token, testData);
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }

    @Test(description = "Offer not found")
    public void notFound() {
        testData = new HashMap();
        testData.put("id", 10000);
        testData.put("amount", faker.number().numberBetween(100, 100000));

        Map result = updateOffer.updateOffer(token, testData);
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
