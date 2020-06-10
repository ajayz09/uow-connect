package com.halo.loginui2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    private final List<Messages> messagesList = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference usersRef;
    DatabaseReference reference;
    FirebaseUser currentUser;
    String receiverID;
    Button sendButton;
    EditText messageText;
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    private RecyclerView messageListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sendButton = findViewById(R.id.button_chatbox_send);
        messageText = findViewById(R.id.message);

        reference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("userInfo"));
            GetUserID(bundle.getString("userInfo"));
        }

        messageAdapter = new MessageAdapter(messagesList);
        messageListRecyclerView = findViewById(R.id.messageRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        messageListRecyclerView.setLayoutManager(linearLayoutManager);

        messageListRecyclerView.setAdapter(messageAdapter);
    }

    private void SendMessage() {
        String msg = messageText.getText().toString();
        String senderID = auth.getCurrentUser().getUid();


        String messageSenderRef = "Messages/" + senderID + "/" + receiverID;
        String messageReceiverRef = "Messages/" + receiverID + "/" + senderID;

        DatabaseReference userMessageKeRef = reference.child("Messages").child(senderID).child(receiverID).push();


        String messagePushID = userMessageKeRef.getKey();

        HashMap messageHashMap = new HashMap();
        messageHashMap.put("message", msg);
        messageHashMap.put("from", senderID);

        HashMap messageBody = new HashMap();
        messageBody.put(messageSenderRef + "/" + messagePushID, messageHashMap);
        messageBody.put(messageReceiverRef + "/" + messagePushID, messageHashMap);


        reference.updateChildren(messageBody).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Message sending failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        messageText.getText().clear();
    }

    private void GetUserID(final String name) {

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fName = dataSnapshot.child("firstName").getValue().toString();
                String lName = dataSnapshot.child("lastName").getValue().toString();
                if ((fName + " " + lName).equals(name)) {
                    receiverID = dataSnapshot.child("userID").getValue().toString();

                    if (reference.child("Messages") != null) {
                        reference.child("Messages").child(auth.getCurrentUser().getUid()).child(receiverID).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Messages messages = dataSnapshot.getValue(Messages.class);

                                messagesList.add(messages);
                                messageAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
