package APITests;

import APISchema.Admin.CreateOfferAPI;
import APISchema.Admin.DeleteOfferAPI;
import APISchema.Admin.UpdateOfferAPI;
import APISchema.Auth.LoginAPI;
import APISchema.Deposit.AvailableDepositAPI;
import base.APITestBase.ApiTestBase;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

public class Precondition extends ApiTestBase {
    protected LoginAPI loginAPI;
    protected CreateOfferAPI create;
    protected UpdateOfferAPI updateOffer;
    protected AvailableDepositAPI available;
    protected DeleteOfferAPI deleteOffer;
    protected String token;
    protected Map testData;
    protected int offerId;

    @BeforeMethod
    public void precondition(){
        loginAPI = new LoginAPI();
        create = new CreateOfferAPI();
        updateOffer = new UpdateOfferAPI();
        available = new AvailableDepositAPI();
        deleteOffer = new DeleteOfferAPI();

        token = loginAPI.submitRequest();
        offerId = (int) available.getFirstOfferId(token).get("offerId");
    }
}
