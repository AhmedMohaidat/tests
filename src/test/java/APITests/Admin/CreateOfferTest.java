package APITests.Admin;

import APITests.Precondition;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class CreateOfferTest extends Precondition {

    @Test(description = "Create a new crowd deposit offer successfully")
    public void createDepositOffer() {
        testData = new HashMap();
        testData.put("amount", faker.number().numberBetween(100, 100000));
        testData.put("period", faker.number().randomDigitNotZero());

        Map result = create.submitOffer(
                token,
                testData
        );
        int statusCode = (int) result.get("statusCode");
        String responseBody = (String) result.get("responseBody");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 200",
                statusCode == 200
        );
        verifyResult(
                responseBody,
                "Response body for '" + result.get("endPoint") + "' should be 'Crowd deposit offer created'",
                responseBody.equalsIgnoreCase("Crowd deposit offer created")
        );
    }

    @Test(description = "Invalid offer data")
    public void invalidData() {
        testData = new HashMap();
        testData.put("amount", "abc");
        testData.put("period", "xyz");

        Map result = create.submitOffer(
                token,
                testData
        );
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 400",
                statusCode == 400
        );

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        testData = new HashMap();
        testData.put("amount", faker.number().numberBetween(100, 100000));
        testData.put("period", faker.number().randomDigitNotZero());

        Map result = create.submitOffer(
                null,
                testData
        );
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        testData = new HashMap();
        testData.put("amount", faker.number().numberBetween(100, 100000));
        testData.put("period", faker.number().randomDigitNotZero());

        token = (String) loginAPI.submitRequest("").get("token");

        Map result = create.submitOffer(
                token,
                testData
        );
        int statusCode = (int) result.get("statusCode");

        verifyResult(
                String.valueOf(statusCode),
                "Status code for '" + result.get("endPoint") + "' should be 401",
                statusCode == 401
        );
    }
}
