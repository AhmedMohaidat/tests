package APITests;

import APISchema.Admin.*;
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
    protected ApproveDepositAPI approveDeposit;
    protected RejectDepositAPI rejectDeposit;
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
        approveDeposit = new ApproveDepositAPI();
        rejectDeposit = new RejectDepositAPI();

        token = loginAPI.submitRequest();
        try {
            offerId = (int) available.getFirstOfferId(token).get("offerId");
        }catch (Exception exception){}

    }
}
