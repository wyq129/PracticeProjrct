package com.zither.aiiage.practiceproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class FootHolder extends RecyclerView.ViewHolder {
    public TextView tips;

    public FootHolder(View itemView) {
        super(itemView);
        tips = (TextView) itemView.findViewById(R.id.tips);
    }
}