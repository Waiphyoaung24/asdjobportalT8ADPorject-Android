package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.fragments.JobIndustryByCategoryFragment;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.fragments.SearchJobFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {


    FrameLayout flContainer;
    List<JobDTO>jobs;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;
    MaterialToolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if(toggle.onOptionsItemSelected(item)){
          return true;
      }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggle = new ActionBarDrawerToggle(this,findViewById(R.id.drawerlayout),R.string.open,R.string.close);
        drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.addDrawerListener(toggle);
        navView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze);


        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        toggle.syncState();
        initComponents();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int id = item.getItemId();
               switch(id)
                {
                    case R.id.menu_item_job_list:
                        ListJobFragment fragment = new ListJobFragment();
                       replaceFragment(fragment);
                        break;
                    case R.id.menu_item_job_category:
                       JobIndustryByCategoryFragment categoryByJobIndustry = new JobIndustryByCategoryFragment();
                       replaceFragment(categoryByJobIndustry);
                       break;
                    case R.id.menu_item_search:
                        SearchJobFragment searchJobFragment = new SearchJobFragment();
                        replaceFragment(searchJobFragment);
                        break;


                }
          return false;
            }
        });


    }

    private void initComponents() {
        flContainer = findViewById(R.id.fl_container);
        jobs = new ArrayList<JobDTO>();

    }
    }



