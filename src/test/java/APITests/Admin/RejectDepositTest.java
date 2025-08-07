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
}
