package utils.driver;

import com.application.common.*;
import com.thoughtworks.gauge.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maciej Kocol
 * <p>
 * This is the core class for delcaring before and after hooks to aid in reporting.
 */
public class Driver {

    private final static Logger logger = Logger.getLogger(Driver.class);
    private static String scenarioName;
    private static String specName;
    public static WebDriver webDriver;

    public CommonObjects commonObjects;

    public static WebDriver getActiveDriver() {
        return webDriver;
    }

    // Initialize a webDriver instance of required browser
    @BeforeSuite
    public void initializeDriver() throws IOException {
        webDriver = DriverFactory.getDriver();
        webDriver.manage().deleteAllCookies();
    }

    @AfterScenario
    public void afterScenario() {
        Gauge.captureScreenshot();
    }

    @AfterStep
    public void afterStepPassFail(ExecutionContext executionContext) {
        commonObjects = new CommonObjects(webDriver);
        if (executionContext.getCurrentStep().getIsFailing()) {
            TestResult currentResult = DataStoreUtils.fetchObjectFromScenarioDataStore("currentResult");
            if (currentResult == null)
                return;
            boolean store = false;
            if (currentResult.getPassOrFail() == null) {
                currentResult.setPassOrFail("Fail");
                store = true;
            }
            if (currentResult.getActualResult() == null) {
                currentResult.setActualResult(executionContext.getCurrentStep().getErrorMessage());
                store = true;
            }
            if (currentResult.getExpectedResult() == null) {
                currentResult.setExpectedResult("The test was unable to make it to the Review/Attest report." +
                        " Please see Actual Result for error message information.");
                store = true;
            }
            if (store) {
                List<TestResult> suiteResults = DataStoreUtils.fetchObjectFromSuiteDataStore("testResults");
                suiteResults.add(currentResult);
                DataStoreUtils.storeObjectToSuiteDataStore("testResults", suiteResults);
            }
        }
    }

    @AfterStep
    public void afterStepLogging(ExecutionContext executionContext) {
        if (Boolean.parseBoolean(System.getenv("console_logging"))) {
            List<LogEntry> logs = webDriver.manage().logs().get(LogType.BROWSER).getAll()
                    .stream()
                    .filter(log -> "severe".equalsIgnoreCase(log.getLevel().getName()))
                    .collect(Collectors.toList());

            for (LogEntry log : logs) {
                String consoleLog = "Javascript CONSOLE LOG: " + log.getMessage() + "\n";
                logger.error(consoleLog);
                Gauge.writeMessage(consoleLog);
            }
        }
        if (Boolean.valueOf(System.getenv("screenshot_after_step"))) {
            Gauge.captureScreenshot();
        }
        String currentStepName = executionContext.getCurrentStep().getText();
        boolean isCurrentStepFailing = executionContext.getCurrentStep().getIsFailing();
        String currentScenarioStatus = isCurrentStepFailing ? "Failed" : "Passed";
        logger.info("Step: \"" + currentStepName + "\", status: " + currentScenarioStatus);
    }

    @AfterScenario
    public void afterScenario(ExecutionContext executionContext) {
        Gauge.captureScreenshot();
        String currentScenarioName = executionContext.getCurrentScenario().getName();
        boolean isCurrentScenarioFailing = executionContext.getCurrentScenario().getIsFailing();
        String currentScenarioStatus = isCurrentScenarioFailing ? "Failed" : "Passed";
        logger.info("Scenario: \"" + currentScenarioName + "\" ended. Status: " + currentScenarioStatus);
    }

    @BeforeScenario
    public void beforeScenario (ExecutionContext executionContext) {
        String specName = executionContext.getCurrentSpecification().getName();
        String scenarioName = executionContext.getCurrentScenario().getName();
        DataStoreUtils.storeStringToScenarioDataStore("scenarioName", scenarioName);
        DataStoreUtils.storeStringToScenarioDataStore("specName", specName);
        Driver.scenarioName = scenarioName;
        Driver.specName = specName;
        logger.info("Scenario: \"" + scenarioName + "\" started.");
    }

    @AfterSuite
    public void closeDriver() {
        webDriver.quit();
    }
}
