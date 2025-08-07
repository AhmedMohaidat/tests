package APITests.Admin;

import APISchema.Admin.UpdateOfferAPI;
import org.testng.annotations.Test;

public class UpdateOfferTest {

    UpdateOfferAPI updateOffer;

    @Test(description = "Update an existing crowd deposit offer successfully")
    public void UpdateDepositOffer() {
        updateOffer = new UpdateOfferAPI();
        updateOffer.updateOffer();
    }
}
