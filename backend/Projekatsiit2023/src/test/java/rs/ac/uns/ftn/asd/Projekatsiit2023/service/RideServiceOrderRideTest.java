package rs.ac.uns.ftn.asd.Projekatsiit2023.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceOrderRideTest {
    @Mock
    RideRepository rideRepository;

    @Mock
    DriverRepository driverRepository;

    @Mock
    PassengerRepository passengerRepository;

    @Mock
    RouteRepository routeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    VehicleService vehicleService;

    @Mock
    NotificationService notificationService;

    @Mock
    PricingService pricingService;

    @Mock
    RouteServiceImpl routeService;

    @Mock
    ActiveVehicleRepository activeVehicleRepository;

    @Mock
    UserBlockRepository userBlockRepository;

    @InjectMocks
    RideService rideService;

    @Test
    void orderRide_shouldThrowException_whenNotEmail(){
        RideRequest request = new RideRequest();

        assertThrows(IllegalStateException.class, () -> rideService.orderRide(request, null));
        verifyNoInteractions(userRepository, driverRepository, vehicleService);
    }

    @Test
    void orderRide_shouldThrow_whenUserNotPassenger(){
        RideRequest request = new RideRequest();

        Driver driver = new Driver();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(driver));

        assertThrows(IllegalStateException.class, () -> rideService.orderRide(request, "test@email.com"));

        verifyNoInteractions(driverRepository, vehicleService, routeRepository);
    }

    @Test
    void orderRide_shouldThrow_whenPassengerBlocked(){
        RideRequest request = new RideRequest();

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setAccountBlocked(true);

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(passenger));
        when(userBlockRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> rideService.orderRide(request, "test@email.com"));
        verifyNoInteractions(driverRepository, vehicleService);
    }

    @Test
    void orderRide_shouldThrow_whenNoDriverAvailable(){
        RideRequest request = new RideRequest();
        request.setStartLocation(new Location());
        request.setEndLocation(new Location());
        request.setVehicleType(VehicleType.STANDARD);

        Passenger passenger = new Passenger();
        passenger.setAccountBlocked(false);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(passenger));
        when(driverRepository.findAllByVehicle_Type(any())).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> rideService.orderRide(request, "test@email.com"));
        verify(notificationService).notifyRideRejected(passenger);
    }

    @Test
    void orderRide_shouldThrow_whenScheduledTooFar(){
        RideRequest request = new RideRequest();
        request.setStartTime(LocalDateTime.now().plusHours(10));

        request.setStartLocation(new Location());
        request.setEndLocation(new Location());

        Passenger passenger = new Passenger();
        passenger.setAccountBlocked(false);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(passenger));
        assertThrows(IllegalStateException.class, () -> rideService.orderRide(request, "test@email.com"));
        verifyNoInteractions(driverRepository, vehicleService);
    }

    @Test
    void orderRide_shouldSucceed(){
        RideRequest request = new RideRequest();

        Location start = new Location();
        start.setAddress("Pocetna adresa");
        start.setLatitude(1.0);
        start.setLongitude(1.0);

        Location end = new Location();
        end.setAddress("Krajnja adresa");
        end.setLatitude(2.0);
        end.setLongitude(2.0);

        request.setStartLocation(start);
        request.setEndLocation(end);
        request.setVehicleType(VehicleType.STANDARD);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setAccountBlocked(false);

        Driver driver = new Driver();
        driver.setId(2L);
        driver.setWorkingHoursToday(0);
        driver.setFirstName("TestIme");
        driver.setLastName("TestPrezime");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(10L);
        driver.setVehicle(vehicle);

        ActiveVehicle av = new ActiveVehicle();
        av.setVehicle(vehicle);
        av.setAvailable(true);
        av.setRide(null);

        av.setVehicleId(10L);

        doNothing().when(vehicleService).setRouteForActiveVehicle(any(), anyList());
        doNothing().when(vehicleService).setLocation(any(), any());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(passenger));
        when(driverRepository.findAllByVehicle_Type(any())).thenReturn(List.of(driver));
        when(vehicleService.getActiveVehicle(anyLong())).thenReturn(av);
        when(routeService.calculateDistanceBetweenTwoPoints(any(), any())).thenReturn(100);
        when(routeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(rideRepository.save(any())).thenAnswer(i -> {
            Ride ride = (Ride) i.getArguments()[0];
            ride.setId(123L);
            return ride;
        });
        when(pricingService.caclulatePriceForRide(any())).thenReturn(100.0);

        ActiveRideVehicleDetailsResponse tracking = new ActiveRideVehicleDetailsResponse();
        tracking.setEstimatedTime(100L);

        when(vehicleService.getRideTrackingResponse(any())).thenReturn(tracking);

        RideResponse response = rideService.orderRide(request, "test@email.com");

        assertNotNull(response);
        assertNotNull(response.getRideId());
        assertEquals(123L, response.getRideId());

        verify(rideRepository).save(any());
        verify(notificationService).notifyRideAccepted(any());
        verify(notificationService).notifyDriverNewRide(any());

    }


}
