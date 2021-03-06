package com.codepathtraining.parstaham.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepathtraining.parstaham.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_signup;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(this, NewHomeActivity.class);
            startActivity(i);
            finish();
        }

        context = this;

        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        btn_login = findViewById(R.id.btn_signup);
        tv_signup = findViewById(R.id.tv_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("LOGIN", "clicked");
                final String username_txt = et_username.getText().toString();
                final String password_txt = et_password.getText().toString();

                login(username_txt, password_txt);
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SignupActivity.class);
                startActivity(i);
            }
        });

    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback(){
            @Override
            public void done(ParseUser user, ParseException e) {
                if( e == null){
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(LoginActivity.this, NewHomeActivity.class);
                    startActivity(i);
                    finish();
                } else{
                    e.printStackTrace();
                }
            }
        });

    }
}
