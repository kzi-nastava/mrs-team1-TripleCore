package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FavoriteRoutesPage {

    WebDriver driver;
    WebDriverWait wait;

    public FavoriteRoutesPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened(){
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Favorite routes')]")));
        return title.isDisplayed();
    }

    public void selectFirstRoute(){
        WebElement selectBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[contains(text(),'Select Route')])[1]")));
        selectBtn.click();
    }
}