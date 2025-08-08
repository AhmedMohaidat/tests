package APITests.Admin;

import APITests.Precondition;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CreateOffersTestData extends Precondition {

    @Test(description = "Create a new crowd deposit offer successfully")
    public void createDepositOffer() {
        for (int i = 0; i < 20; i++){
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

    }
}
