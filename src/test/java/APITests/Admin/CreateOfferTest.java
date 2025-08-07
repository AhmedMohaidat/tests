package APITests.Admin;

import APISchema.Admin.CreateOfferAPI;
import org.testng.annotations.Test;

public class CreateOfferTest {

    CreateOfferAPI create;

    @Test(description = "Create a new crowd deposit offer successfully")
    public void createDepositOffer() {
        create = new CreateOfferAPI();
        create.submitOffer();
    }
}
