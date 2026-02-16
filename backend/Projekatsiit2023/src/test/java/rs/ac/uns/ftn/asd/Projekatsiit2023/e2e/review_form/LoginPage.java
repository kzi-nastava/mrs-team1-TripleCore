package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String email, String password){
        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/form/input[1]")))
        );
        WebElement passwordInput = wait.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/form/input[2]")))
        );
        WebElement loginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/form/button")))
        );

        loginBtn.click();
    }
}
