package utils.driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;

/**
 * @author Maciej Kocol
 * <p>
 * This is the core class for Selenium driver setup and options.
 */
public class DriverFactory {
    private static final Logger LOGGER = Logger.getLogger(DriverFactory.class);

    public static WebDriver getDriver() throws MalformedURLException {
        boolean headless = Boolean.parseBoolean(System.getenv("headless"));
        ChromeOptions chromeOptions = new ChromeOptions();
        if (headless) {
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1280x800");
            ChromeDriverManager.chromedriver().setup();
            return new ChromeDriver(chromeOptions);
        } else {
            chromeOptions.addArguments("--start-maximized");
            ChromeDriverManager.chromedriver().setup();
            return new ChromeDriver(chromeOptions);
        }
    }
}