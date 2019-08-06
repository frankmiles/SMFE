package com.freeoda.franktirkey.smfe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;

public class StartChatActivity extends AppCompatActivity {

  private String name;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    name = getIntent().getStringExtra("name");


    Backendless.setUrl(ApplicationClass.SERVER_URL);
    Backendless.initApp(this, ApplicationClass.APPLICATION_ID, ApplicationClass.API_KEY);

    setContentView(R.layout.activity_start_char);

    Button startChatButton = findViewById(R.id.start_chat_button);
    startChatButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startChat();
      }
    });
  }

  private void startChat() {
    Intent intent = new Intent(StartChatActivity.this, ChatRoomActivity.class);
    intent.putExtra("name", name);
    startActivity(intent);

  }

}