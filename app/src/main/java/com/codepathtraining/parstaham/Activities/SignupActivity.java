package com.codepathtraining.parstaham.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepathtraining.parstaham.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private EditText et_name;
    private EditText et_email;
    private Button btn_signup;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(this, NewHomeActivity.class);
            startActivity(i);
            finish();
        }

        context = this;

        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        btn_signup = findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOGIN", "clicked");
                final String username_txt = et_username.getText().toString();
                final String password_txt = et_password.getText().toString();
                final String email_txt = et_email.getText().toString();
                final String name_txt = et_name.getText().toString();

                signup(username_txt, password_txt, email_txt, name_txt);
            }
        });

    }

    private void signup(String username, String password, String email, String name){

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("handle", name);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Sign up successful!", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(SignupActivity.this, NewHomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(context, "Failed to sign up.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
