package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form.pages.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form.ReviewFormTestDataFactory.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewFormTest {

    @Autowired
    RouteRepository routeRepository;
    @Autowired
    RideRepository rideRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    VehicleRepository vehicleRepository;

    Passenger passenger;

    @BeforeAll
    void setupData(){
        Vehicle vehicle = vehicleRepository.save(createTestVehicle());
        Driver driver = driverRepository.save(createTestDriver(vehicle));
        passenger = passengerRepository.save(createTestPassenger());
        Route route = routeRepository.save(createTestRoute());
        Ride ride = rideRepository.save(createTestRide(passenger, driver, route));
    }

    private WebDriver driver;
    private GuestHomePage guestHomePage;
    private LoginPage loginPage;
    private PassengerHomePage passengerHomePage;
    private MyRidesPage myRidesPage;
    private MyReviewsPage myReviewsPage;

    @BeforeEach
    void setup(){
        System.setProperty("webdriver.chrome.driver", "src/test/java/rs/ac/uns/ftn/asd/Projekatsiit2023/e2e/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:4200");
        guestHomePage = new GuestHomePage(driver);
        loginPage = new LoginPage(driver);
        passengerHomePage = new PassengerHomePage(driver);
        myRidesPage = new MyRidesPage(driver);
        myReviewsPage = new MyReviewsPage(driver);
    }

    @Test
    void CreateReviewTest(){
        assertTrue(guestHomePage.isOpened());
        guestHomePage.openLogin();

        assertTrue(loginPage.isOpened());
        loginPage.login(passenger.getEmail(), passenger.getPassword());

        assertTrue(passengerHomePage.isOpened());
        passengerHomePage.openMyRides();

        assertTrue(myRidesPage.isOpened());
        myRidesPage.OpenReviewForm();
        assertTrue(myRidesPage.isFormOpened());

        int driverRating = 5;
        int vehicleRating = 5;
        String comment = "Amazing ride!";

        myRidesPage.submitReview(driverRating, vehicleRating, comment);
        myRidesPage.openMyReviews();

        assertTrue(myReviewsPage.isOpened());
        assertTrue(myReviewsPage.reviewLoaded(comment));
    }

}
