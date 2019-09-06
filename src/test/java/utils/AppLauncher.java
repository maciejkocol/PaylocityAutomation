package utils;

import com.application.common.CommonSteps;
import com.application.pages.LoginPage;
import com.thoughtworks.gauge.Step;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Cookie;
import utils.driver.Driver;

import java.util.ArrayList;

/**
 * @author Maciej Kocol
 * <p>
 * This is the core class to launch the Paylocity base page using drivers.
 */
public class AppLauncher extends CommonSteps {

    public static String BASE_URL = System.getenv("base_url");
    private LoginPage loginPage;

    @Step("Navigate to ‘Benefits Dashboard’ page")
    public void launchTheApplication() {
        loginPage = new LoginPage(driver);
        Driver.webDriver.navigate().refresh();
        Driver.webDriver.get("file://" + System.getProperty("user.dir") + BASE_URL);

        Assertions.assertThat(loginPage.getPageTitle()).isEqualTo("Login");

        loginPage.login();
        loginPage.waitURLContains("/home.html");
        loginPage.waitUntilFinishedLoading();

        Assertions.assertThat(loginPage.getPageTitle().equalsIgnoreCase("Benefits Dashboard"))
                .as("Landed on Paylocity home page correctly").isTrue();
    }
}
