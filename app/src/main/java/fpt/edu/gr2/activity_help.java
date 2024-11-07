package fpt.edu.gr2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import fpt.edu.gr2.Adapters.ChatAdapter;
import fpt.edu.gr2.Helpers.SendMessageInBg;
import fpt.edu.gr2.Interfaces.BotReply;
import fpt.edu.gr2.Models.Message;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class activity_help extends AppCompatActivity implements BotReply {

    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<Message> messageList = new ArrayList<>();
    EditText editMessage;
    ImageButton btnSend;

    // dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "mainactivity";

    // Predefined Q&A with keywords
    private Map<String, String> predefinedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);
        chatView = findViewById(R.id.chatView);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        chatAdapter = new ChatAdapter(messageList, this);
        chatView.setAdapter(chatAdapter);

        // Initialize predefined questions and answers
        initializePredefinedQuestions();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));
                    editMessage.setText("");
                    handleUserMessage(message);
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(activity_help.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot();
    }

    private void initializePredefinedQuestions() {
        predefinedQuestions = new HashMap<>();
        predefinedQuestions.put("hello", "Hello! How can I assist you today?");
        predefinedQuestions.put("hi", "Hi there! How can I help you?");
        predefinedQuestions.put("how are you", "I'm just a bot, but I'm here to help!");
        predefinedQuestions.put("what can you do", "I can answer questions, provide information, and assist with various tasks.");
        predefinedQuestions.put("what is your name", "I'm a chatbot of MyMoney, but you can call me Jennie Kim, so I don't have a name.");
        predefinedQuestions.put("bye", "Goodbye! Have a great day!");
        predefinedQuestions.put("thanks", "You're welcome! If you have more questions, feel free to ask.");
        predefinedQuestions.put("help", "Sure, I'm here to help. What do you need assistance with?");
        predefinedQuestions.put("how to add a category", "To add a new category, go to the Category Management screen and tap on the Add Category button.");
        predefinedQuestions.put("how to add a transaction", "To add a new transaction, go to the Add Transaction screen, enter the details, and select a category.");
        predefinedQuestions.put("how to edit a transaction", "To edit a transaction, tap on the transaction in the list, and you’ll see the Edit option.");
        predefinedQuestions.put("how to delete a transaction", "You can delete a transaction by tapping on the Delete icon next to it in the Transaction List screen.");
        predefinedQuestions.put("how to add transaction", "To add a new transaction, go to the Add Transaction screen, enter the details, and select a category.");
        predefinedQuestions.put("how to edit transaction", "To edit a transaction, tap on the transaction in the list, and you’ll see the Edit option.");
        predefinedQuestions.put("how to delete transaction", "You can delete a transaction by tapping on the Delete icon next to it in the Transaction List screen.");
        predefinedQuestions.put("how to enable notifications", "Go to the Settings screen and select Notification Preferences to enable or disable notifications.");
        predefinedQuestions.put("how to contact support", "For support, go to the Help screen and send us a message or visit our website.");
        predefinedQuestions.put("how to manage categories", "You can manage categories in the Category Management screen. You can add, edit, or delete categories as needed.");
        predefinedQuestions.put("how to adjust settings", "To adjust app settings, go to the Settings screen. Here you can modify preferences, notification settings, and language.");
        predefinedQuestions.put("how to update profile", "To update your profile, navigate to the Profile screen from the Settings menu and edit your information.");
        predefinedQuestions.put("how to change language", "You can change the app language in the Settings screen under Language Preferences.");
        predefinedQuestions.put("add transaction", "To add a new transaction, go to the Add Transaction screen, enter the details, and select a category.");
        predefinedQuestions.put("edit transaction", "To edit a transaction, tap on the transaction in the list, and you’ll see the Edit option.");
        predefinedQuestions.put("delete transaction", "You can delete a transaction by tapping on the Delete icon next to it in the Transaction List screen.");
        predefinedQuestions.put("notification", "Go to the Settings screen and select Notification Preferences to enable or disable notifications.");
        predefinedQuestions.put("support", "For support, go to the Help screen and send us a message or visit our website.");
        predefinedQuestions.put("categories", "You can manage categories in the Category Management screen. You can add, edit, or delete categories as needed.");
        predefinedQuestions.put("settings", "To adjust app settings, go to the Settings screen. Here you can modify preferences, notification settings, and language.");
        predefinedQuestions.put("profile", "To update your profile, navigate to the Profile screen from the Settings menu and edit your information.");
        predefinedQuestions.put("language", "You can change the app language in the Settings screen under Language Preferences.");
        predefinedQuestions.put("add", "To add a new transaction, go to the Add Transaction screen, enter the details, and select a category.");
        predefinedQuestions.put("statistics", "The Statistics screen shows a summary of your expenses and income across various categories.");
        predefinedQuestions.put("help me", "Sure, I'm here to help. What do you need assistance with?");
        predefinedQuestions.put("please help me", "Sure, I'm here to help. What do you need assistance with?");
        predefinedQuestions.put("help me please", "Sure, I'm here to help. What do you need assistance with?");
    }

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.dialogflow_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void handleUserMessage(String message) {
        boolean responseFound = false;
        message = message.toLowerCase(); // Convert the message to lowercase for easier matching

        // Loop through predefined questions and check for keywords in the user message
        for (Map.Entry<String, String> entry : predefinedQuestions.entrySet()) {
            String keyword = entry.getKey().toLowerCase(); // Convert keyword to lowercase
            String botReply = entry.getValue();

            // Check if the user's message contains the keyword
            if (message.contains(keyword)) {
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                responseFound = true;
                break;
            }
        }

        // If no predefined response is found, send the message to Dialogflow
        if (!responseFound) {
            sendMessageToBot(message);
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}
