package com.application.pages;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.DecimalFormat;
import java.util.*;

import static com.application.common.DataStoreUtils.fetchStringFromScenarioDataStore;

public class DashboardPage extends BasePage {

    private final Logger logger = Logger.getLogger(DashboardPage.class);

    private final int paycheck_total = 2000;
    private final int paycheck_count = 26;
    private final double emp_cost = 1000;
    private final double dep_cost = 500;
    private final double discount = 10;

    //headers
    @FindBy(className = "modal-title")
    public WebElement hdrAddEmployee;

    //fields
    @FindBy(xpath = "//label[text()='First Name:']/following::input[1]")
    public WebElement txtFirst;
    @FindBy(xpath = "//label[text()='Last Name:']/following::input[1]")
    public WebElement txtLast;
    @FindBy(xpath = "//label[text()='Dependants:']/following::input[1]")
    public WebElement txtDependents;

    //buttons
    @FindBy(id = "btnAddEmployee")
    public WebElement btnAddEmployee;
    @FindBy(xpath = "//button[text()='Submit']")
    public WebElement btnSubmit;
    @FindBy(xpath = "//button[text()='Close']")
    public WebElement btnClose;

    //tables
    @FindBy(id = "employee-table")
    public WebElement tblEmployee;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Retrieves the employee table
     *
     * @return
     */
    public List<Map<String, String>> getEmployeeTable() {
        List<Map<String, String>> table = new ArrayList<>();
        WebElement tableElement = getElement(tblEmployee);

        // iterate through all rows of the found table element
        List<WebElement> rowElements = tableElement.findElements(By.xpath("//tbody/tr"));

        // get column names of table from table headers
        List<String> columnNames = new ArrayList<String>();
        List<WebElement> headerElements = tableElement.findElements(By.tagName("th"));
        for (WebElement headerElement : headerElements) {
            columnNames.add(headerElement.getText());
        }

        // iterate through all rows and add their content to table array
        for (int i = 0; i < rowElements.size(); i++) {
            Map<String, String> row = new LinkedHashMap<>();

            // add table cells to current row
            int columnIndex = 0;
            List<WebElement> cellElements = rowElements.get(i).findElements(By.tagName("td"));
            for (WebElement cellElement : cellElements) {
                String cellText = cellElement.getText();

                if (columnNames.get(columnIndex).equalsIgnoreCase("actions")) {
                    List<WebElement> actionElements = cellElement.findElements(By.tagName("span"));
                    List<String> actionList = new ArrayList<>();
                    for (WebElement actionElement : actionElements) {
                        String action = actionElement.getAttribute("title");
                        actionList.add(action);
                    }
                    row.put(columnNames.get(columnIndex), Arrays.toString(actionList.toArray()));
                } else {
                    row.put(columnNames.get(columnIndex), cellText);
                }

                columnIndex++;
            }

            table.add(row);
        }
        return table;
    }

    /**
     * Generates a new employee record
     *
     * @return
     */
    public Map<String, String> generateEmployeeRecord() {
        DecimalFormat df = new DecimalFormat("#.00");
        String first = fetchStringFromScenarioDataStore("add_first");
        Map<String, String> record = new LinkedHashMap<>();

        double emp_cost = this.emp_cost;
        double dep_cost = this.dep_cost;

        try {
            if (first.toLowerCase().startsWith("a")) {
                emp_cost = emp_cost - (emp_cost * discount / 100);
                dep_cost = dep_cost - (dep_cost * discount / 100);
            }

            int id = getLastId() + 1;
            String last = fetchStringFromScenarioDataStore("add_last");
            double salary = this.paycheck_total * this.paycheck_count;
            int depedents = Integer.parseInt(fetchStringFromScenarioDataStore("add_dependents"));
            int gross = this.paycheck_total;
            double cost = (emp_cost + (dep_cost * depedents)) / 26;
            double net = gross - cost;

            record.put("ID", String.valueOf(id));
            record.put("Last Name", last);
            record.put("First Name", first);
            record.put("Salary", df.format(salary));
            record.put("Dependents", String.valueOf(depedents));
            record.put("Gross Pay", String.valueOf(gross));
            record.put("Benefit Cost", df.format(cost));
            record.put("Net Pay", df.format(net));
            record.put("Actions", "[Delete, Edit]");

        } catch (Exception e) {
            logger.error("Unable to complete benefit cost calculations: \n" + e.getStackTrace());
            e.printStackTrace();
        }

        return record;
    }

    /**
     * Returns the last ID
     *
     * @return
     */
    public int getLastId() {
        List<Map<String, String>> table = getEmployeeTable();
        int id = 0;

        if (table.size() > 0) {
            Map<String, String> lastRecord = table.get(table.size() - 1);
            id = Integer.parseInt(lastRecord.get("ID"));
        }

        return id;
    }
}
