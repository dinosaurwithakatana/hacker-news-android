package io.dwak.holohackernews.app.ui.storylist;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.dwak.holohackernews.app.R;
import io.dwak.holohackernews.app.preferences.UserPreferenceManager;
import io.dwak.holohackernews.app.ui.NavigationDrawerAdapter;
import io.dwak.holohackernews.app.ui.NavigationDrawerItem;
import io.dwak.rx.events.RxEvents;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    public static final int NAVIGATION_ITEM_COUNT = 5;
    private NavigationDrawerCallbacks mCallbacks;
    private HNDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        setContentPivot(mDrawerLayout.getRootView());
        mDrawerToggle = new HNDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(() -> mDrawerToggle.syncState());

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        if (position < NAVIGATION_ITEM_COUNT) mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            if (position < NAVIGATION_ITEM_COUNT)
                mDrawerListView.setItemChecked(position, true);
            else
                mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            if (mDrawerListView != null) {
                mCallbacks.onNavigationDrawerItemSelected((NavigationDrawerItem) mDrawerListView.getAdapter().getItem(position));
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                UserPreferenceManager.isNightModeEnabled(getActivity())
                        ? R.layout.fragment_navigation_drawer_dark
                        : R.layout.fragment_navigation_drawer,
                container,
                false);
        mDrawerListView = (ListView) rootView.findViewById(R.id.navigation_list);
        RxEvents.observableFromListItemClick(mDrawerListView)
                .subscribe(rxListItemClickEvent -> selectItem(rxListItemClickEvent.getPosition()));

        List<NavigationDrawerItem> navigationDrawerItems = new ArrayList<>();
        navigationDrawerItems.add(new NavigationDrawerItem(0, 0, getResources().getString(R.string.title_section_top), false));
        navigationDrawerItems.add(new NavigationDrawerItem(1, 0, getResources().getString(R.string.title_section_best), false));
        navigationDrawerItems.add(new NavigationDrawerItem(2, 0, getResources().getString(R.string.title_section_newest), false));
        navigationDrawerItems.add(new NavigationDrawerItem(3, 0, getResources().getString(R.string.title_section_show), false));
        navigationDrawerItems.add(new NavigationDrawerItem(4, 0, getResources().getString(R.string.title_section_show_new), false));
        navigationDrawerItems.add(new NavigationDrawerItem(5, R.drawable.ic_action_setting, getResources().getString(R.string.title_section_settings), true));
        navigationDrawerItems.add(new NavigationDrawerItem(6, R.drawable.ic_action_about, getResources().getString(R.string.title_section_about), true));

        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), 0, navigationDrawerItems);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        selectItem(mCurrentSelectedPosition);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    private void setContentPivot(View fragmentContainerView) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        fragmentContainerView.setPivotY(size.y / 2);
        fragmentContainerView.setPivotX(size.x / 2);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         *
         * @param drawerItem
         */
        void onNavigationDrawerItemSelected(NavigationDrawerItem drawerItem);
    }

    private class HNDrawerToggle extends ActionBarDrawerToggle {

        /**
         * Construct a new ActionBarDrawerToggle.
         * <p/>
         * <p>The given {@link android.app.Activity} will be linked to the specified {@link android.support.v4.widget.DrawerLayout}.
         * The provided drawer indicator drawable will animate slightly off-screen as the drawer
         * is opened, indicating that in the open state the drawer will move off-screen when pressed
         * and in the closed state the drawer will move on-screen when pressed.</p>
         * <p/>
         * <p>String resources must be provided to describe the open/close drawer actions for
         * accessibility services.</p>
         *  @param activity                  The Activity hosting the drawer
         * @param drawerLayout              The DrawerLayout to link to the given Activity's ActionBar
         * @param openDrawerContentDescRes  A String resource to describe the "open drawer" action
 *                                  for accessibility
         * @param closeDrawerContentDescRes A String resource to describe the "close drawer" action
         */
        public HNDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerSlide(View drawerView, float offset) {
            super.onDrawerSlide(drawerView, offset);
        }

    }

}
