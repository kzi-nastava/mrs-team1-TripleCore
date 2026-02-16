package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class MyRidesPage {
    WebDriver driver;
    WebDriverWait wait;

    public MyRidesPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened(){
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-my-rides/div/h2"))
        );

        return title.isDisplayed();
    }

    public void OpenReviewForm() {
        WebElement rateLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/button[2]/span[3]"))
        );

        // Not using regular click because the span element with materials classes is not clickable
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rateLink);
    }


    public boolean isFormOpened(){
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/h2"))
        );

        return title.isDisplayed();
    }

    public void submitReview(int driverRating, int vehicleRating, String comment){
        WebElement driverStar = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/mat-card-content/div[1]/div/img[%d]", driverRating)))
        );
        WebElement vehicleStar = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/mat-card-content/div[2]/div/img[%d]", vehicleRating)))
        );
        WebElement commentInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/mat-card-content/mat-form-field/div[1]/div/div[2]/textarea"))
        );

        driverStar.click();
        vehicleStar.click();
        commentInput.sendKeys(comment);

        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/mat-card-actions/button[2]"))
        );


        submitBtn.click();

        WebElement closeBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-my-rides/div/table/tbody/tr/td[7]/div/app-review-form/div/mat-card/button"))
        );

        closeBtn.click();
    }

    public void openMyReviews(){
        WebElement myReviewsLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-passenger-my-rides/app-navbar/nav/div/div[1]/div[2]/ul/li[2]/a"))
        );

        myReviewsLink.click();
    }
}
