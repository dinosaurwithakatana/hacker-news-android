package io.dwak.holohackernews.app.ui.storylist;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.models.User;
import io.dwak.holohackernews.app.preferences.LocalDataManager;

public class MainViewModel extends BaseViewModel {

    public static final int LOG_OUT_PROFILE_ITEM = -1;
    public static final int ADD_ACCOUNT_PROFILE_ITEM = 0;
    public static final int LOGGED_IN_PROFILE_ITEM = 1;

    private List<IDrawerItem> mDrawerItems;
    private IProfile[] mLoggedInProfiles;
    private IProfile[] mLoggedOutProfileItems;

    IProfile[] getLoggedOutProfileItem() {
        if (mLoggedOutProfileItems == null) {
            mLoggedOutProfileItems = new IProfile[1];
            ProfileSettingDrawerItem profileAddAccountItem = new ProfileSettingDrawerItem().withIdentifier(ADD_ACCOUNT_PROFILE_ITEM)
                                                                                           .withName(getResources().getString(R.string.nav_drawer_login))
                                                                                           .withDescription(getResources().getString(R.string.nav_drawer_add_an_account))
                                                                                           .withIcon(getResources().getDrawable(R.drawable.ic_add));
            mLoggedOutProfileItems[0] = profileAddAccountItem;
        }
        return mLoggedOutProfileItems;
    }

    IProfile[] getLoggedInProfileItem() {
        if (mLoggedInProfiles == null) {
            mLoggedInProfiles = new IProfile[2];
            User currentUser = LocalDataManager.getInstance().getUser();
            ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withIdentifier(LOGGED_IN_PROFILE_ITEM)
                                                                         .withIcon(TextDrawable.builder()
                                                                                               .buildRound(String.valueOf(currentUser.getUserName().charAt(0)),
                                                                                                           getResources().getColor(R.color.colorPrimaryDark)))
                                                                         .withName(currentUser.getUserName());
            ProfileSettingDrawerItem logoutDrawerItem = new ProfileSettingDrawerItem().withIdentifier(LOG_OUT_PROFILE_ITEM)
                                                                                      .withName("Logout")
                                                                                      .withDescription("Logout of current account")
                                                                                      .withIcon(getResources().getDrawable(R.drawable.ic_close));

            mLoggedInProfiles[0] = profileDrawerItem;
            mLoggedInProfiles[1] = logoutDrawerItem;
        }

        return mLoggedInProfiles;
    }

    void clearLoggedInProfileItem() {
        mLoggedInProfiles = null;
    }

    List<IDrawerItem> getDrawerItems() {
        if (mDrawerItems != null && !mDrawerItems.isEmpty()) {
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

    boolean isLoggedIn() {
        return LocalDataManager.getInstance().getUser() != null;
    }

    public void logout() {
        LocalDataManager.getInstance().logoutUser();
    }
}
