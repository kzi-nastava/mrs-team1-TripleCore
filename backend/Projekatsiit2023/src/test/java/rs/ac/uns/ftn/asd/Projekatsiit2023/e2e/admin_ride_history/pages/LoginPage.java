package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        wait.until(driver -> emailInput.isDisplayed() && emailInput.isEnabled());
        emailInput.clear();
        emailInput.sendKeys(email);

        wait.until(driver -> passwordInput.isDisplayed() && passwordInput.isEnabled());
        passwordInput.clear();
        passwordInput.sendKeys(password);

        wait.until(driver -> loginButton.isDisplayed() && loginButton.isEnabled());
        loginButton.click();
    }
}
