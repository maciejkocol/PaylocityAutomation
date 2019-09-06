package com.application.common;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import utils.driver.Driver;

import java.util.List;
import java.util.Map;

/**
 * @author Maciej Kocol
 * <p>
 * This is the common assert class for validations.
 */
public class CommonAssert {

    private static CommonObjects commonObjects;

    private CommonAssert() {
        commonObjects = new CommonObjects(Driver.webDriver);
    }

    /**
     * Asserts the condition is equal to true, in case: false, throws Assertion error with a message
     * @param errorMessage - errorMessage
     * @param isTrue - isTrue
     */
    public static void assertTrue ( String errorMessage, boolean isTrue ) {
        Assert.assertTrue(errorMessage, isTrue);
    }

    /**
     * Asserts the condition is equal to false, in case: true, throws Assertion error with a message
     * @param errorMessage - errorMessage
     * @param isFalse - isFalse
     */
    public static void assertFalse ( String errorMessage, boolean isFalse ) {
        Assert.assertFalse(errorMessage, isFalse);
    }

    /**
     * Verifies that a given element exists.
     *
     * @param message
     * @param element
     */
    public static void assertElementExists(String message, WebElement element) {
        Assertions.assertThat(element.isDisplayed()).as(message).isTrue();
    }

    /**
     * Asserts web element text value with expected text
     * @param actualText - actualText
     * @param expectedText - expectedText
     */
    public static void assertEqualsText ( String actualText, String expectedText ) {
        assertTrue("\nActual: " + actualText + ",\nExpected: " + expectedText, actualText.equals(expectedText));
    }

    /**
     * Asserts List text value with expected list
     * @param list1 - list1 (Actual List)
     * @param list2 - list2 (Expected List)
     */
    public static void assertListsOfMapsEquals (List<Map<String, String>> list1, List<Map<String, String>> list2 ) {
        assertTrue("Number of elements not matched between lists", list1.size() == list2.size());
        for (int index = 0; index < list1.size(); index++) {
            Map<String, String> list2Map = list2.get(index);
            assertTrue("List mismatch at: " + list2Map, list1.contains(list2Map));
        }
    }

    /**
     * Asserts web element text value with expected text
     * @param webElement - webElement
     * @param expectedText - expectedText
     */
    public static void assertContainsText (WebElement webElement, String expectedText ) {
        commonObjects.scrollIntoView(webElement, 200);
        String actualText = commonObjects.getText(webElement);
        assertContainsText(actualText, expectedText);
    }

    /**
     * Asserts web element text value with expected text
     * @param actualText - actualText
     * @param expectedText - expectedText
     */
    public static void assertContainsText ( String actualText, String expectedText ) {
        assertTrue("Text \"" + actualText + "\" did not contain \"" + expectedText + "\" text.", actualText.contains(expectedText));
    }
}
