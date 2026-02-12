package model;

import java.time.LocalDateTime;

import enums.UserRole;

public class MessageResponse {
    public String text;
    public Long senderId;
    public UserRole senderRole;
    public LocalDateTime sentAt;
}
