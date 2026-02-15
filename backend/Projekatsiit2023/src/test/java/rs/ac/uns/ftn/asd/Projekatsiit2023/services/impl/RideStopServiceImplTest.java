package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideStopRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class  RideStopServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private ActiveVehicleRepository activeVehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private RideStopServiceImpl rideStopService;

    private Ride testRide;
    private Driver testDriver;
    private Vehicle testVehicle;
    private ActiveVehicle testActiveVehicle;
    private Route testRoute;
    private RideStopRequest testRequest;
    private Passenger testPassenger;

    @BeforeEach
    void setUp() {
        testPassenger = createPassenger();
        testDriver = createDriver();
        testVehicle = createVehicle();
        testRoute = createRoute();
        testActiveVehicle = createActiveVehicle();
        testRide = createRide();
        testRequest = createRideStopRequest();

        testDriver.setVehicle(testVehicle);
        testRide.setDriver(testDriver);
        testRide.setOrderer(testPassenger);
        testRide.setRoute(testRoute);
        testActiveVehicle.setVehicle(testVehicle);
        testActiveVehicle.setRide(testRide);
    }

    @Test
    void stopRideTest_whenRideNotFound_thenThrowRuntimeException() {
        // Arrange
        Long rideId = 999L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> rideStopService.stopRide(rideId, testRequest));

        assertEquals("Ride with ID " + rideId + " not found", exception.getMessage());
        // verify that rideRepository.findById was called once with the correct rideId
        verify(rideRepository, times(1)).findById(rideId);
        // verify that no interactions were made with other repositories
        verifyNoInteractions(activeVehicleRepository, driverRepository);
    }

    @Test
    void stopRideTest_whenRideIsNotInProgress_thenThrowRuntimeException() {
        // Arrange
        Long rideId = 1L;

        RideStatus[] nonInProgressStatuses = {
                RideStatus.ACCEPTED,
                RideStatus.REQUESTED,
                RideStatus.FINISHED,
                RideStatus.CANCELLED,
                RideStatus.REJECTED
        };

        for (RideStatus status : nonInProgressStatuses) {
            testRide.setStatus(status);
            when(rideRepository.findById(rideId)).thenReturn(Optional.of(testRide));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> rideStopService.stopRide(rideId, testRequest));

            assertEquals("Ride is not in progress. Only rides in progress can be stopped.",
                    exception.getMessage());

            verify(rideRepository, times(1)).findById(rideId);
            verifyNoMoreInteractions(activeVehicleRepository, driverRepository);

            // Reset mocks for the next iteration
            reset(rideRepository);
        }
    }

    @Test
    void calculateNewDistanceTest_whenCalled_returns90PercentOfOriginalDistance() {
        // Arrange
        Ride ride = testRide; // 10 km
        double newLat = 45.2553;
        double newLng = 19.8452;

        // Act
        double result = rideStopService.calculateNewDistance(ride, newLat, newLng);

        // Assert
        double expected = 9.0; // 10km * 0.9 = 9km
        assertEquals(expected, result, 0.001);
    }

    @Test
    void recalculatePriceTest_whenCalled_recalculatesPriceBasedOnNewDistance() {
        // Arrange
        Ride ride = testRide; // price = 500.0, originalDistance = 10km
        double originalDistance = 10.0;
        double newDistance = 9.0;

        // Act
        double result = rideStopService.recalculatePrice(ride, originalDistance, newDistance);

        // Assert
        double expected = 450.0; // (500/10) * 9 = 450
        assertEquals(expected, result, 0.001);
    }

    @Test
    void recalculatePriceTest_whenOriginalPriceIsNull_usesDefaultPrice() {
        // Arrange
        testRide.setPrice(null);
        double originalDistance = 10.0;
        double newDistance = 9.0;

        // Act
        double result = rideStopService.recalculatePrice(testRide, originalDistance, newDistance);

        // Assert
        double expected = 180.0; // (200/10) * 9 = 180
        assertEquals(expected, result, 0.001);
    }

    @Test
    void stopRideTest_whenRideIsSuccessfullyStopped_thenPriceAndDistanceAreRecalculated() {
        // Arrange
        when(rideRepository.findById(1L)).thenReturn(Optional.of(testRide));
        when(activeVehicleRepository.findById(any())).thenReturn(Optional.of(testActiveVehicle));

        // Act
        RideStopResponse response = rideStopService.stopRide(1L, testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(450.0, response.getNewTotalPrice());
        assertEquals(9.0, response.getNewDistance());
    }

    @Test
    void stopRideTest_whenRideIsStopped_thenRideDetailsAreUpdated() {
        // Arrange
        Long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(testRide));
        when(activeVehicleRepository.findById(any())).thenReturn(Optional.of(testActiveVehicle));

        // Act
        RideStopResponse response = rideStopService.stopRide(rideId, testRequest);

        // Assert - check that actual end location, status and end time are updated correctly
        assertNotNull(testRide.getActualEndLocation());
        assertEquals(testRequest.getLatitude(), testRide.getActualEndLocation().getLatitude());
        assertEquals(testRequest.getLongitude(), testRide.getActualEndLocation().getLongitude());
        assertEquals(testRequest.getAddress(), testRide.getActualEndLocation().getAddress());
        assertEquals(RideStatus.FINISHED, testRide.getStatus());
        assertNotNull(testRide.getEndTime());

        verify(rideRepository, times(1)).save(testRide);
    }

    @Test
    void stopRideTest_whenRideIsStopped_thenActiveVehicleIsUpdated() {
        // Arrange
        Long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(testRide));
        when(activeVehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testActiveVehicle));

        // Act
        RideStopResponse response = rideStopService.stopRide(rideId, testRequest);

        // Assert - check that active vehicle's ride is set to null and availability is set to true
        assertNull(testActiveVehicle.getRide());
        assertTrue(testActiveVehicle.isAvailable());

        verify(activeVehicleRepository, times(1)).save(testActiveVehicle);
    }

    @Test
    void stopRideTest_whenRideIsStopped_thenDriverIsUpdated() {
        // Arrange
        Long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(testRide));
        when(activeVehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testActiveVehicle));

        // Act
        RideStopResponse response = rideStopService.stopRide(rideId, testRequest);

        // Assert - check that driver's availability is set to true and currentlyWorking is set to false
        assertTrue(testDriver.isAvailable());
        assertFalse(testDriver.isCurrentlyWorking());

        verify(driverRepository, times(1)).save(testDriver);
    }

    @Test
    void stopRideTest_whenRideIsStopped_thenReturnsCorrectResponse() {
        // Arrange
        Long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(testRide));
        when(activeVehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testActiveVehicle));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        // Act
        RideStopResponse response = rideStopService.stopRide(rideId, testRequest);

        // Assert - check response fields
        assertAll(
                () -> assertTrue(response.isSuccess()),
                () -> assertEquals("Ride #" + rideId + " stopped successfully at " + testRequest.getAddress(),
                        response.getMessage()),
                () -> assertEquals(450.0, response.getNewTotalPrice()),
                () -> assertEquals(9.0, response.getNewDistance()),
                () -> assertNotNull(response.getStopTime()),
                () -> assertEquals(testRequest.getAddress(), response.getFinalAddress())
        );
    }

    // helper methods for creating entities

    private Passenger createPassenger() {
        Passenger p = new Passenger();
        p.setEmail("passenger@example.com");
        p.setPassword("password");
        p.setFirstName("Pera");
        p.setLastName("Peric");
        p.setAddress("Bulevar oslobođenja 1, Novi Sad");
        p.setPhone("123456789");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);
        return p;
    }

    private Driver createDriver() {
        Driver d = new Driver();
        d.setEmail("driver@example.com");
        d.setPassword("password");
        d.setFirstName("Mika");
        d.setLastName("Mikic");
        d.setAddress("Address");
        d.setPhone("123");
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now());
        d.setRole(UserRole.DRIVER);
        d.setCurrentlyWorking(true);
        d.setWorkingHoursToday(4.5);
        d.setAvailable(false);
        d.setLastWorkStart(LocalDateTime.now().minusHours(4));
        d.setVehicle(null);
        return d;
    }

    private Vehicle createVehicle() {
        Vehicle v = new Vehicle();
        v.setBrand("Tesla");
        v.setModel("Model 3");
        v.setPlateNumber("NS123456");
        v.setSeatNumber(4);
        v.setBabyFriendly(true);
        v.setPetFriendly(false);
        return v;
    }

    private Route createRoute() {
        Route r = new Route();

        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("Bulevar oslobođenja 1, Novi Sad");
        r.setStartLocation(start);

        Location end = new Location();
        end.setLatitude(45.2553);
        end.setLongitude(19.8452);
        end.setAddress("Trg slobode 1, Novi Sad");
        r.setEndLocation(end);

        r.setEstimatedDistanceMeters(10000.0); // 10km
        r.setEstimatedDurationSeconds(900L); // 15 minuta

        return r;
    }

    private ActiveVehicle createActiveVehicle() {
        ActiveVehicle av = new ActiveVehicle();

        Location currentLocation = new Location();
        currentLocation.setLatitude(45.2600);
        currentLocation.setLongitude(19.8400);
        currentLocation.setAddress("Uspenska 5, Novi Sad");
        av.setLocation(currentLocation);

        av.setAvailable(false);
        av.setRouteCoordinates("[[45.2671,19.8335],[45.2600,19.8400],[45.2553,19.8452]]");
        av.setRouteIndex(1);
        return av;
    }

    private Ride createRide() {
        Ride r = new Ride();
        r.setStartTime(LocalDateTime.now().minusMinutes(10));
        r.setPrice(500.0);
        r.setBabyFriendly(false);
        r.setPetFriendly(false);
        r.setStatus(RideStatus.IN_PROGRESS);
        r.setPanic(false);
        r.setInconsistencies(null);
        return r;
    }

    private RideStopRequest createRideStopRequest() {
        RideStopRequest r = new RideStopRequest();
        r.setLatitude(45.2553);
        r.setLongitude(19.8452);
        r.setAddress("Trg slobode 1, Novi Sad");
        return r;
    }
}