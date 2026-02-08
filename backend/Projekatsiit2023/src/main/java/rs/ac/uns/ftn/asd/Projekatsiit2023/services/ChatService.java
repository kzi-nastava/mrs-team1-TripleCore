package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.chat.ChatResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.chat.MessageResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

import javax.naming.directory.InvalidAttributesException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final AdminRepository adminRepository;

    public ChatService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            PassengerRepository passengerRepository,
            DriverRepository driverRepository,
            AdminRepository adminRepository
    ){
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
    }

    private void saveMessageToChat(Chat chat, User user, String text) {
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

    private Chat createChat(User user) throws InvalidAttributesException {
        if (user.getRole().equals(UserRole.ADMIN)){
            throw new InvalidAttributesException("Admin cannot create a chat");
        }

        Chat chat = new Chat();
        chat.setUser(user);

        chatRepository.save(chat);
        return chat;
    }

    public void saveUserMessage(Long userId, String text) throws InvalidAttributesException {
        User user;
        if (passengerRepository.findById(userId).isPresent())
            user = passengerRepository.findById(userId).get();
        else if (driverRepository.findById(userId).isPresent())
            user = driverRepository.findById(userId).get();
        else
            throw new EntityNotFoundException("User not found");

        Chat chat;

        if (chatRepository.findByUserId(userId).isEmpty()){
            chat = createChat(user);
        } else {
            chat = chatRepository.findByUserId(userId).get();
        }

        saveMessageToChat(chat, user, text);
    }

    public void saveAdminMessage(Long chatId, Long adminId, String text){
        User user;
        if (adminRepository.findById(adminId).isPresent())
            user = adminRepository.findById(adminId).get();
        else
            throw new EntityNotFoundException("User not found");

        Chat chat;
        if (chatRepository.findById(chatId).isPresent())
            chat = chatRepository.findById(chatId).get();
        else{
            throw new EntityNotFoundException("Chat not found");
        }

        saveMessageToChat(chat, user, text);
    }


    public ChatResponse createChatResponse(Chat chat){
        List<Message> messages = messageRepository.findByChatId(chat.getId());

        ChatResponse response = new ChatResponse();
        response.chatId = chat.getId();
        response.userId = chat.getUser().getId();
        for (Message message : messages){
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.id = message.getId();
            messageResponse.senderId = message.getSender().getId();
            messageResponse.senderRole = message.getSenderRole();
            messageResponse.text = message.getText();
            messageResponse.sentAt = message.getSentAt();
            response.messages.add(messageResponse);
        }

        return response;
    }

    public ChatResponse getUserChatResponse(Long userId){
        Chat chat = chatRepository.findByUserId(userId).orElseThrow(
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
}
