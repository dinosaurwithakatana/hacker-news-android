package io.dwak.holohackernews.app.ui.about;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.models.AboutLicense;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AboutItem> mList;

    public AboutAdapter() {
        mList = new ArrayList<>();
    }

    public void addHeader() {
        mList.add(0, new AboutItem<>(null, AboutItem.HEADER));
        notifyItemInserted(0);
    }

    public void addLicense(AboutLicense aboutLicense) {
        mList.add(new AboutItem<>(aboutLicense, AboutItem.ITEM));
        notifyItemInserted(mList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AboutItem.HEADER:
                return AboutHeaderItemViewHolder.create(parent.getContext(), parent);
            case AboutItem.ITEM:
                return AboutLicenseItemViewHolder.create(parent.getContext(), parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case AboutItem.HEADER:
                AboutHeaderItemViewHolder.bind();
                break;
            case AboutItem.ITEM:
                AboutLicenseItemViewHolder.bind((AboutLicenseItemViewHolder) holder,
                                                (AboutLicense) mList.get(position).object);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).viewType;
    }
}
