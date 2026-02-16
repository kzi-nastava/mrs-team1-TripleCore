package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyReviewsPage {

    WebDriver driver;
    WebDriverWait wait;

    public MyReviewsPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened(){
        WebElement reviewsContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-passenger-reviews/app-reviews-page/div"))
        );

        return reviewsContainer.isDisplayed();
    }

    public boolean reviewLoaded(String comment){
        List<WebElement> cards = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".review-card"))
        );

        for (WebElement card : cards){
            String cardComment = card.findElement(By.cssSelector(".comment")).getText();
            if (cardComment.equals(comment))
                return true;
        }

        return false;
    }
}
