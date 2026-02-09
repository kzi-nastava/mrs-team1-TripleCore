package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.chat.SaveAdminMessageRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.chat.SaveUserMessageRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.chat.ChatResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(
            ChatService chatService
    ){
        this.chatService = chatService;
    }

    @PostMapping("/save-user-message")
    public ResponseEntity<?> saveUserMessage(@Valid @RequestBody SaveUserMessageRequest request){
        try{
            chatService.saveUserMessage(request.senderId, request.text);
            return ResponseEntity.ok("Message saved successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/save-admin-message")
    public ResponseEntity<?> saveAdminMessage(@Valid @RequestBody SaveAdminMessageRequest request){
        try{
            chatService.saveAdminMessage(request.chatId, request.senderId, request.text);
            return ResponseEntity.ok("Message saved successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserChat(@PathVariable("id") Long id){
        try{
            ChatResponse response = chatService.getUserChatResponse(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllChats(){
        try{
            List<ChatResponse> response = chatService.getAllChatResponses();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
