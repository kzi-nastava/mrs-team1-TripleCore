package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.AdminHomePage;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.AdminRideHistoryPage;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminRideHistoryTests {
    WebDriver driver;
    LoginPage loginPage;
    AdminHomePage adminHomePage;
    AdminRideHistoryPage adminRideHistoryPage;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/java/rs/ac/uns/ftn/asd/Projekatsiit2023/e2e/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:4200/login");

        loginPage = new LoginPage(driver);
        adminHomePage = new AdminHomePage(driver);
        adminRideHistoryPage = new AdminRideHistoryPage(driver);
    }

    @Test
    void testAdminRideHistory() {
        loginPage.login("petar@example.com", "petar123");

        assertTrue(adminHomePage.isAdminHomePageDisplayed(), "Admin home page should be displayed after login");

        adminHomePage.navigateToRideHistory();

        adminRideHistoryPage.waitForPageToLoad();
        adminRideHistoryPage.waitForRidesToAppear();

        assertTrue(adminRideHistoryPage.hasRides(), "Rides should be displayed");

        // test sorting by start date
        adminRideHistoryPage.sortByStart();
        assertTrue(adminRideHistoryPage.isStartColumnSortedAscending(), "Start column should be sorted in ascending order after first click");

        // test sorting by start date again to check descending order
        adminRideHistoryPage.sortByStart();
        assertTrue(adminRideHistoryPage.isStartColumnSortedDescending(), "Start column should be sorted in descending order after second click");

        // test sorting by panic button
        adminRideHistoryPage.sortByPanic();
        assertTrue(adminRideHistoryPage.isPanicColumnSortedAscending(), "Panic column should be sorted in ascending order after first click");

        // test sorting by panic button again to check descending order
        adminRideHistoryPage.sortByPanic();
        assertTrue(adminRideHistoryPage.isPanicColumnSortedDescending(), "Panic column should be sorted in descending order after second click");

        // test sorting by cancelled
        adminRideHistoryPage.sortByCancelled();
        assertTrue(adminRideHistoryPage.isCancelledColumnSortedAscending(), "Cancelled column should be sorted in ascending order after first click");

        // test sorting by cancelled again to check descending order
        adminRideHistoryPage.sortByCancelled();
        assertTrue(adminRideHistoryPage.isCancelledColumnSortedDescending(), "Cancelled column should be sorted in descending order after second click");
    }

    @AfterAll
    void tearDownAll() {
        if (driver != null) {
            driver.quit();
        }
    }
}
