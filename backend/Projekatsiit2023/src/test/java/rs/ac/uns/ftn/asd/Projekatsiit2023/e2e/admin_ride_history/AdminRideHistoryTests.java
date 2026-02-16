package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.AdminHomePage;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.AdminRideHistoryPage;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.pages.LoginPage;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history.AdminRideHistoryTestDataFactory.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminRideHistoryTests {

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    RideRepository rideRepository;

    Admin admin;

    @BeforeAll
    void setupData(){
        Vehicle v1 = vehicleRepository.save(createTestVehicle1());
        Vehicle v2 = vehicleRepository.save(createTestVehicle2());
        Vehicle v3 = vehicleRepository.save(createTestVehicle3());

        Driver d1 = createTestDriver1();
        d1.setVehicle(v1);
        driverRepository.save(d1);
        Driver d2 = createTestDriver2();
        d2.setVehicle(v2);
        driverRepository.save(d2);
        Driver d3 = createTestDriver3();
        d3.setVehicle(v3);
        driverRepository.save(d3);

        Passenger p1 = passengerRepository.save(createTestPassenger1());
        Passenger p2 = passengerRepository.save(createTestPassenger2());
        Passenger p3 = passengerRepository.save(createTestPassenger3());

        Route r1 = routeRepository.save(createTestRoute1());
        Route r2 = routeRepository.save(createTestRoute2());
        Route r3 = routeRepository.save(createTestRoute3());

        Ride ride1 = rideRepository.save(createCancelledRide(p1, d1, r1));
        Ride ride2 = rideRepository.save(createCancelledRide(p2, d2, r2));
        Ride ride3 = rideRepository.save(createCancelledRide(p3, d3, r3));

        admin = adminRepository.save(createTestAdmin());
    }

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
        loginPage.login(admin.getEmail(), admin.getPassword());

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
