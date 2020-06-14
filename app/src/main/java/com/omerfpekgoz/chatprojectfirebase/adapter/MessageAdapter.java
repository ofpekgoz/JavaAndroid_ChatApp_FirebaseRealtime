package com.omerfpekgoz.chatprojectfirebase.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.cardViewHolder> {

    private Context mContext;
    private List<String> messageKeyList;
    private List<Message> messageList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser mUser;

    private String mUserId;
    private boolean state;
    private int messageTypeSend = 1;
    private int messageTypeReceived = 2;

    public MessageAdapter() {
    }

    public MessageAdapter(Context mContext, List<String> messageKeyList, List<Message> messageList) {
        this.mContext = mContext;
        this.messageKeyList = messageKeyList;
        this.messageList = messageList;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();
        state = false;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == messageTypeSend) {

            view = LayoutInflater.from(mContext).inflate(R.layout.message_send_layout, parent, false);
            return new cardViewHolder(view);
        } else {

            view = LayoutInflater.from(mContext).inflate(R.layout.message_received_layout, parent, false);
            return new cardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {

        holder.messageText.setText(messageList.get(position).getMessageText());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class cardViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            if (state == true) {
                messageText = itemView.findViewById(R.id.txtMessageSend);
            } else {
                messageText = itemView.findViewById(R.id.txtMessageReceived);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messageList.get(position).getFrom().equals(mUserId)) {
            state = true;
            return messageTypeSend;

        } else {
            state = false;
            return messageTypeReceived;
        }
    }
}
