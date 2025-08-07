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

    @Test(description = "Invalid offer data")
    public void invalidData() {
        create = new CreateOfferAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        create = new CreateOfferAPI();

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        create = new CreateOfferAPI();

    }
}
