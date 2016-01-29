package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters;

import android.content.Context;
import android.database.DatabaseUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.l3info.utf.mm.MC.R;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by mm on 18/01/16.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages){
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       ViewHolder holder;

        if(convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);

            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageicon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.SenderLabel);
         //   holder.mImageViewClick = (ImageView)convertView.findViewById(R.id.MessageimageView);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        ParseObject message = mMessages.get(position);
        Date createdAt = message.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(),
                now,
                DateUtils.MINUTE_IN_MILLIS).toString();

        holder.timeLabel.setText(convertedDate);
        if(message.getString(ParseConstant.KEY_FILE_TYPE).equals(ParseConstant.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_photo);
        }else{
            holder.iconImageView.setImageResource(R.drawable.ic_movie);
        }
        holder.nameLabel.setText(message.getString(ParseConstant.KEY_SENDER_NAME));

//
//        ListView lisView = (ListView)parent;
//        if(lisView.isItemChecked(position)){
//            holder.mImageViewClick.setVisibility(View.VISIBLE);
//
//        }else {
//            holder.mImageViewClick.setVisibility(View.INVISIBLE);
//        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView;
    //    ImageView mImageViewClick;
        TextView nameLabel;
        TextView timeLabel;
    }


    public void refill(List<ParseObject> messages){
            mMessages.clear();
            mMessages.addAll(messages);
            notifyDataSetChanged();
    }
}
