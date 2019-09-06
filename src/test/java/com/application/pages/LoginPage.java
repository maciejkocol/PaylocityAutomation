package com.application.pages;

import com.application.common.CommonObjects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Maciej Kocol
 * <p>
 * Login page objects for Paylocity application.
 */
public class LoginPage extends CommonObjects {

    //input fields
    @FindBy(name = "form-username")
    private WebElement usernameField;
    @FindBy(name = "form-password")
    private WebElement passwordField;

    //buttons
    @FindBy(id = "btnLogin")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Logs user into Paylocity
     */
    public void login() {
        String username = System.getenv("paylocity_username");
        String password = System.getenv("paylocity_password");
        fillInputField(usernameField, username);
        fillInputField(passwordField, password);
        clickElement(loginButton);
    }
}
