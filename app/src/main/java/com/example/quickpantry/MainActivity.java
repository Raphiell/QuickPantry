package com.example.quickpantry;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.quickpantry.Database.DatabaseHelper;
import com.example.quickpantry.Database.Item;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // View references
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] tabItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get View References
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        // Grab pantry_tab names
        tabItems = getResources().getStringArray(R.array.fragment_tabs);

        // Setup the tabs
        setupTabs();

        // Initialize the Realm
        DatabaseHelper.InitializeRealm(this);

        // Delete all items
        DatabaseHelper.DeleteAll(Item.class);

        // Add an item
        DatabaseHelper.AddItem("Fudge bars", "A few", "Hershey", new Date(), new Date(System.currentTimeMillis() + (1000) * (60) * (60) * (24) * (20)), "");
        DatabaseHelper.AddItem("Peanut Butter bars", "Lots", "Hershey", new Date(), new Date(System.currentTimeMillis() + (1000) * (60) * (60) * (24) * (7)), "");
        DatabaseHelper.AddItem("Chocolate bars", "7", "Hershey", new Date(), new Date(System.currentTimeMillis() + (1000) * (60) * (60) * (24) * (1)), "");

    } // On create

    private void setupTabs(){
        // Add new tabs for each pantry_tab found
        for(String item : tabItems) {
            tabLayout.addTab(tabLayout.newTab().setText(item));
        }

        // Pager adapter to handle swiping
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        // Set view pager change listener to trigger pantry_tab selection on specified pantry_tab when swiped
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Tab Selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
