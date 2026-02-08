package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Message;

import java.util.List;

public class ChatResponse {
    public Long chatId;
    public Long userId;
    public List<Message> messages;
}
