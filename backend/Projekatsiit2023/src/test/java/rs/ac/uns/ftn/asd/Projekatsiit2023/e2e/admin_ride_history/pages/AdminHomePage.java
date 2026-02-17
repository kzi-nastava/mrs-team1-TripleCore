package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminHomePage {
    WebDriver driver;
    private WebDriverWait wait;

    @FindBy(linkText = "Dashboard")
    WebElement dashboardLink;

    @FindBy(linkText = "Ride history")
    WebElement rideHistoryLink;

    public AdminHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isAdminHomePageDisplayed() {
        wait.until(driver -> dashboardLink.isDisplayed());
        return dashboardLink.isDisplayed();
    }

    public void navigateToRideHistory() {
        wait.until(driver -> rideHistoryLink.isDisplayed() && rideHistoryLink.isEnabled());
        rideHistoryLink.click();
    }
}
