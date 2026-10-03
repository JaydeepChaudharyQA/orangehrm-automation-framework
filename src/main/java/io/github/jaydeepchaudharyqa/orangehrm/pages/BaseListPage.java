package io.github.jaydeepchaudharyqa.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Shared behaviour for OrangeHRM "search + results table" screens (Employee List, System Users…).
 * Written once here and inherited by every list page, so a new list page only adds its own filters.
 */
public abstract class BaseListPage extends AuthenticatedPage {

    private static final By SEARCH_BUTTON = By.cssSelector(".oxd-table-filter button[type='submit']");
    private static final By RESULT_ROWS = By.cssSelector(".oxd-table-body .oxd-table-card");
    private static final By CELLS = By.cssSelector(".oxd-table-cell");
    private static final By RECORDS_LABEL =
            By.xpath("//span[contains(normalize-space(),'Record Found') or contains(normalize-space(),'Records Found')]");
    private static final By CONFIRM_DELETE =
            By.xpath("//div[contains(@class,'oxd-dialog')]//button[normalize-space()='Yes, Delete']");
    private static final String FILTER_INPUT =
            "//div[contains(@class,'oxd-table-filter')]//label[normalize-space()='%s']/ancestor::div[contains(@class,'oxd-input-group')]//input";
    private static final String ROW_DELETE_BUTTON =
            "(//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-card')])[%d]//button[.//i[contains(@class,'bi-trash')]]";

    private static final Duration TABLE_REFRESH_TIMEOUT = Duration.ofSeconds(5);

    protected BaseListPage(WebDriver driver) {
        super(driver);
    }

    /** The text input in the filter panel with the given label, e.g. "Employee Id". */
    protected By filterInput(String label) {
        return By.xpath(String.format(FILTER_INPUT, label));
    }

    /**
     * Clicks Search and waits until the results table has actually been refreshed.
     * Without this, a test can read the <i>old</i> rows while OrangeHRM is re-drawing the table.
     */
    protected void clickSearch() {
        List<WebElement> oldRows = driver.findElements(RESULT_ROWS);
        actions.click(SEARCH_BUTTON);
        if (!oldRows.isEmpty()) {
            waits.staleWithin(oldRows.get(0), TABLE_REFRESH_TIMEOUT);
        }
        waits.forPageReady();
    }

    /** Number of rows currently shown in the results table. */
    public int getResultCount() {
        waits.forPageReady();
        return actions.count(RESULT_ROWS);
    }

    /** The summary above the table, e.g. "(1) Record Found" or "No Records Found". */
    public String getRecordsLabel() {
        return waits.textNotEmpty(RECORDS_LABEL);
    }

    /** All values in one column; index 0 is the checkbox column. Retries if the table re-renders mid-read. */
    protected List<String> getColumnValues(int columnIndex) {
        waits.forPageReady();
        return waits.until(d -> d.findElements(RESULT_ROWS).stream()
                .map(row -> row.findElements(CELLS))
                .filter(cells -> cells.size() > columnIndex)
                .map(cells -> cells.get(columnIndex))
                .map(WebElement::getText)
                .map(String::trim)
                .toList(), "reading column " + columnIndex + " of the results table");
    }

    /** Deletes the row at the given position (1-based) and returns the confirmation toast text. */
    protected String deleteRow(int position) {
        actions.click(By.xpath(String.format(ROW_DELETE_BUTTON, position)));
        actions.click(CONFIRM_DELETE);
        String toast = getToastMessage();
        waits.forPageReady();
        return toast;
    }
}
