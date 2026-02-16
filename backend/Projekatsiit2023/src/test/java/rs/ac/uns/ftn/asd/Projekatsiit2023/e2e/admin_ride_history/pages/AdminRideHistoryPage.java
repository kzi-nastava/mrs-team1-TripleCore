package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminRideHistoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "table.rides-table")
    private WebElement ridesTable;

    public AdminRideHistoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOf(ridesTable));
    }

    public void waitForRidesToAppear() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@mat-row]")));
    }

    public boolean hasRides() {
        List<WebElement> rideRows = driver.findElements(By.xpath("//tr[@mat-row]"));
        return !rideRows.isEmpty();
    }

    // sort by date
    public void sortByStart() {
        WebElement startColumnHeader = driver.findElement(By.xpath("//th[contains(.,'Start')]"));
        startColumnHeader.click();
    }

    public boolean isStartColumnSortedAscending() {
        return isColumnSorted("startDate", true);
    }

    public boolean isStartColumnSortedDescending() {
        return isColumnSorted("startDate", false);
    }

    private boolean isColumnSorted(String columnDataAttr, boolean ascending) {
        List<WebElement> cells = driver.findElements(By.xpath("//tr[@mat-row]/td[@data-column='" + columnDataAttr + "']"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<LocalDateTime> dates = new ArrayList<>();

        for (WebElement cell : cells) {
            String dateText = cell.getText().replace("\n", " ").trim(); // dd.MM.yyyy HH:mm
            dates.add(LocalDateTime.parse(dateText, formatter));
        }

        for (int i = 0; i < dates.size() - 1; i++) {
            if (ascending && dates.get(i).isAfter(dates.get(i + 1))) return false;
            if (!ascending && dates.get(i).isBefore(dates.get(i + 1))) return false;
        }
        return true;
    }

    // sort by boolean columns: Cancelled and Panic
    public void sortByCancelled() {
        WebElement cancelledHeader = driver.findElement(By.xpath("//th[contains(.,'Cancelled')]"));
        cancelledHeader.click();
    }

    public void sortByPanic() {
        WebElement panicHeader = driver.findElement(By.xpath("//th[contains(.,'Panic')]"));
        panicHeader.click();
    }

    public boolean isCancelledColumnSortedAscending() {
        return isStatusColumnSorted("cancelled", true);
    }

    public boolean isCancelledColumnSortedDescending() {
        return isStatusColumnSorted("cancelled", false);
    }

    public boolean isPanicColumnSortedAscending() {
        return isStatusColumnSorted("panic", true);
    }

    public boolean isPanicColumnSortedDescending() {
        return isStatusColumnSorted("panic", false);
    }

    // helper method to check sorting of boolean columns
    private boolean isStatusColumnSorted(String columnDataAttr, boolean ascending) {
        List<WebElement> cells = driver.findElements(By.xpath("//tr[@mat-row]/td[@data-column='" + columnDataAttr + "']"));
        List<Integer> values = new ArrayList<>(); // 0 = No, 1 = Yes

        for (WebElement cell : cells) {
            String text = cell.getText().trim().toLowerCase();
            values.add(text.equals("yes") || text.equals("YES") ? 1 : 0);
        }

        for (int i = 0; i < values.size() - 1; i++) {
            if (ascending && values.get(i) > values.get(i + 1)) return false;
            if (!ascending && values.get(i) < values.get(i + 1)) return false;
        }
        return true;
    }
}
