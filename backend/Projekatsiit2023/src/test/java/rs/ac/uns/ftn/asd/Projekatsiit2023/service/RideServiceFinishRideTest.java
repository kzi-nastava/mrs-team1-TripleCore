package rs.ac.uns.ftn.asd.Projekatsiit2023.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepositoryTest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.NotificationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceFinishRideTest {

    @Mock
    RideRepository rideRepository;

    @Mock
    DriverRepository driverRepository;

    @Mock
    ActiveVehicleRepositoryTest activeVehicleRepository;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    RideService rideService;

    @Test
    void finishRide_shouldThrowException_whenRideNotFound(){
        // setup
        Long INVALID_RIDE_ID = 100L;
        when(rideRepository.findById(INVALID_RIDE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rideService.finishRide(INVALID_RIDE_ID));

        verifyNoInteractions(activeVehicleRepository);
    }

    @Test
    void finishRide_shouldThrowException_whenRideNotInProgress(){
        // setup
        Long VALID_RIDE_ID = 1L;
        Ride ride = new Ride();
        ride.setId(VALID_RIDE_ID);
        ride.setStatus(RideStatus.FINISHED);

        when(rideRepository.findById(VALID_RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(IllegalStateException.class,
                () -> rideService.finishRide(VALID_RIDE_ID));

        verifyNoInteractions(activeVehicleRepository);
    }

    @Test
    void finishRide_shouldThrowException_whenVehicleNotInActiveVehicles(){
        Long VALID_RIDE_ID = 1L;
        Ride ride = new Ride();
        ride.setId(VALID_RIDE_ID);
        ride.setStatus(RideStatus.IN_PROGRESS);

        Long VEHICLE_ID = 2L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(VEHICLE_ID);

        Driver driver = new Driver();
        driver.setId(3L);

        driver.setVehicle(vehicle);
        ride.setDriver(driver);

        when(rideRepository.findById(VALID_RIDE_ID)).thenReturn(Optional.of(ride));
        when(activeVehicleRepository.findById(VEHICLE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rideService.finishRide(VALID_RIDE_ID)
        );

        verifyNoInteractions(notificationService);
    }

    @Test
    void finishRide_shouldThrowException_whenActiveVehicleNotLinkedToRide() {

        Long VALID_RIDE_ID = 1L;

        Ride ride = new Ride();
        ride.setId(VALID_RIDE_ID);
        ride.setStatus(RideStatus.IN_PROGRESS);

        Driver driver = new Driver();

        Long VALID_VEHICLE_ID = 100L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(VALID_VEHICLE_ID);
        driver.setVehicle(vehicle);
        ride.setDriver(driver);

        ActiveVehicle av = new ActiveVehicle();
        av.setVehicle(vehicle);
        av.setRide(null);

        when(rideRepository.findById(VALID_RIDE_ID)).thenReturn(Optional.of(ride));
        when(activeVehicleRepository.findById(VALID_VEHICLE_ID)).thenReturn(Optional.of(av));

        assertThrows(IllegalStateException.class,
                () -> rideService.finishRide(VALID_RIDE_ID));

        verifyNoInteractions(notificationService);
    }

    @Test
    void finishRide_shouldThrow_whenActiveVehicleLinkedToDifferentRide() {

        Long VALID_RIDE_ID = 1L;

        Ride ride = new Ride();
        ride.setId(VALID_RIDE_ID);
        ride.setStatus(RideStatus.IN_PROGRESS);

        Long DIFFERENT_RIDE_ID = 2L;
        Ride differentRide = new Ride();
        differentRide.setId(DIFFERENT_RIDE_ID);

        Driver driver = new Driver();

        Long VALID_VEHICLE_ID = 100L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(VALID_VEHICLE_ID);
        driver.setVehicle(vehicle);
        ride.setDriver(driver);

        ActiveVehicle av = new ActiveVehicle();
        av.setVehicle(vehicle);
        av.setRide(differentRide);

        when(rideRepository.findById(VALID_RIDE_ID)).thenReturn(Optional.of(ride));
        when(activeVehicleRepository.findById(VALID_VEHICLE_ID)).thenReturn(Optional.of(av));

        assertThrows(IllegalStateException.class,
                () -> rideService.finishRide(VALID_RIDE_ID));
    }

    @Test
    void finishRide_shouldFinishSuccessfully() {
        Long VALID_RIDE_ID = 1L;

        Ride ride = new Ride();
        ride.setId(VALID_RIDE_ID);
        ride.setStatus(RideStatus.IN_PROGRESS);

        Driver driver = new Driver();

        Long VALID_VEHICLE_ID = 100L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(VALID_VEHICLE_ID);
        driver.setVehicle(vehicle);
        ride.setDriver(driver);

        ActiveVehicle av = new ActiveVehicle();
        av.setVehicle(vehicle);
        av.setRide(ride);
        av.setAvailable(false);
        Location location = new Location();
        location.setLatitude(19.0);
        location.setLongitude(42.0);
        av.setLocation(location);

        when(rideRepository.findById(VALID_RIDE_ID)).thenReturn(Optional.of(ride));
        when(activeVehicleRepository.findById(VALID_VEHICLE_ID)).thenReturn(Optional.of(av));

        rideService.finishRide(VALID_RIDE_ID);

        assertEquals(RideStatus.FINISHED, ride.getStatus());
        assertNotNull(ride.getEndTime(), "EndTime should be set");
        assertEquals(av.getLocation(), ride.getActualEndLocation(), "ActualEndLocation should be set correctly");

        assertTrue(driver.isAvailable(), "Driver should be available");
        assertTrue(driver.isCurrentlyWorking(), "Driver should be marked as currently working");

        assertTrue(av.isAvailable(), "ActiveVehicle should be available");
        assertNull(av.getRide(), "ActiveVehicle's ride should be null");

        verify(rideRepository).save(ride);
        verify(driverRepository).save(driver);
        verify(activeVehicleRepository).save(av);
        verify(notificationService).rideFinishNotifyPassengers(ride);

        verifyNoMoreInteractions(rideRepository, driverRepository, activeVehicleRepository, notificationService);
    }

}
