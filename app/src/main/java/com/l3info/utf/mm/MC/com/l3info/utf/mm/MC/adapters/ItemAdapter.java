package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3info.utf.mm.MC.R;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.MD5Util;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by mm on 18/01/16.
 */
public class ItemAdapter extends ArrayAdapter<ParseUser> {

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public ItemAdapter(Context context, List<ParseUser> users){
        super(context, R.layout.message_item, users);
        mContext = context;
        mUsers = users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       ViewHolder holder;

        if(convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.users_items, null);

            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.userimageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.checkImageView = (ImageView) convertView.findViewById(R.id.checkimageView);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        ParseUser user = mUsers.get(position);
        String email = user.getEmail().toLowerCase();
        if(email.equals("")){
            holder.userImageView.setImageResource(R.drawable.ic_account);
        }else{
            String hash = MD5Util.md5Hex(email);
            String gravatarUrl = "http://www.gravatar.com/avatar/" + hash + "?s=150&d=404";
            Picasso.with(mContext).load(gravatarUrl).placeholder(R.drawable.ic_account).into(holder.userImageView);

        }

        holder.nameLabel.setText(user.getUsername());
        GridView gridView = (GridView)parent;
        if(gridView.isItemChecked(position)){
            holder.checkImageView.setVisibility(View.VISIBLE);

        }else {
            holder.checkImageView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder{
        ImageView userImageView;
        TextView nameLabel;
        ImageView checkImageView;

    }


    public void refill(List<ParseUser> users){
            mUsers.clear();
            mUsers.addAll(users);
            notifyDataSetChanged();
    }
}
