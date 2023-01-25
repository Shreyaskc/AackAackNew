package com.aackaacknew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aackaacknew.activities.R;
import com.aackaacknew.helper.FacebookHelper;
import com.aackaacknew.helper.TwitterHelper;
import com.aackaacknew.utils.KeyHashGenerator;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;
import org.json.JSONException;
import org.json.JSONObject;
public class Social extends AppCompatActivity
  implements FacebookHelper.OnFbSignInListener,
    TwitterHelper.OnTwitterSignInListener {

        private static final String TAG = MainActivity.class.getSimpleName();
        //--------------------------------Facebook login--------------------------------------//
        private FacebookHelper fbConnectHelper;
        private Button fbSignInButton;

        //-----------------------------------Twitter Sign In -----------------------------------//
        private TwitterHelper twitterHelper;
        private Button tSignInButton;

        private TextView userName;
        private TextView email;
        private ProgressBar progressBar;
        private boolean isFbLogin = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            twitterHelper = new TwitterHelper(this, this); // Twitter Initialization
            FacebookSdk.sdkInitialize(getApplicationContext()); // Facebook SDK Initialization
            setContentView(R.layout.activity_social);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);

            userName = findViewById(R.id.main_name_txt);
            email = findViewById(R.id.main_email_txt);

            //--------------------------------Facebook login--------------------------------------//
            KeyHashGenerator.generateKey(this);
            fbConnectHelper = new FacebookHelper(this, this);
            fbSignInButton = findViewById(R.id.fb_sign_in_button);
            fbSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    fbConnectHelper.connect();
                    isFbLogin = true;
                }
            });




            //----------------------------------Twitter Sign in button ------------------------------//
            tSignInButton = (Button) findViewById(R.id.main_twitter_sign_in_button);
            tSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    twitterHelper.connect();
                    isFbLogin = false;
                }
            });

            //----------------------------------Linked In Sign In Button ------------------------------//

        }

        @Override protected void onStart() {
            super.onStart();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            fbConnectHelper.onActivityResult(requestCode, resultCode, data);
            twitterHelper.onActivityResult(requestCode, resultCode, data);

            if (isFbLogin) {
                progressBar.setVisibility(View.VISIBLE);
                isFbLogin = false;
            }
        }

        @Override
        public void OnTwitterSignInComplete(TwitterHelper.UserDetails userDetails, String error) {
            progressBar.setVisibility(View.GONE);
            if (userDetails != null) {
                userName.setText(userDetails.getUserName());
                if (userDetails.getUserEmail() != null) {
                    email.setText(userDetails.getUserEmail());
                }
            }
        }

        @Override
        public void OnTweetPostComplete(Result<Tweet> result, String error) {

        }

        @Override
        public void OnFbSignInComplete(GraphResponse graphResponse, String error) {
            progressBar.setVisibility(View.GONE);
            if (error == null) {
                try {
                    JSONObject jsonObject = graphResponse.getJSONObject();
                    userName.setText(jsonObject.getString("name"));
                    email.setText(jsonObject.getString("email"));
                    String id = jsonObject.getString("id");
                    String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
                } catch (JSONException e) {
                    Log.i(TAG, e.getMessage());
                }
            }
        }




}
