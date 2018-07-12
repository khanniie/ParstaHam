package com.codepathtraining.parstaham.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.codepathtraining.parstaham.Fragments.CameraFragment;
import com.codepathtraining.parstaham.Fragments.HomeFragment;
import com.codepathtraining.parstaham.Fragments.MakePostFragment;
import com.codepathtraining.parstaham.Fragments.UserFragment;
import com.codepathtraining.parstaham.R;
import com.parse.ParseUser;

public class NewHomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
                                                                    MakePostFragment.OnFragmentInteractionListener,
                                                                    CameraFragment.OnFragmentInteractionListener,
                                                                    UserFragment.OnFragmentInteractionListener{
    private HomeFragment homeFragment;
    private MakePostFragment makeFragment;
    private CameraFragment cameraFragment;
    private UserFragment userFragment;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            homeFragment= HomeFragment.newInstance();
            makeFragment= MakePostFragment.newInstance();
            cameraFragment= CameraFragment.newInstance();
            userFragment= UserFragment.newInstance(ParseUser.getCurrentUser());
            ft.replace(R.id.placeholder, homeFragment);
            ft.commit();
        }


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        displayFragment(homeFragment);
                        return true;
                    case R.id.action_makepost:
                        displayFragment(makeFragment);
                        return true;
                    case R.id.action_user:
                        displayFragment(userFragment);
                        return true;
                }
                return false;
            }
        });
    }
    //home fragment interface
    public void onHomeFragmentInteraction(Uri uri){
        Log.i("lol", "something happened");
    }
    //make fragment interface
    public void onMakeFragmentOpenCamera(){
        Log.i("lol", "trying to open camera");
        displayFragment(cameraFragment);
    }
    public void onMakeFragmentAfterPost(){
        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
        displayFragment(homeFragment);
    }
    //camera fragment interface
    public void onCameraFragmentInteraction(String path){
        Log.i("lol", "something happened");
        displayFragment(makeFragment);
        makeFragment.RelaunchAfterCamera(path);
    }
    //userpage fragment interface
    public void OnUserFragmentLogOut(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    protected void displayFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (homeFragment.isAdded()) { ft.hide(homeFragment); }
        if (makeFragment.isAdded()) { ft.hide(makeFragment); }
        if (userFragment.isAdded()) { ft.remove(userFragment); }
        if (cameraFragment.isAdded()) { ft.remove(cameraFragment); }

        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.placeholder, fragment, "A");
        }
        // Commit changes
        ft.commit();
    }


}
