package service;

import java.util.List;

import model.ChatResponse;
import network.RetrofitClient;
import retrofit2.Call;
import service.api.ApiService;

public class ChatService {

    private static ChatService instance;

    private ApiService apiService;

    private ChatService() {
        apiService = RetrofitClient.getApiService();
    }

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }
        return instance;
    }

    public Call<List<ChatResponse>> getAllChats() {
        return apiService.getChatList();
    }
}
