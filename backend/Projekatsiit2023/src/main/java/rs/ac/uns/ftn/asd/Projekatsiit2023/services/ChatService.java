package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ChatResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Chat;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Message;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ChatRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;

import javax.naming.directory.InvalidAttributesException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final PassengerRepository passengerRepository;

    public ChatService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            PassengerRepository passengerRepository
    ){
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.passengerRepository = passengerRepository;
    }

    public void saveMessage(User user, String text) throws InvalidAttributesException {

        Chat chat;

        // checks if the chat exists and if not creates it
        // admin cannot be the one to create the chat
        if (chatRepository.findByUserId(user.getId()).isEmpty()){
            if (user.getRole().equals(UserRole.ADMIN)){
                throw new InvalidAttributesException("Admin cannot create chats");
            }

            chat = new Chat();
            chat.setUser(user);
        } else {
            chat = chatRepository.findByUserId(user.getId()).get();
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(user);
        message.setSenderRole(user.getRole());
        message.setText(text);
        message.setSentAt(LocalDateTime.now());

        chat.setLastMessageAt(message.getSentAt());

        chatRepository.save(chat);
        messageRepository.save(message);

    }

    public ChatResponse createChatResponse(Chat chat){
        List<Message> messages = messageRepository.findByChatId(chat.getId());

        ChatResponse response = new ChatResponse();
        response.chatId = chat.getId();
        response.userId = chat.getUser().getId();
        response.messages = messages;

        return response;
    }

    public ChatResponse getChatResponse(Long chatId){
        Chat chat = chatRepository.findById(chatId).orElseThrow(
                () -> new EntityNotFoundException("Chat not found")
        );

        return createChatResponse(chat);
    }

    public List<ChatResponse> getAllChatResponses(){
        List<Chat> chats = chatRepository.findAll();
        List<ChatResponse> responses = new ArrayList<>();
        for (Chat chat : chats){
            responses.add(createChatResponse(chat));
        }

        return responses;
    }

    public void createTest() throws InvalidAttributesException {

        Passenger passenger = passengerRepository.findById(2L).get();
        saveMessage(passenger, "I am testing the feature");

    }
}
