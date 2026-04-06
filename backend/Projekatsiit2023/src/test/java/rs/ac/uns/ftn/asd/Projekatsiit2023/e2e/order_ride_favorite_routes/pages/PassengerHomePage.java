package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PassengerHomePage {

    WebDriver driver;
    WebDriverWait wait;

    public PassengerHomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened(){
        WebElement favoriteRoutesLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Order a ride')]")));
        return favoriteRoutesLabel.isDisplayed();
    }

    public void clickOrderFromFavoriteRoutes(){
        WebElement favBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Order from Favorite Routes')]")));
        favBtn.click();
    }

    public boolean isRouteLoaded(){
        WebElement start = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Start point']")));

        String value = start.getAttribute("value");
        return value != null && !value.isEmpty();
    }

    public void clickOrderRide() {
        WebElement startInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Start point']")));
        wait.until(ExpectedConditions.attributeToBeNotEmpty(startInput, "value"));

        WebElement orderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Order ride')]")));

        orderBtn.click();
    }

}