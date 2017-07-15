package com.sivanta.chatapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chandan on 7/12/2017.
 */

public class ChatListAdapter extends BaseAdapter {
    private ArrayList<ChatMessage1> chatMessage1;
    private Context context;
    public static final long SIMPLE_DATE_FORMAT = new Date().getTime();

    public ChatListAdapter(ArrayList<ChatMessage1> chatMessage1, Context context) {
        this.chatMessage1 = chatMessage1;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chatMessage1.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessage1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ChatMessage1 message1 = chatMessage1.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        if (message1.getUserType() == UserType.SELF) {
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.chat_user1, null, false);
                holder1 = new ViewHolder1();
                holder1.messageTextView = (TextView) view.findViewById(R.id.textView_message);
                holder1.timeTextView = (TextView) view.findViewById(R.id.textView_time);
                view.setTag(holder1);
            } else {
                view = convertView;
                holder1 = (ViewHolder1) view.getTag();
            }
            holder1.messageTextView.setText(message1.getMessageText());
            holder1.timeTextView.setText(DateFormat.format("h:mm a", message1.getMessageTime()));
        } else if (message1.getUserType() == UserType.OTHER) {
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.chat_user2, null, false);
                holder2 = new ViewHolder2();
                holder2.messageTextView = (TextView) view.findViewById(R.id.textView_message);
                holder2.timeTextView = (TextView) view.findViewById(R.id.textView_time);
                holder2.messageStauts = (ImageView) view.findViewById(R.id.user_reply_status);
                view.setTag(holder2);


            } else {
                view = convertView;
                holder2 = (ViewHolder2) view.getTag();
            }
            holder2.messageTextView.setText(message1.getMessageText());
            holder2.timeTextView.setText(DateFormat.format("h:mm a",message1.getMessageTime()));
         if (message1.getMessageStatus()==Status.DELIVERED)
         {
             holder2.messageStauts.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_server));
         }
         else if (message1.getMessageStatus()==Status.SENT)
         {
             holder2.messageStauts.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_server));
         }
        }
        return view;
    }

    public class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
    }

    public class ViewHolder2 {
        public ImageView messageStauts;
        public TextView messageTextView;
        public TextView timeTextView;
    }
}
