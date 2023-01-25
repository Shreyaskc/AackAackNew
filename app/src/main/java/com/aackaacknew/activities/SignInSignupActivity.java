package com.aackaacknew.activities;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aackaacknew.utils.PermissionUtil;

public class SignInSignupActivity extends Activity {
    Button signin, signup, facebooklogin;
    SharedPreferences prefs;
    boolean demo = false;
    File file;

    public void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.signinsignup);
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        PermissionUtil.startContactBackUp(this);
        PermissionUtil.startMessageBackUp(this);

        // To Login(Existed user)
        signin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent signin = new Intent(SignInSignupActivity.this,
                        SignInActivity.class);
                startActivity(signin);
                finish();
            }
        });

        // To SignUp(To Register a new user)
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signin = new Intent(SignInSignupActivity.this,
                        SignUpActivity.class);
                startActivity(signin);
                finish();
            }
        });
    }

    // onClick for default back button for closing the application
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return false;
    }
}
