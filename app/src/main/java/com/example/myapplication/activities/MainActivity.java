package com.example.myapplication.activities;


import android.content.Context;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.Token;
import com.example.myapplication.fragments.CompanyReviewFragment;
import com.example.myapplication.fragments.JobIndustryByCategoryFragment;
import com.example.myapplication.fragments.ListBookmarkFragment;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.fragments.ListViewedJobsFragment;
import com.example.myapplication.fragments.LoginFragment;
import com.example.myapplication.fragments.NewReviewFragment;
import com.example.myapplication.fragments.RegistrationFragment;
import com.example.myapplication.fragments.SearchJobFragment;
import com.example.myapplication.fragments.UserFragment;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {


    FrameLayout flContainer;
    Token token;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;
    MaterialToolbar toolbar;
    String tag = "";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("list");
        if(fragment!=null)
        Toast.makeText(this, "List method", Toast.LENGTH_SHORT).show();

        Fragment fragmentForCategory = getSupportFragmentManager().findFragmentByTag("category");
        if(fragmentForCategory!=null)
           replaceFragment(new ListJobFragment(),"list");



       //
       /* Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("list");
        if(fragment1!=null){
            Toast.makeText(this, "List last page", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        refreshToken();

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
        replaceFragment(fragment,"list");


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                switch (id) {
                    case R.id.menu_item_job_list:
                        ListJobFragment fragment = new ListJobFragment();
                        replaceFragment(fragment,"list");
                        break;
                    case R.id.menu_item_job_category:
                        JobIndustryByCategoryFragment categoryByJobIndustry = new JobIndustryByCategoryFragment();
                        replaceFragment(categoryByJobIndustry,"category");
                        break;
                    case R.id.menu_item_search:
                        SearchJobFragment searchJobFragment = new SearchJobFragment();
                        replaceFragment(searchJobFragment,"search");

                        break;
                    case R.id.menu_item_bookmark:
                        ListBookmarkFragment bookmarkFragment = new ListBookmarkFragment();
                        replaceFragment(bookmarkFragment,"bookmark");

                        break;
                    case R.id.menu_item_viewedjobs:
                        ListViewedJobsFragment listviewedjobsFragment = new ListViewedJobsFragment();
                        replaceFragment(listviewedjobsFragment,"viewdJob");

                        break;
                    case R.id.menu_item_review:
                        CompanyReviewFragment companyReviewFragment = new CompanyReviewFragment();
                        replaceFragment(companyReviewFragment,"companyReview");

                        break;
                    case R.id.menu_item_new_review:
                        NewReviewFragment newReview = new NewReviewFragment();
                        replaceFragment(newReview,"newReview");

                        break;
                    case R.id.menu_item_login:
                        LoginFragment loginFragment = new LoginFragment();
                        replaceFragment(loginFragment);
                        break;
                    case R.id.menu_item_registration:
                        RegistrationFragment registrationFragment = new RegistrationFragment();
                        replaceFragment(registrationFragment);
                        break;
                    case R.id.menu_item_user:
                        UserFragment userFragment = new UserFragment();
                        Bundle bundle = new Bundle();
                        if (token != null) {
                            bundle.putSerializable("Token", token);
                            userFragment.setArguments(bundle);
                            Log.i("token: ", token.toString());
                        }
                        replaceFragment(userFragment,"userProfile");
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

    private void refreshToken() {
        String username_, refresh_token;
        try {
            SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username_ = storeToken.getString("username", null);
            refresh_token = storeToken.getString("refresh_token", null);
            if (username_ != null && refresh_token != null) {
                String authorization = "Bearer " + refresh_token;
                Log.i("refreshtoken", authorization);
                Call<Token> call = RetrofitClient.getInstance().getResponse().refreshToken(authorization);
                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        Log.i("status", String.valueOf(response.code()));
                        if (response.isSuccessful()) {
                            Log.i("refresh success", "success");
                            SharedPreferences.Editor editor = storeToken.edit();
                            Token token = response.body();
                            editor.putString("access_token", token.getAccess_token());
                            editor.putString("refresh_token", token.getRefresh_token());
                            editor.putString("username", token.getUsername());
                            editor.apply();
                        } else {
                            Log.i("refresh fail", "");
                            Toast.makeText(getApplicationContext(), "please login first", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = storeToken.edit();
                            editor.clear().commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.i("refresh token on failure error: ", t.getMessage());
                        Toast.makeText(getApplicationContext(), "plese login first", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = storeToken.edit();
                        editor.clear().commit();
                    }
                });
            } else {
                Log.i("there is no token stored, need login first", "");
                Toast.makeText(getApplicationContext(), "please login first", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = storeToken.edit();
                editor.clear().commit();
            }
        } catch(NullPointerException e){
            Toast.makeText(this,"please login first",Toast.LENGTH_SHORT).show();
            Log.i("there is no user stored","");
        }
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int i = RESULT_OK;
    }
}



