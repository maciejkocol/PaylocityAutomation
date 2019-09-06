package com.application.common;

/**
 * @author Maciej Kocol
 * <p>
 * This is the step class that organizes the way test results are displayed.
 */
public class TestResult {
    private String testCase;
    private String expectedResult;
    private String actualResult;
    private String passOrFail;

    public TestResult(String testCase, String expectedResult, String actualResult, String passOrFail) {
        this.testCase = testCase;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.passOrFail = passOrFail;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public String getPassOrFail() {
        return passOrFail;
    }

    public void setPassOrFail(String passOrFail) {
        this.passOrFail = passOrFail;
    }
}
