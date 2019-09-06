package com.application.common;

import com.thoughtworks.gauge.Gauge;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.driver.Driver;

import java.time.Duration;

/**
 * @author Maciej Kocol
 * <p>
 * A helper class for managing all shared page objects.
 */
public class CommonObjects {

    protected WebDriver driver;
    protected FluentWait wait;
    protected JavascriptExecutor javascriptExecutor;
    private int sleepTime = 1000;
    private WebDriverWait webDriverWait;
    private static final Logger logger = Logger.getLogger(CommonObjects.class);

    public CommonObjects(WebDriver driver) {
        this.driver = Driver.webDriver;
        this.webDriverWait = new WebDriverWait(driver, 10);
        this.wait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);
        PageFactory.initElements(driver, this);
    }

    /**
     * Returns JavascriptExecutor object
     *
     * @return
     */
    public JavascriptExecutor getJSObject() {
        if (javascriptExecutor == null) {
            javascriptExecutor = (JavascriptExecutor) driver;
        }
        return javascriptExecutor;
    }

    /**
     * This method gets the current page title.
     * @return Page title as string
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * This method gets the current page URL.
     * @return Page url as string
     */
    public String getPageURL() {
        return driver.getCurrentUrl();
    }

    /**
     * Scrolls into view Web Element
     * @param element - element
     */
    public void scrollIntoView(WebElement element) {
        javascriptExecutor.executeScript("arguments[0].scrollIntoView()", getElement(element));
    }

    /**
     * Scrolls into view element with coordinates
     * @param webElement - webElement
     * @param coordinateOfY - coordinateOfY
     */
    public void scrollIntoView(WebElement webElement, int coordinateOfY) {
        int y = getElement(webElement).getLocation().getY() - coordinateOfY;
        javascriptExecutor.executeScript("scroll(0," + y + ")");
    }

    public void waitUntilFinishedLoading() {
        webDriverWait.until((driver) ->
                getJSObject().executeScript("return document.readyState").toString().equals("complete")
        );
    }

    /**
     * Click on an element.
     */
    public void clickElement(WebElement element) {
        waitForElement(element);
        try {
            element.click();
        } catch (WebDriverException e) {
            click(getElement(element));
        }
    }

    /**
     * Checks if web element displayed on UI
     * @param element - UI Web Element
     * @return - is Web Element Displayed
     */
    public boolean isElementDisplayed (WebElement element) {
        try {
            if (element.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException e) {}
        return false;
    }

    /**
     * Gets a web element on the web page
     * @param webElementLocator - webElementLocator
     */
    public WebElement getElement(By webElementLocator) {
        waitUntilFinishedLoading();
        WebElement webElement;
        try {
            webElement = driver.findElement(webElementLocator);
        } catch ( NoSuchElementException e ) {
            getWindowFocus();
            webElement = driver.findElement(webElementLocator);
        }
        waitElementVisibility(webElement);
        return webElement;
    }

    /**
     * Gets a web element on the web page
     * @param webElement - webElement
     * @return
     */
    public WebElement getElement(WebElement webElement) {
        waitUntilFinishedLoading();
        waitElementVisibility(webElement);
        return webElement;
    }

    /**
     * This method checks if page contains a given element.
     * @return true if contains
     */
    public boolean pageContains(By byXPath) {
        waitUntilFinishedLoading();
        boolean pageContains = false;
        int attempts = 0;
        while (attempts <= 3) {
            try {
                driver.findElement(byXPath);
                pageContains = true;
                break;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                sleepForSeconds(1);
                attempts++;
            }
        }
        return pageContains;
    }

    /**
     * Sends text to input element
     * @param webElement
     * @param text
     */
    public void sendKeys(WebElement webElement, String text) {
        WebElement inputFieldsElement = getElement(webElement);
        if (inputFieldsElement.getTagName().equalsIgnoreCase("input")) {
            inputFieldsElement.clear();
        }
        inputFieldsElement.sendKeys(text);
    }

    /**
     * Gets text from web element
     * @param webElement - web element
     * @return - UI text
     */
    public String getText(WebElement webElement) {
        webElement = getElement(webElement);
        String webElementText = webElement.getText();
        return webElementText.replaceAll("\\s+", " ").trim();
    }

    /**
     * Gets Web Element`s Attribute text
     * @param element - element
     * @param attribute - attribute
     * @return - Web Element`s Attribute text
     */
    public String getAttribute (WebElement element, String attribute) {
        String elementAttribute = "";
        if ( isElementHasAttribute(element, attribute) ) {
            elementAttribute = element.getAttribute(attribute).trim();
        }
        return elementAttribute;
    }

    public boolean isElementHasAttribute(WebElement element, String attribute) {
        boolean isAttribute = false;
        WebElement webElement = getElement(element);
        String attributeValue = webElement.getAttribute(attribute.toLowerCase());
        if ( attributeValue != null ) {
            isAttribute = true;
        }
        return isAttribute;
    }

    /**
     * Driver timeout in seconds
     * @param timeOutInSeconds - timeOutInSeconds
     */
    public void sleepForSeconds(int timeOutInSeconds) {
        sleepForMill(timeOutInSeconds * 1000);
    }

    /**
     * Driver timeout in milliseconds
     * @param timeOutInMilliseconds - timeOutInMilliseconds
     */
    public void sleepForMill(int timeOutInMilliseconds) {
        try {
            Thread.sleep(timeOutInMilliseconds);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Highlights a web element on UI
     *
     * @param webElement
     */
    public void highLightElement(WebElement webElement) {
        getJSObject().executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", webElement);
    }

    /**
     * Highlights a web element on UI
     */
    public void highLightElement(By by) {
        WebElement webElement = driver.findElement(by);
        getJSObject().executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", webElement);
    }

    /**
     * Logs a Gauge Message in test report
     *
     * @param logMessage - Logger Message
     */
    public static void gaugeLogger(String logMessage) {
        Gauge.writeMessage(logMessage);
    }

    /**
     * Clicks on web element
     * in case of failure:
     * prints error message,
     * scrolls to the web element with coordinates of 200 (so it is not blocked by header element)
     * clicks on the web element again
     *
     * @param webElement
     */
    public void click(WebElement webElement) {
        try {
            webElement.click();
        } catch (WebDriverException e) {
            sleepFor(1);
            scrollIntoView(webElement, 200);
            webElement.click();
        }
    }

    /**
     * Driver timeout in seconds
     *
     * @param timeOutInSeconds
     */
    public void sleepFor(int timeOutInSeconds) {
        try {
            Thread.sleep(timeOutInSeconds * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Wait for element to be visible and clickable.
     */
    public void waitForElement(WebElement element) {
        try {
            Thread.sleep(sleepTime / 5);
            this.waitUntilFinishedLoading();
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wait until web element visible on UI
     *
     * @param webElementLocator - webElementLocator
     */
    public void waitElementVisibility(By webElementLocator) {
        waitUntilFinishedLoading();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(webElementLocator));
    }

    /**
     * Wait until web element visible on UI
     *
     * @param webElement - webElement
     */
    public void waitElementVisibility(WebElement webElement) {
        waitUntilFinishedLoading();
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Wait for URL to contain a string
     *
     * @param url
     */
    public void waitURLContains(String url) {
        waitUntilFinishedLoading();
        webDriverWait.until(ExpectedConditions.urlContains(url));
    }

    /**
     * Fill an input field with text.
     */
    public void fillInputField(WebElement element, String text) {
        waitForElement(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * This method gets the inner html text of an element.
     *
     * @return text as String
     */
    public String getTextOfAnElement(WebElement element) {
        waitForElement(element);
        highLightElement(element);
        return element.getText();
    }

    private void getWindowFocus () {
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
    }
}

