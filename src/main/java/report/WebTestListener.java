package report;

import base.TestDataBase.TestDataSetBase;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.Date;

public class WebTestListener extends TestDataSetBase implements ITestListener {

    // Make ExtentReports static to share across all test contexts
    private static ExtentReports extent;
    private ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static int testContextCount = 0;
    private static int completedTestContexts = 0;

    // Initialize ExtentReports only once
    static {
        extent = ExtentManager.createInstance();
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        LOGGER.info("TEST SCENARIO STARTED!");

        // Increment the count of test contexts
        testContextCount++;

        // Create a test node for each test context (Login Test, Register Test, etc.)
        ExtentTest extentTest = extent.startTest("<b><font color=blue>" + "MODULE: </font><br>" + context.getName() +
                "</b> </br> ");
        test.set(extentTest);
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        LOGGER.info("TEST SCENARIO FINISHED!");

        // End the current test
        extent.endTest(test.get());

        // Increment completed contexts counter
        completedTestContexts++;

        // Only flush when all test contexts are complete
        if (completedTestContexts >= testContextCount) {
            extent.flush();
            LOGGER.info("All test contexts completed. ExtentReports flushed.");
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        LOGGER.info((result.getMethod().getMethodName().toUpperCase() + " STARTED!"));
    }

    @SneakyThrows
    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        LOGGER.info(result.getMethod().getMethodName().toUpperCase() + " PASSED!");
        int stepNumber = 0;
        String allSteps = "";
        try {
            for (int i = 0; i < steps.get().size(); i++) {
                stepNumber++;
                allSteps += stepNumber + steps.get().get(i) + "<br>";
            }
        } catch (Exception exception) {
            // Handle exception silently
        }

        String logText = "<b>Scenario: </b>" + result.getMethod().getDescription();
        String description = logText + "<details><summary><b><font color=blue> Click to see details:" +
                "</font></b></summary><font color='green' size='2'><i>" + allSteps + "</i></font></details> \n";

        test.get().log(LogStatus.PASS, description);
    }

    @SneakyThrows
    @Override
    public synchronized void onTestFailure(ITestResult result) {
        LOGGER.error((result.getMethod().getMethodName().toUpperCase() + " FAILED!"));
        int stepNumber = 0;
        String allSteps = "";
        try {
            for (int i = 0; i < steps.get().size(); i++) {
                stepNumber++;
                allSteps += stepNumber + steps.get().get(i) + "<br>";
            }
        } catch (Exception exception) {
            // Handle exception silently
        }

        String exceptionMessage = result.getThrowable().getMessage();
        String logText1 = "<b>Scenario: </b>" + result.getMethod().getDescription();
        String description = logText1 + "<details><summary><b><font color=red>" + "Click to see details:" +
                "</font></b></summary><font color='green' size='2'><i>" + allSteps + "</i></font></details> \n" +
                "</font></b></summary>" + exceptionMessage + "</details> \n";
        LOGWithScreenshot(result, test.get(), LogStatus.FAIL, description);
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        LOGGER.info((result.getMethod().getMethodName().toUpperCase() + " SKIPPED!"));
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOGGER.info(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName().toUpperCase()));
    }

    public static String getScreenshotName(String methodName) {
        Date date = new Date();
        String fileName = methodName + "_" + date.toString().replace(":", "_") + ".png";
        return fileName;
    }

    public void LOGWithScreenshot(ITestResult iTestResult, ExtentTest logger, LogStatus status, String TestDescription) {
        String Base64StringofScreenshot = "";
        ITestContext context = iTestResult.getTestContext();
        // Uncomment and modify as needed for screenshot functionality
        // WebDriver driver = (WebDriver) context.getAttribute("WebDriver");
        // if (driver != null) {
        //     byte[] fileContent = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        //     Base64StringofScreenshot = "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
        // }
        logger.log(status, TestDescription + "\n" + logger.addBase64ScreenShot(Base64StringofScreenshot));
    }
}