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

    @Test(description = "Invalid update data")
    public void invalidData() {
        updateOffer = new UpdateOfferAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        updateOffer = new UpdateOfferAPI();

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        updateOffer = new UpdateOfferAPI();

    }

    @Test(description = "Offer not found")
    public void notFound() {
        updateOffer = new UpdateOfferAPI();

    }
}
