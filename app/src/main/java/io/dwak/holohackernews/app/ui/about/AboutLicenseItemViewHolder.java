package io.dwak.holohackernews.app.ui.about;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.models.AboutLicense;

public class AboutLicenseItemViewHolder extends RecyclerView.ViewHolder{
    @InjectView(R.id.name) public TextView name;
    @InjectView(R.id.license) public TextView license;
    public AboutLicenseItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public static AboutLicenseItemViewHolder create(Context context, ViewGroup parent){
        return new AboutLicenseItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_about_license, parent, false));
    }

    public static void bind(AboutLicenseItemViewHolder holder, AboutLicense license){
        holder.name.setText(holder.itemView.getContext().getResources().getString(license.nameResId));
        holder.license.setText(holder.itemView.getContext().getResources().getString(license.licenseResId));
    }
}
