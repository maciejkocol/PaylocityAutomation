package com.application.steps;

import com.application.common.CommonAssert;
import com.application.common.CommonSteps;
import com.application.pages.DashboardPage;
import com.thoughtworks.gauge.ContinueOnFailure;
import com.thoughtworks.gauge.Step;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

import static com.application.common.DataStoreUtils.*;

public class DashboardSteps extends CommonSteps {

    private final Logger logger = Logger.getLogger(DashboardSteps.class);
    private DashboardPage dashboardPage;

    @ContinueOnFailure
    @Step("Start add employee process")
    public void addEmployee() {
        dashboardPage = new DashboardPage(driver);

        List<Map<String, String>> table = dashboardPage.getEmployeeTable();
        storeObjectToScenarioDataStore("employee_table", table);
        dashboardPage.clickElement(dashboardPage.btnAddEmployee);

        String actualHeader = dashboardPage.getElement(dashboardPage.hdrAddEmployee).getText();
        String expectHeader = "Add Employee & His dependents";

        CommonAssert.assertEqualsText(actualHeader, expectHeader);
    }

    @ContinueOnFailure
    @Step("Fill employee details with <first> <last> <dependents>")
    public void addEmployee(String first, String last, String dependents) {
        dashboardPage = new DashboardPage(driver);

        dashboardPage.assertElementExists("First Name input not found", dashboardPage.txtFirst);
        dashboardPage.assertElementExists("Last Name input not found", dashboardPage.txtLast);
        dashboardPage.assertElementExists("Dependents input not found", dashboardPage.txtDependents);

        dashboardPage.fillInputField(dashboardPage.txtFirst, first);
        dashboardPage.fillInputField(dashboardPage.txtLast, last);
        dashboardPage.fillInputField(dashboardPage.txtDependents, dependents);

        storeStringToScenarioDataStore("add_first", first);
        storeStringToScenarioDataStore("add_last", last);
        storeStringToScenarioDataStore("add_dependents", dependents);

        dashboardPage.assertElementExists("Submit button not found", dashboardPage.btnSubmit);
        dashboardPage.assertElementExists("Close button not found", dashboardPage.btnClose);
    }

    @ContinueOnFailure
    @Step("Submit employee")
    public void submitEmployee() {
        dashboardPage = new DashboardPage(driver);
        dashboardPage.clickElement(dashboardPage.btnSubmit);

        List<Map<String, String>> table = fetchObjectFromScenarioDataStore("employee_table");
        Map<String, String> newEmployee = dashboardPage.generateEmployeeRecord();

        table.add(newEmployee);
        storeObjectToScenarioDataStore("employee_table", table);
    }

    @ContinueOnFailure
    @Step("Validate employee table")
    public void validateEmployeeTable() {
        dashboardPage = new DashboardPage(driver);

        List<Map<String, String>> actualTable = dashboardPage.getEmployeeTable();
        List<Map<String, String>> expectTable = fetchObjectFromScenarioDataStore("employee_table");

        CommonAssert.assertListsOfMapsEquals(actualTable, expectTable);
    }
}
