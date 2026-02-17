package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form.pages;

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
        WebElement orderRideLabel = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-home/app-order-ride-registered-user/div/div/form/div/section[1]/h3"))
        );

        return orderRideLabel.isDisplayed();
    }

    public void openMyRides(){
        WebElement myRidesLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-home/app-navbar/nav/div/div[1]/div[2]/ul/li[1]/a/span"))
        );

        myRidesLink.click();
    }




}
