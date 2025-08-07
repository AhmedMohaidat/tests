package APITests.Admin;

import APISchema.Admin.ApproveDepositAPI;
import org.testng.annotations.Test;

public class ApproveDepositTest {

    ApproveDepositAPI approveDeposit;


    @Test(description = "Approve a custom deposit request successfully")
    public void approveCustomDeposit() {
        approveDeposit = new ApproveDepositAPI();
        approveDeposit.submitRequest();
    }
}
