package com.sivanta.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageButton sendMsg;
    private ListView chatListView;
    private EditText chatEditText;
    private ArrayList<ChatMessage1> chatMessage1;
    private ChatListAdapter listAdapter;
    private static final int SIGN_IN_REQUEST_CODE = 1;
    //private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        chatMessage1=new ArrayList<>();

        chatListView= (ListView) findViewById(R.id.list_of_messages);
        chatEditText= (EditText) findViewById(R.id.input_editText);
         listAdapter=new ChatListAdapter(chatMessage1,this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Welcome " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
           // displayChatMessages();
        }
        sendMsg = (ImageButton) findViewById(R.id.send_msg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = (EditText) findViewById(R.id.input);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (s.toString().equals("")) {
                         //   input.setError(" please enter a message");
                            sendMsg.setEnabled(false);
                        } else {
                            String in = input.getText().toString();
                            sendMsg.setEnabled(true);
                            FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(in,
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                input.setText("");
            }
        });
    }

   /* private void displayChatMessages() {
        ListView listView = (ListView) findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        listView.setAdapter(adapter);


    } */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_SHORT).show();
              //  displayChatMessages();
            } else {
                Toast.makeText(this, "We couldn't sign you in. Please try again later.", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ChatActivity.this, "You have been signed out.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    private void sendMessage(final String messageText, final UserType userType)
    {
        if(messageText.trim().length()==0)
            return;

        final ChatMessage1 message = new ChatMessage1();
        message.setMessageStatus(Status.SENT);
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());
        chatMessage1.add(message);

        if(listAdapter!=null)
            listAdapter.notifyDataSetChanged();

        // Mark message as delivered after one second

        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable() {
            @Override
            public void run() {
                message.setMessageStatus(Status.DELIVERED);

                final ChatMessage1 message = new ChatMessage1();
                message.setMessageStatus(Status.SENT);
                message.setMessageText(messageText); // 10 spaces;
                message.setUserType(UserType.SELF);
                message.setMessageTime(new Date().getTime());
                chatMessage1.add(message);

                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });


            }
        }, 1, TimeUnit.SECONDS);

    }
}
