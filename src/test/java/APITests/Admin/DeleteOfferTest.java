package APITests.Admin;

import APISchema.Admin.DeleteOfferAPI;
import org.testng.annotations.Test;

public class DeleteOfferTest {

    DeleteOfferAPI deleteOffer;


    @Test(description = "Delete a crowd Deposit offer successfully")
    public void deleteDepositOffer() {
        deleteOffer = new DeleteOfferAPI();
        deleteOffer.deleteOffer();
    }

    @Test(description = "Invalid delete request")
    public void invalidData() {
        deleteOffer = new DeleteOfferAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        deleteOffer = new DeleteOfferAPI();

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        deleteOffer = new DeleteOfferAPI();

    }

    @Test(description = "Offer not found")
    public void notFound() {
        deleteOffer = new DeleteOfferAPI();

    }
}
