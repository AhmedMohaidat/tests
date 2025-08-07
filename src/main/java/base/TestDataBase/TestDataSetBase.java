package base.TestDataBase;

import helpers.ReadWriteHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TestDataSetBase {

    protected long retries;
    protected String baseUrl, mockUrl, thirdParty;
    protected static String sender = "";
    protected static String mailPassword = "";
    protected static String smtpHost = "";
    protected static String smtpAuth = "true";
    protected static String smtpPort = "25";


    protected static Map<String, Object> testConfig = new ConcurrentHashMap<>();
    protected ArrayList<String> stepList;
    protected static ThreadLocal<ArrayList<String>> steps = new ThreadLocal<>();

    protected final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());



    public TestDataSetBase() {
        stepList = new ArrayList<>();

        Map testConfigFile = ReadWriteHelper.getDataFromJson("src/main/java/config/testConfig.json");

        Iterator testConfigKeys = testConfigFile.keySet().iterator();
        while (testConfigKeys.hasNext()) {
            String testConfigKey = (String) testConfigKeys.next();
            Object testConfigValue = testConfigFile.get(testConfigKey);

            testConfig.put(testConfigKey, testConfigValue);
        }
        thirdParty = (String) testConfig.get("thirdParty");
        baseUrl = (String) testConfig.get("baseUrl");
        if (thirdParty.equalsIgnoreCase("mock")){
            baseUrl = (String) testConfig.get("mockUrl");
        }

        retries = (long) testConfig.get("retries");
    }
}
