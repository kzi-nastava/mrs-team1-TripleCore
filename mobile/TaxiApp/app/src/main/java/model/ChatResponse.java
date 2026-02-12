package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatResponse implements Serializable {
    public Long chatId;
    public Long userId;
    public String userName;
    public List<MessageResponse> messages = new ArrayList<>();
}
