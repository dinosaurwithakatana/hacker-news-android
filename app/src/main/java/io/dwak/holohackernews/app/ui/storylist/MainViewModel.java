package io.dwak.holohackernews.app.ui.storylist;

import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;

public class MainViewModel extends BaseViewModel{

    private List<IDrawerItem> mDrawerItems;

    List<IDrawerItem> getDrawerItems(){
        if(mDrawerItems != null && !mDrawerItems.isEmpty()){
            return mDrawerItems;
        }

        List<PrimaryDrawerItem> primaryDrawerItems = new ArrayList<>();
        List<SecondaryDrawerItem> secondaryDrawerItems = new ArrayList<>();
        mDrawerItems = new ArrayList<>();

        primaryDrawerItems.add(new PrimaryDrawerItem().withIdentifier(0).withName(R.string.title_section_top).withIcon(R.drawable.ic_trending_up));
        primaryDrawerItems.add(new PrimaryDrawerItem().withIdentifier(1).withName(R.string.title_section_best).withIcon(R.drawable.ic_best));
        primaryDrawerItems.add(new PrimaryDrawerItem().withIdentifier(2).withName(R.string.title_section_newest).withIcon(R.drawable.ic_new_releases));
        primaryDrawerItems.add(new PrimaryDrawerItem().withIdentifier(3).withName(R.string.title_section_show).withIcon(R.drawable.ic_show));
        primaryDrawerItems.add(new PrimaryDrawerItem().withIdentifier(4).withName(R.string.title_section_show_new).withIcon(R.drawable.ic_show));
        mDrawerItems.addAll(primaryDrawerItems);

        mDrawerItems.add(new DividerDrawerItem());

        secondaryDrawerItems.add(new SecondaryDrawerItem().withIdentifier(5).withName(R.string.title_section_settings).withCheckable(false));
        secondaryDrawerItems.add(new SecondaryDrawerItem().withIdentifier(6).withName(R.string.title_section_about).withCheckable(false));

        mDrawerItems.addAll(secondaryDrawerItems);

        return mDrawerItems;
    }
}
