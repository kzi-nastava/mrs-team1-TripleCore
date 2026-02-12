package service;

import java.util.List;

import model.ChatResponse;
import model.SaveAdminMessageRequest;
import model.SaveUserMessageRequest;
import network.RetrofitClient;
import retrofit2.Call;
import service.api.ApiService;

public class ChatService {

    private static ChatService instance;
    private final ApiService apiService;

    private ChatService() {
        apiService = RetrofitClient.getApiService();
    }

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }
        return instance;
    }

    // ------------------ API calls ------------------

    public Call<List<ChatResponse>> getAllChats() {
        return apiService.getChatList(); // pretpostavljam da ovo vraća /all endpoint
    }

    public Call<Void> saveUserMessage(Long senderId, String text) {
        SaveUserMessageRequest request = new SaveUserMessageRequest();
        request.senderId = senderId;
        request.text = text;
        return apiService.saveUserMessage(request);
    }

    public Call<Void> saveAdminMessage(Long chatId, Long senderId, String text) {
        SaveAdminMessageRequest request = new SaveAdminMessageRequest();
        request.chatId = chatId;
        request.senderId = senderId;
        request.text = text;
        return apiService.saveAdminMessage(request);
    }

    public Call<ChatResponse> getUserChat(Long userId) {
        return apiService.getUserChat(userId);
    }
}
