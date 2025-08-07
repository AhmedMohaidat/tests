package report;

import base.TestDataBase.TestDataSetBase;
import com.relevantcodes.extentreports.ExtentReports;
import helpers.DateHelper;

public class ExtentManager extends TestDataSetBase {

    static DateHelper date = new DateHelper();
    static private ExtentReports extent;
    public static String reportFileName = date.getCurrentDate("yyyy-MM-dd") + "-" + testConfig.get("AUT") + "-Test-Report-" + System.currentTimeMillis() + ".html";
    public static String path = "src/main/resources/Reports/" + date.getCurrentDate("yyyy-MM-dd") + "/";


    public static String getReportPath() {
        return path + reportFileName;
    }

    public static ExtentReports createInstance() {

        extent = new ExtentReports(getReportPath());
        extent.addSystemInfo("AUTHOR", "Ahmad Muhaidat");
        extent.addSystemInfo("ENVIRONMENT", "<b><font color='blue'>" + testConfig.get("baseUrl") + "<font/></b>");
        extent.addSystemInfo("POWERED BY:", "<b>Wadaie</b>");
//        extent.loadConfig(new File("src/main/java/Core/report/extent-report.xml"));

        return extent;
    }
}
