package com.bae.dialogflowbot;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.activities.LogoutPage;
import com.bae.dialogflowbot.activities.NotePage;
import com.bae.dialogflowbot.activities.ProfilePage;
import com.bae.dialogflowbot.activities.TaskPage;
import com.bae.dialogflowbot.adapters.ChatAdapter;
import com.bae.dialogflowbot.helpers.SendMessageInBg;
import com.bae.dialogflowbot.interfaces.BotReply;
import com.bae.dialogflowbot.models.Message;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BotReply {

  RecyclerView chatView;
  ChatAdapter chatAdapter;
  List<Message> messageList = new ArrayList<>();
  EditText editMessage;
  ImageView btnSend, menu_bar;

  //dialogFlow
  private SessionsClient sessionsClient;
  private SessionName sessionName;
  private String uuid = UUID.randomUUID().toString();
  private String TAG = "mainactivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    chatView = findViewById(R.id.chatView);
    editMessage = findViewById(R.id.editMessage);
    btnSend = findViewById(R.id.btnSend);
    menu_bar = findViewById(R.id.option_menu);

    menu_bar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          showPopupMenu(view);
      }
    });

    chatAdapter = new ChatAdapter(messageList, this);
    chatView.setAdapter(chatAdapter);

    btnSend.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        String message = editMessage.getText().toString();
        if (!message.isEmpty()) {
          messageList.add(new Message(message, false));
          editMessage.setText("");
          sendMessageToBot(message);
          Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
          Objects.requireNonNull(chatView.getLayoutManager())
              .scrollToPosition(messageList.size() - 1);
        } else {
          Toast.makeText(MainActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
        }
      }
    });

    setUpBot();
  }

  private void setUpBot() {
    try {
      InputStream stream = this.getResources().openRawResource(R.raw.credentials);
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

  private void sendMessageToBot(String message) {
    QueryInput input = QueryInput.newBuilder()
        .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
    new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
  }

  @Override
  public void callback(DetectIntentResponse returnResponse) {
     if(returnResponse!=null) {
       String botReply = returnResponse.getQueryResult().getFulfillmentText();
       if(!botReply.isEmpty()){
         messageList.add(new Message(botReply, true));
         chatAdapter.notifyDataSetChanged();
         Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
       }else {
         Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
       }
     } else {
       Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
     }
  }

  private void showPopupMenu(View view) {
    PopupMenu popupMenu = new PopupMenu(this, view);
    MenuInflater inflater = popupMenu.getMenuInflater();
    inflater.inflate(R.menu.navigation_menu, popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.profile) {
          Toast.makeText(MainActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(MainActivity.this, ProfilePage.class));
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          return true;
        } else if (item.getItemId() == R.id.notes) {
          Toast.makeText(MainActivity.this, "Notes clicked", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(MainActivity.this, NotePage.class));
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          return true;
        } else if (item.getItemId() == R.id.tasks) {
          Toast.makeText(MainActivity.this, "Task clicked", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(MainActivity.this, TaskPage.class));
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          return true;
        } else if (item.getItemId() == R.id.logout) {
          Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(MainActivity.this, LogoutPage.class);
          intent.putExtra("FROM_ACTIVITY","MainActivity");
          startActivity(intent);
          overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
          return true;
        }
        return false;
      }
    });
    popupMenu.show();
  }

  boolean doubleBackToExitPressedOnce = false;

  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      super.onBackPressed();
      finishAffinity();
      return;
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

      @Override
      public void run() {
        doubleBackToExitPressedOnce=false;
      }
    }, 2000);
  }

}
