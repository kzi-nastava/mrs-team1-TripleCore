package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.notification.NotificationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.NotificationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PassengerService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<?> getPassengerNotifications(@PathVariable("id") Long id){
        try{
            List<NotificationResponse> responses = notificationService.getAllPassengerNotifications(id);
            return ResponseEntity.ok(responses);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong with loading the notifications");
        }
    }
}
