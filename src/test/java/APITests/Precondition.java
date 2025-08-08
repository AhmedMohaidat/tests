package APITests;

import APISchema.Admin.*;
import APISchema.Auth.LoginAPI;
import APISchema.Deposit.*;
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
    protected GetAllDepositsAPI getDeposits;
    protected GetDepositIDAPI depositId;
    protected RequestDepositAPI requestDeposit;
    protected CrowdEnrollAPI enrollAPI;

    protected String token;
    protected Map testData;
    protected int offerId;
    protected int depositID;

    @BeforeMethod
    public void precondition() {
        loginAPI = new LoginAPI();
        create = new CreateOfferAPI();
        updateOffer = new UpdateOfferAPI();
        available = new AvailableDepositAPI();
        deleteOffer = new DeleteOfferAPI();
        approveDeposit = new ApproveDepositAPI();
        rejectDeposit = new RejectDepositAPI();
        getDeposits = new GetAllDepositsAPI();
        depositId = new GetDepositIDAPI();
        requestDeposit = new RequestDepositAPI();
        enrollAPI = new CrowdEnrollAPI();

        token = loginAPI.submitRequest();
    }

    protected void setOfferId() {
        try {
            offerId = (int) available.getFirstOfferId(token).get("offerId");
        } catch (Exception exception) {
            // Handle exception or log if needed
            System.err.println("Failed to get offerId: " + exception.getMessage());
        }
    }

    protected int getOfferId() {
        if (offerId == 0) {
            setOfferId();
        }
        return offerId;
    }

    protected int setDepositID() {
        try {
            depositID = (int) getDeposits.getFirstDepositId(token).get("depositId");
        } catch (Exception exception) {
            // Handle exception or log if needed
            System.err.println("Failed to get depositId: " + exception.getMessage());
        }
        return depositID;
    }


}
