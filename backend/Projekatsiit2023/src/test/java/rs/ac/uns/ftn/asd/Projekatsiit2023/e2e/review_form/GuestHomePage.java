package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GuestHomePage {

    WebDriver driver;
    WebDriverWait wait;

    public GuestHomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened(){
        WebElement logo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".logo-text"))
        );

        return logo.isDisplayed();
    }

    public void openLogin(){
        WebElement loginLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-home/app-navbar/nav/div/div[1]/div[2]/ul/li[3]/a"))
        );

        loginLink.click();
    }

}
