package com.example.ckbur.tracking_friend;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ListOnlineViewHolder extends RecyclerView.ViewHolder{

    public TextView txtEmail;

    public CardView parent_layout;

    public ListOnlineViewHolder(@NonNull View itemView) {
        super(itemView);

        txtEmail=(TextView)itemView.findViewById(R.id.text_email);
        parent_layout=(CardView)itemView.findViewById(R.id.parent_Layout);

    }
}
