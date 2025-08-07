package APITests.Admin;

import APISchema.Admin.RejectDepositAPI;
import org.testng.annotations.Test;

public class RejectDepositTest {

    RejectDepositAPI rejectDeposit;

    @Test(description = "Reject a custom deposit request successfully")
    public void rejectCustomDeposit() {
        rejectDeposit = new RejectDepositAPI();
        rejectDeposit.submitRequest();
    }

    @Test(description = "Invalid rejection request")
    public void invalidData() {
        rejectDeposit = new RejectDepositAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        rejectDeposit = new RejectDepositAPI();

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        rejectDeposit = new RejectDepositAPI();

    }

    @Test(description = "Custom deposit request not found")
    public void notFound() {
        rejectDeposit = new RejectDepositAPI();

    }
}
