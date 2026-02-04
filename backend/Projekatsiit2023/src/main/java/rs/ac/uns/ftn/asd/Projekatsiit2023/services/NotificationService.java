package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;
    private final String fromEmail = "taxiapp@example.com";

    public NotificationService(
            NotificationRepository notificationRepository,
            JavaMailSender mailSender){
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
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

    private void sendStartRideEmail(Passenger passenger, Ride ride){
        String link = "http://localhost:4200/active-ride-tracking/36";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(ride.getOrderer().getEmail());
        message.setSubject("Ride started");
        message.setText(String.format("Hello %s,\nYour ride from %s to %s has just started!\nYou can track it by clicking the link.\n%s",
                passenger.getFirstName(),
                ride.getRoute().getStartLocation().getAddress(),
                ride.getRoute().getEndLocation().getAddress(),
                link));

        mailSender.send(message);
    }

    private void sendFinishRideEmail(Passenger passenger, Ride ride){
        String link = "http://localhost:4200/login";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(ride.getOrderer().getEmail());
        message.setSubject("Ride finished");
        message.setText(String.format("Hello %s,\nYour ride from %s to %s has just finished!\nYou can rate it by logging in and checking out My Rides.\n%s",
                passenger.getFirstName(),
                ride.getRoute().getStartLocation().getAddress(),
                ride.getRoute().getEndLocation().getAddress(),
                link));

        mailSender.send(message);
    }

    public void rideStartNotifyPassengers(Ride ride){
        List<Passenger> passengers = new ArrayList<>();

        passengers.add(ride.getOrderer());
        if (!ride.getLinkedPassengers().isEmpty()){
            passengers.addAll(ride.getLinkedPassengers());
        }

        for (Passenger passenger : passengers){
            createStartRideNotification(passenger, ride);
            sendStartRideEmail(passenger, ride);
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
            sendFinishRideEmail(passenger, ride);
        }
    }
}
