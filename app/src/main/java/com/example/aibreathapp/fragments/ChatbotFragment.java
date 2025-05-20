// java/com/example/aibreathapp/fragments/ChatbotFragment.java
package com.example.aibreathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aibreathapp.R;
import com.example.aibreathapp.adapters.ChatAdapter;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.ChatMessage;
import com.example.aibreathapp.models.ChatbotRequest;
import com.example.aibreathapp.models.ChatbotResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private View loadingIndicator;

    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        // Initialize UI components
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);

        // Set up RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Add initial welcome message
        if (chatMessages.isEmpty()) {
            chatMessages.add(new ChatMessage(
                    "Hi there! I'm a medical chatbot. How can I help you today?",
                    ChatMessage.TYPE_BOT));
            chatAdapter.notifyItemInserted(0);
        }

        // Set up send button click listener
        sendButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        // Add user message to chat
        chatMessages.add(new ChatMessage(messageText, ChatMessage.TYPE_USER));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);

        // Clear input field
        messageEditText.setText("");

        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);

        // Create request
        ChatbotRequest request = new ChatbotRequest(messageText);

        // Make API call
        apiService.getChatbotResponse(request).enqueue(new Callback<ChatbotResponse>() {
            @Override
            public void onResponse(Call<ChatbotResponse> call, Response<ChatbotResponse> response) {
                // Hide loading indicator
                loadingIndicator.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ChatbotResponse chatbotResponse = response.body();
                    String botMessage = chatbotResponse.getResponse();

                    if (botMessage != null && !botMessage.isEmpty()) {
                        // Add bot response to chat
                        chatMessages.add(new ChatMessage(botMessage, ChatMessage.TYPE_BOT));
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                    } else {
                        handleError("No response from chatbot");
                    }
                } else {
                    handleError("Failed to get response");
                }
            }

            @Override
            public void onFailure(Call<ChatbotResponse> call, Throwable t) {
                // Hide loading indicator
                loadingIndicator.setVisibility(View.GONE);
                handleError("Network error: " + t.getMessage());
            }
        });
    }

    private void handleError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        chatMessages.add(new ChatMessage(
                "Sorry, I'm having trouble responding right now. Please try again later.",
                ChatMessage.TYPE_BOT));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }
}