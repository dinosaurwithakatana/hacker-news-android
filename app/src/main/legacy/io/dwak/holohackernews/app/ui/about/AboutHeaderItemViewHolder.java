package io.dwak.holohackernews.app.ui.about;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dwak.holohackernews.app.R;

public class AboutHeaderItemViewHolder extends RecyclerView.ViewHolder{
    public AboutHeaderItemViewHolder(View itemView) {
        super(itemView);
    }

    public static AboutHeaderItemViewHolder create(Context context, ViewGroup parent){
        return new AboutHeaderItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_about_header, parent, false));
    }

    public static void bind(){

    }
}
