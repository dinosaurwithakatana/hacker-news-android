package io.dwak.holohackernews.app.ui.about;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.models.AboutLicense;

public class AboutViewModel extends BaseViewModel{
    Resources mResources;

    public AboutViewModel(Resources resources) {
        mResources = resources;
    }

    public List<AboutLicense> getLicenses(){
        List<AboutLicense> licenses = new ArrayList<>();
        licenses.add(new AboutLicense(mResources.getString(R.string.square),
                                      mResources.getString(R.string.square_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.netflix_rxjava),
                                      mResources.getString(R.string.netflix_rxjava_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.drawer_title),
                                      mResources.getString(R.string.drawer_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.robototextview),
                                      mResources.getString(R.string.robototextview_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.fab),
                                      mResources.getString(R.string.fab_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.sugar),
                                      mResources.getString(R.string.sugar_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.dagger),
                                      mResources.getString(R.string.dagger_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.sliding_up_panel),
                                      mResources.getString(R.string.sliding_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.logan_square),
                                      mResources.getString(R.string.logan_square_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.disk_lru_cache),
                                      mResources.getString(R.string.dlruc_license)));

        licenses.add(new AboutLicense(mResources.getString(R.string.swipe_layout),
                                      mResources.getString(R.string.swipe_layout_license)));

        return licenses;
    }
}
