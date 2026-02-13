package model;

import java.time.LocalDateTime;

public class NotificationResponse {
    public Long id;
    public Long recipientId;
    public String title;
    public String message;
    public String link;
    public String time;
    public boolean seen;
}
