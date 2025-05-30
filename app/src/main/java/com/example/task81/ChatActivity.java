package com.example.task81;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task81.api.ChatService;
import com.example.task81.api.RetrofitClient;
import com.example.task81.model.Message;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "User";
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + username);

        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(view -> {
            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            } else {
                sendMessage(text);
                etMessage.setText("");
            }
        });
    }

    private void sendMessage(String text) {
        Message userMsg = new Message(text, true);
        messageList.add(userMsg);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        JsonObject body = new JsonObject();
        body.addProperty("message", text);

        ChatService service = RetrofitClient.getInstance().getChatService();
        service.sendMessage(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().get("response").getAsString();
                    Message botMsg = new Message(reply, false);
                    messageList.add(botMsg);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                } else {
                    showError("Failed to get response from server");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
