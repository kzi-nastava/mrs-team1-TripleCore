package model;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.UserRole;

public class MessageResponse implements Serializable {
    public String text;
    public Long senderId;
    public UserRole senderRole;
    public String sentAt;
}
