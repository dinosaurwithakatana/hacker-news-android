package io.dwak.holohackernews.app.ui.about;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.base.BaseViewModel;
import io.dwak.holohackernews.app.models.AboutLicense;

public class AboutViewModel extends BaseViewModel{
    public AboutViewModel() {
    }

    public List<AboutLicense> getLicenses(){
        List<AboutLicense> licenses = new ArrayList<>();
        licenses.add(new AboutLicense(R.string.square,
                                      R.string.square_license));

        licenses.add(new AboutLicense(R.string.netflix_rxjava,
                                      R.string.netflix_rxjava_license));

        licenses.add(new AboutLicense(R.string.drawer_title,
                                      R.string.drawer_license));

        licenses.add(new AboutLicense(R.string.robototextview,
                                      R.string.robototextview_license));

        licenses.add(new AboutLicense(R.string.fab,
                                      R.string.fab_license));

        licenses.add(new AboutLicense(R.string.sugar,
                                      R.string.sugar_license));

        licenses.add(new AboutLicense(R.string.dagger,
                                      R.string.dagger_license));

        licenses.add(new AboutLicense(R.string.sliding_up_panel,
                                      R.string.sliding_license));

        licenses.add(new AboutLicense(R.string.logan_square,
                                      R.string.logan_square_license));

        licenses.add(new AboutLicense(R.string.disk_lru_cache,
                                      R.string.dlruc_license));

        licenses.add(new AboutLicense(R.string.swipe_layout,
                                      R.string.swipe_layout_license));

        return licenses;
    }
}
