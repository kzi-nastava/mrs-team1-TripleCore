package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes.pages.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes.OrderRideTestDataFactory.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRideFromFavoriteRoutesTest {

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    FavoriteRouteRepository favoriteRouteRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ActiveVehicleRepository activeVehicleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    Passenger passenger;


    @BeforeEach
    void setupData() {
        passenger = passengerRepository.saveAndFlush(createTestPassenger());
        Route route = routeRepository.saveAndFlush(createTestRoute());
        favoriteRouteRepository.saveAndFlush(new FavoriteRoute(passenger.getId(), route.getId()));

        Vehicle managedVehicle = vehicleRepository.saveAndFlush(createTestVehicle());

        Driver driver = createTestDriver(managedVehicle);
        driverRepository.saveAndFlush(driver);

        // rjesenje zbog perzistencije u bazu i problema sa entity detachom
        transactionTemplate.execute(status -> {
            Vehicle vehicle = vehicleRepository.findById(managedVehicle.getId()).get();
            ActiveVehicle activeVehicle = createActiveVehicle(vehicle);
            return activeVehicleRepository.saveAndFlush(activeVehicle);
        });

        transactionTemplate.execute(status -> {
            Price standardPrice = new Price();
            standardPrice.setVehicleType(VehicleType.STANDARD);
            standardPrice.setPrice(500);
            entityManager.persist(standardPrice);


            Price luxuryPrice = new Price();
            luxuryPrice.setVehicleType(VehicleType.LUXURY);
            luxuryPrice.setPrice(600);
            entityManager.persist(luxuryPrice);

            Price vanPrice = new Price();
            vanPrice.setVehicleType(VehicleType.VAN);
            vanPrice.setPrice(700);
            entityManager.persist(vanPrice);

            return null;
        });
    }

    private WebDriver driver;
    private LoginPage loginPage;
    private PassengerHomePage homePage;
    private FavoriteRoutesPage favoriteRoutesPage;

    @BeforeEach
    void setup(){
        System.setProperty("webdriver.chrome.driver", "src/test/java/rs/ac/uns/ftn/asd/Projekatsiit2023/e2e/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("http://localhost:4200/login");

        loginPage = new LoginPage(driver);
        homePage = new PassengerHomePage(driver);
        favoriteRoutesPage = new FavoriteRoutesPage(driver);
    }

    @Test
    void orderRideFromFavoriteRoutesTest(){
        assertTrue(loginPage.isOpened());
        loginPage.login(passenger.getEmail(), passenger.getPassword());

        assertTrue(homePage.isOpened());
        homePage.clickOrderFromFavoriteRoutes();

        assertTrue(favoriteRoutesPage.isOpened());
        favoriteRoutesPage.selectFirstRoute();

        assertTrue(homePage.isRouteLoaded(), "Route is not loaded");
        homePage.clickOrderRide();

        // cekanje alerta za narucenu voznju
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        // provjera da li je iskocio alert ride ordered
        assertEquals("Ride ordered!", alert.getText());

        // prihvatanje alerta
        alert.accept();

    }

    @AfterAll
    void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}
