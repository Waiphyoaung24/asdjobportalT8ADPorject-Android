package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.data.Token;
import com.example.myapplication.fragments.CompanyReviewFragment;
import com.example.myapplication.fragments.JobIndustryByCategoryFragment;
import com.example.myapplication.fragments.ListBookmarkFragment;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.fragments.ListViewedJobsFragment;
import com.example.myapplication.fragments.NewReviewFragment;
import com.example.myapplication.fragments.SearchJobFragment;
import com.example.myapplication.fragments.UserFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity {


    FrameLayout flContainer;
    Token token;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;
    MaterialToolbar toolbar;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        //get token
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            token = (Token)bundle.getSerializable("Token");
            Log.i("token: ", token.toString());
        } catch(Exception e){
            Log.e("error: ", e.getMessage());
        }

        //drawerlayout toggle state
        toggle = new ActionBarDrawerToggle(this, findViewById(R.id.drawerlayout), R.string.open, R.string.close);
        toggle.syncState();

        //image view adding inside toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze);



        //changing color of status bar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        ListJobFragment fragment = new ListJobFragment();
        replaceFragment(fragment);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                switch (id) {
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
                    case R.id.menu_item_bookmark:
                        ListBookmarkFragment bookmarkFragment = new ListBookmarkFragment();
                        replaceFragment(bookmarkFragment);

                        break;
                    case R.id.menu_item_viewedjobs:
                        ListViewedJobsFragment listviewedjobsFragment = new ListViewedJobsFragment();
                        replaceFragment(listviewedjobsFragment);

                        break;
                    case R.id.menu_item_review:
                        CompanyReviewFragment companyReviewFragment = new CompanyReviewFragment();
                        replaceFragment(companyReviewFragment);

                        break;
                    case R.id.menu_item_new_review:
                        NewReviewFragment newReview = new NewReviewFragment();
                        replaceFragment(newReview);

                        break;
                    case R.id.menu_item_login:
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        break;
                    case R.id.menu_item_user:
                        UserFragment userFragment = new UserFragment();
                        Bundle bundle = new Bundle();
                        if(token!=null){
                            bundle.putSerializable("Token", token);
                            userFragment.setArguments(bundle);
                            Log.i("token: ", token.toString());
                        }
                        replaceFragment(userFragment);
                        break;


                }
                return false;
            }
        });


    }

    private void initComponents() {
        flContainer = findViewById(R.id.fl_container);
        drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.addDrawerListener(toggle);
        navView = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.toolbar);

    }


}



