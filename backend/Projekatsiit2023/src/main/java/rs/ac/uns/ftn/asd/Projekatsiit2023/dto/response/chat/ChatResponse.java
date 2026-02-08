package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatResponse {
    public Long chatId;
    public Long userId;
    public List<MessageResponse> messages = new ArrayList<>();
}
