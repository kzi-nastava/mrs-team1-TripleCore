package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeleniumTest {

    private WebDriver driver;

    @BeforeAll
    void prepareTestData(){

    }

    @BeforeEach
    void setup() {
        System.setProperty("webdriver.chrome.driver", "src/test/java/rs/ac/uns/ftn/asd/Projekatsiit2023/e2e/chromedriver.exe");

        driver = new ChromeDriver();
    }

    @Test
    void openLocalhost() {
        driver.get("http://localhost:4200");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement logo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("logo-text"))
        );

        Assertions.assertTrue(logo.isDisplayed());


    }

//    @AfterEach
//    void teardown() {
//        if (driver != null) driver.quit();
//    }
}
