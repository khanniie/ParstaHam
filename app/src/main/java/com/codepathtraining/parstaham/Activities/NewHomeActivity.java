package com.codepathtraining.parstaham.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.codepathtraining.parstaham.Fragments.CameraFragment;
import com.codepathtraining.parstaham.Fragments.GalleryFragment;
import com.codepathtraining.parstaham.Fragments.HomeFragment;
import com.codepathtraining.parstaham.Fragments.MakePostFragment;
import com.codepathtraining.parstaham.Fragments.PostDetailFragment;
import com.codepathtraining.parstaham.Fragments.UserFragment;
import com.codepathtraining.parstaham.Models.Post;
import com.codepathtraining.parstaham.R;
import com.parse.ParseUser;

public class NewHomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
                                                                    MakePostFragment.OnFragmentInteractionListener,
                                                                    CameraFragment.OnFragmentInteractionListener,
                                                                    UserFragment.OnFragmentInteractionListener,
                                                                    PostDetailFragment.OnFragmentInteractionListener,
                                                                    GalleryFragment.OnFragmentInteractionListener{
    private HomeFragment homeFragment;
    private MakePostFragment makeFragment;
    private CameraFragment cameraFragment;
    private UserFragment userFragment;
    private GalleryFragment galleryFragment;
    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;
    private Fragment previousFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (savedInstanceState == null) {
            homeFragment= HomeFragment.newInstance();
            makeFragment= MakePostFragment.newInstance();
            cameraFragment= CameraFragment.newInstance();
            galleryFragment = GalleryFragment.newInstance();
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
    public void onHomeFragmentInteraction(Post post){
        PostDetailFragment detailFragment= PostDetailFragment.newInstance(post);
        displayFragment(detailFragment);
    }
    public void onUserProfClicked(ParseUser user){
        UserFragment otherUserFragment = UserFragment.newInstance(user);
        displayFragment(otherUserFragment);
    }
    //make fragment interface
    public void onMakeFragmentOpenCamera(){
        Log.i("lol", "trying to open camera");
        displayFragment(cameraFragment);
    }
    public void onMakeFragmentOpenGallery(){
        Log.i("lol", "trying to open camera");
        displayFragment(galleryFragment);
    }
    public void onMakeFragmentAfterPost(){
        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
        makeFragment= MakePostFragment.newInstance();
        displayFragment(homeFragment);
    }
    //camera fragment interface
    public void onCameraFragmentInteraction(String path){
        bottomNavigationView.getMenu().findItem(R.id.action_makepost).setChecked(true);
        displayFragment(makeFragment);
        makeFragment.RelaunchAfterCamera(path);
    }
    //userpage fragment interface
    public void OnUserFragmentLogOut(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    public void OnUserFragmentOpenGallery(){
        displayFragment(galleryFragment);
    }
    public void OnPostDetailViewClick(Post post){
        PostDetailFragment detailFragment= PostDetailFragment.newInstance(post);
        displayFragment(detailFragment);
    }
    //detail fragment interface
    public void onDetailFragmentInteraction(){ }
    //gallery interface
    public void onGalleryFragmentInteraction(String path){
        if(previousFragment instanceof MakePostFragment){
            bottomNavigationView.getMenu().findItem(R.id.action_makepost).setChecked(true);
            displayFragment(makeFragment);
            makeFragment.RelaunchAfterCamera(path);
        } else if(previousFragment instanceof  UserFragment){
            bottomNavigationView.getMenu().findItem(R.id.action_user).setChecked(true);
            displayFragment(userFragment);
            userFragment.RelaunchAfterGallery(path);
        } else {
            Log.i("HOME ACTIVITY", previousFragment.toString());
        }

    }

    protected void displayFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null && (currentFragment instanceof PostDetailFragment)) { ft.remove(currentFragment); }
        if (currentFragment != null && (currentFragment instanceof UserFragment)) { ft.remove(currentFragment); }
        if (homeFragment.isAdded()) { ft.hide(homeFragment); }
        if (makeFragment.isAdded()) { ft.hide(makeFragment); }
        if (userFragment.isAdded()) { ft.remove(userFragment); }
        if (galleryFragment.isAdded()) { ft.remove(galleryFragment); }
        if (cameraFragment.isAdded()) { ft.remove(cameraFragment); }

        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.placeholder, fragment, "A");
            ft.addToBackStack(fragment.toString());
        }
        previousFragment = currentFragment;
        currentFragment = fragment;
        // Commit changes
        ft.commit();
    }


}
