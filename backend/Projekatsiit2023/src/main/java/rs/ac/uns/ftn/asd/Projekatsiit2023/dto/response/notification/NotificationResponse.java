package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long recipientId;
    private String title;
    private String message;
    private String link;
    private LocalDateTime time;
    private boolean seen;
}
