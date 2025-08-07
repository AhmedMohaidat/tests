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
}
