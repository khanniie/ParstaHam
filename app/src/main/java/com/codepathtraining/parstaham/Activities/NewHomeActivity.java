package com.codepathtraining.parstaham.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.codepathtraining.parstaham.Fragments.HomeFragment;
import com.codepathtraining.parstaham.Fragments.MakePostFragment;
import com.codepathtraining.parstaham.R;

public class NewHomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
                                                                    MakePostFragment.OnFragmentInteractionListener {
    private HomeFragment homeFragment;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ft = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            homeFragment= HomeFragment.newInstance();
            ft.replace(R.id.placeholder, homeFragment);
            ft.commit();
        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;
                    case R.id.action_makepost:
                        // do something here
                        return true;
                    case R.id.action_user:
                        // do something here
                        return true;
                }
                return false;
            }
        });
    }

    public void onHomeFragmentInteraction(Uri uri){
        Log.i("lol", "something happened");
    }
    public void onMakeFragmentInteraction(Uri uri){
        Log.i("lol", "something happened");
    }
}
