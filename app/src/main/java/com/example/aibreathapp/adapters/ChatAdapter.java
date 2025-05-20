// java/com/example/aibreathapp/adapters/ChatAdapter.java
package com.example.aibreathapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aibreathapp.R;
import com.example.aibreathapp.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_bot, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(chatMessage);
        } else if (holder instanceof BotMessageViewHolder) {
            ((BotMessageViewHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
        }
    }

    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        public BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
        }
    }
}