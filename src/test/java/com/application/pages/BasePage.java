package com.application.pages;

import com.application.common.CommonAssert;
import com.application.common.CommonObjects;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Maciej Kocol
 * <p>
 * This is the step class that will be extended by all the other steps.
 */
public class BasePage extends CommonObjects {
    private final Logger logger = Logger.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies that a given element exists.
     *
     * @param message
     * @param element
     */
    public void assertElementExists(String message, WebElement element) {
        CommonAssert.assertElementExists(message, element);
        highLightElement(element);
    }
}

