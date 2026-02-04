package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    private Notification createStartRideNotification(Passenger passenger, Ride ride){
        Notification notification = new Notification();
        notification.setRecipient(passenger);
        notification.setLink(String.format("ride-tracking:%d", ride.getId()));
        notification.setSeen(false);
        notification.setMessage(
                String.format("Hello %s,\nYour ride from %s to %s just started!\nYou can track it here by clicking the link.\n",
                        passenger.getFirstName(),
                        ride.getRoute().getStartLocation().getAddress(),
                        ride.getRoute().getEndLocation().getAddress())
        );
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);
        return notification;
    }

    private Notification createFinishRideNotification(Passenger passenger, Ride ride){
        Notification notification = new Notification();
        notification.setRecipient(passenger);
        notification.setLink(String.format("review:%d", ride.getId()));
        notification.setSeen(false);
        notification.setMessage(
                String.format("Hello %s,\nYour ride from %s to %s just finished!\nYou can rate it by clicking the link.\n",
                        passenger.getFirstName(),
                        ride.getRoute().getStartLocation().getAddress(),
                        ride.getRoute().getEndLocation().getAddress())
        );
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);
        return notification;
    }

    private void sendStartRideEmail(Passenger passenger, Ride ride){}
    private void sendFinishRideEmail(Passenger passenger, Ride ride){}

    public void rideStartNotifyPassengers(Ride ride){
        List<Passenger> passengers = new ArrayList<>();

        passengers.add(ride.getOrderer());
        if (!ride.getLinkedPassengers().isEmpty()){
            passengers.addAll(ride.getLinkedPassengers());
        }

        for (Passenger passenger : passengers){
            createStartRideNotification(passenger, ride);
        }
    }

    public void rideFinishNotifyPassengers(Ride ride){
        List<Passenger> passengers = new ArrayList<>();

        passengers.add(ride.getOrderer());
        if (!ride.getLinkedPassengers().isEmpty()){
            passengers.addAll(ride.getLinkedPassengers());
        }

        for (Passenger passenger : passengers){
            createFinishRideNotification(passenger, ride);
        }
    }


}
