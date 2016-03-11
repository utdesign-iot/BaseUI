package activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import fragments.ActionsFragment;
import fragments.DevicesFragment;
import com.utdesign.iot.baseui.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ActionBar actionBar;
    private SearchView searchView;
    private DevicesFragment devicesFragment;
    private ActionsFragment actionsFragment;
    private int activeTab;
    String ACTIVE_TAB = "ACTIVE_TAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            activeTab = 0;
        } else {
            activeTab = savedInstanceState.getInt(ACTIVE_TAB);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setCheckable(true);
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                if (menuItem.getOrder() == 1) {
                    mToolbar.setTitle(menuItem.getTitle());
                } else { mToolbar.setTitle(getTitle()); }
                Toast.makeText(MainActivity.this,
                        menuItem.getTitle()+" "+menuItem.getOrder(), Toast.LENGTH_LONG).show();                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        devicesFragment = new DevicesFragment();
        actionsFragment = new ActionsFragment();
        //alertsFragment = new AlertsFragment();

        adapter.addFragment(devicesFragment, "Devices");
        adapter.addFragment(actionsFragment, "Actions");
        //adapter.addFragment(alertsFragment, "Alerts");
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            // this anonymous class doesn't get called until after onCreateOptionsMenu()
            // Thus, searchView is already initialized.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                activeTab = tab.getPosition();

                // if the searchview hasn't been made yet, don't try to modify it's query hints
                if(searchView == null)
                    return;

                setQueryHint(searchView, activeTab);
                /*
                switch(activeTab)
                {
                    case 0:
                        searchView.setQueryHint("Search Devices...");
                        break;

                    case 1:
                        searchView.setQueryHint("Search Actions...");
                        break;

//                    case 2:
//                        searchView.setQueryHint("Search Alerts...");
//                        break;
                }
                */
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        setQueryHint(searchView, activeTab);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search every fragment for the text entered
                // TODO: find a more efficient way to search.
                // maybe search only active tabs, and make sure to filter text upon switching tabs.
                // maybe just use multiple threads

                devicesFragment.getDevicesAdapter().getFilter().filter(newText);
                actionsFragment.getActionsAdapter().getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_camera:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_edit_urls:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:
                //Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setQueryHint(SearchView searchView, int activeTab) {

        switch(activeTab)
        {
            case 0:
                searchView.setQueryHint("Search Devices...");
                break;

            case 1:
                searchView.setQueryHint("Search Actions...");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(ACTIVE_TAB, activeTab);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
