package com.halo.loginui2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;
    private FirebaseAuth auth;

    public MessageAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout, parent, false);

        auth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        String messageSenderId = auth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);

        String fromUserId = messages.getFrom();

        messageViewHolder.receiverMessageText.setVisibility(View.INVISIBLE);

        if(fromUserId.equals(messageSenderId)){
//            messageViewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
            messageViewHolder.senderMessageText.setText(messages.getMessage());
        }
        else{
            messageViewHolder.senderMessageText.setVisibility(View.INVISIBLE);
            messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);
//            messageViewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_layout);
            messageViewHolder.receiverMessageText.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {

        return userMessagesList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText,receiverMessageText;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
        }


    }




}
