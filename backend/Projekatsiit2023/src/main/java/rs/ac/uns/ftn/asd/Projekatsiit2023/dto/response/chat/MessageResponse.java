package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.chat;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

import java.time.LocalDateTime;

public class MessageResponse {
    public Long id;
    public String text;
    public Long senderId;
    public UserRole senderRole;
    public LocalDateTime sentAt;
}
