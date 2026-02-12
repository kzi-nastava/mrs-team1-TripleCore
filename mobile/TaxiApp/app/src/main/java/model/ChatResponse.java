package model;

import java.util.ArrayList;
import java.util.List;

public class ChatResponse {
    public Long chatId;
    public Long userId;
    public String userName;
    public List<MessageResponse> messages = new ArrayList<>();
}
