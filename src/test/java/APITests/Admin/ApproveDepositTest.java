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

    @Test(description = "Invalid approval request")
    public void invalidData() {
        approveDeposit = new ApproveDepositAPI();

    }

    @Test(description = "User not authenticated")
    public void unAuthorized() {
        approveDeposit = new ApproveDepositAPI();

    }

    @Test(description = "User does not have admin privileges")
    public void notAdmin() {
        approveDeposit = new ApproveDepositAPI();

    }

    @Test(description = "Custom deposit request not found")
    public void notFound() {
        approveDeposit = new ApproveDepositAPI();

    }
}