package com.aackaacknew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aackaacknew.activities.ui.messages.MessagesFragment;
import com.aackaacknew.helper.FacebookHelper;
import com.aackaacknew.helper.TwitterHelper;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.KeyHashGenerator;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.PermissionUtil;
import com.aackaacknew.utils.PreferenceHelper;
import com.aackaacknew.utils.UrltoValue;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class SignInActivity extends AppCompatActivity implements   TwitterHelper.OnTwitterSignInListener,FacebookHelper.OnFbSignInListener {
    RelativeLayout bottomLayout,topLayout;
    LinearLayout continuewithsocial, continuewithemail,tSignInButton,fbSignInButton;
    Button submit;
    public static SharedPreferences mPrefs, preferences, prefs3, prefs;
    String username, password, response = "0", success = "", checkresponse,
    strMessage = "", strUsername = "", strProfilepic = "",
    strEmail_forgot = "", strMessage_forgot = "";
    public static String resUsername, resFullname = "", lastbackup = "",
            resUserid, resProfilepic, resEmail, resPhone,
            strProfileFollowersCount = "", strProfileFollowingCount = "",
            strProfileAacksCount = "";

    public static String strFacebookId = null, firstname = "", lastname = "",
            strFacebookName, strFacebookUsername = "", deviceId = "",
            strFacebookEmail = "", strFacebookProfilePic;
    EditText edtUsername, edtPassword;
    PreferenceHelper pHelper;
    MyProgressDialog dialog;
    Long msg_time = 0l;
    private TwitterHelper twitterHelper;
    String strAccesstoken, followStatus = "";
    private FacebookHelper fbConnectHelper;
//    private ProgressBar progressBar;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private boolean isFbLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        continuewithemail = (LinearLayout) findViewById(R.id.continuewithemail);
        continuewithsocial = (LinearLayout) findViewById(R.id.continuewithsocial);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.signin);
        pHelper = new PreferenceHelper(SignInActivity.this);
        topLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);
        twitterHelper = new TwitterHelper(this, this); // Twitter Initialization
        tSignInButton = (LinearLayout) findViewById(R.id.twitterlogin);
        PermissionUtil.EnableRuntimePermission(Manifest.permission.READ_PHONE_STATE, this);
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        preferences = PreferenceManager
                .getDefaultSharedPreferences(SignInActivity.this);
        tSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                twitterHelper.connect();
//                isFbLogin = false;
            }
        });
        continuewithemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.VISIBLE);
            }
        });
        continuewithsocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.GONE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CheckInternetConnection.isOnline(SignInActivity.this)) {
                    if (validate()) {

//                        ProfileFragment.image = null;
                        Logindob dob = new Logindob();
                        dob.execute();
                    }
                } else {
                    Toast.makeText(SignInActivity.this,
                            "No Internet Connection ", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        KeyHashGenerator.generateKey(this);
        FacebookSdk.sdkInitialize(this);
        fbConnectHelper = new FacebookHelper(this, this);
        fbSignInButton = findViewById(R.id.facebooklogin);
        fbSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                fbConnectHelper.connect();
                isFbLogin = true;
            }
        });
    }
    public boolean validate() {
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString().trim().replace(" ", "");

        if ((username.length() == 0) || (username.toString() == " ")) {
            Toast.makeText(SignInActivity.this, "Please enter UserName ",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if ((password.length() == 0) || (password.toString() == " ")) {
            Toast.makeText(SignInActivity.this, "Please enter Password ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // Async class for login.
    class Logindob extends AsyncTask<URL, Integer, Long> {
        Context con;
        JSONArray ja;
        JSONObject jo;

        public void onPreExecute() {
            dialog = MyProgressDialog.show(SignInActivity.this, null, null);
        }

        protected Long doInBackground(URL... arg0) {

            if (CheckInternetConnection.isOnline(SignInActivity.this)) {
                response = UrltoValue.getValuefromUrl(DataUrls.general_login
                        + username + "&password=" + password + "&deviceid="
                        + getDeviceId());

                if (!response.equals("zero")) {
                    try {
                        jo = new JSONObject(response);
                        success = jo.getString("message");
                        try {
                            followStatus = jo.getString("followstatus");
                            if (followStatus.equalsIgnoreCase("true")) {
                                DataUrls.checkhome = true;
                            } else {
                                DataUrls.checkhome = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resEmail = jo.getString("email");
                        resFullname = jo.getString("fullname");
                        resPhone = jo.getString("number");
                        resUserid = jo.getString("userid");
                        resUsername = jo.getString("username");
                        resProfilepic = jo.getString("profilepic");

                        strProfileFollowersCount = jo
                                .getString("followerscount");
                        strProfileFollowingCount = jo
                                .getString("followingcount");
                        strProfileAacksCount = jo.getString("aackscount");

                        try {
                            msg_time = Long.parseLong(jo
                                    .getString("lastbackup").trim());// msgtime
                        } catch (Exception e) {
                            msg_time = 0l;
                            e.printStackTrace();
                        }
                        try {
                            lastbackup = jo.getString("lastbackup");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        preferences = PreferenceManager
                                .getDefaultSharedPreferences(SignInActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", resUserid);
                        editor.putString("email", resEmail);
                        editor.putString("fullname", resFullname);
                        editor.putString("lastbackup", lastbackup);
                        editor.putString("profilepic", resProfilepic);
                        editor.putString("username", resUsername);
                        editor.putString("ProfileFollowersCount",
                                strProfileFollowersCount);
                        editor.putString("ProfileFollowingCount",
                                strProfileFollowingCount);
                        editor.putString("ProfileAacksCount",
                                strProfileAacksCount);
                        editor.putLong("msgtime", msg_time);
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignInActivity.this,
                                DataUrls.dialogtitle, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.equals("0")) {
                Toast.makeText(SignInActivity.this, "No Internet Connection ",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (success.equals("success")) {
                    preferences = PreferenceManager
                            .getDefaultSharedPreferences(SignInActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("checkvalue", "1");
                    editor.putLong("messageslength", 0);
                    editor.commit();

//                    MessagesFragment.clear();

                    Intent ints = new Intent(SignInActivity.this,
                            Home.class);
                    ints.putExtra("reg", "emaillo");
                    // To start the BackGround service To get messages and
                    // upload to amazon(mms) and myappdemo.net(messages)
                    DataUrls.svc = new Intent(SignInActivity.this,
                            BackupMessagesService.class);
                    startService(DataUrls.svc);
                    startActivity(ints);
                    finish();
                }
                // To check the login with new device.
                else if (success.equals("you have login with new device")) {
//                    showDialog_Confirm_User();
                } else {
//                    showDialog_Confirm();
                }
            }
        }
    }


    @Override
    public void OnTwitterSignInComplete(TwitterHelper.UserDetails userDetails, String error) {
        if (userDetails != null) {
            Toast toast=Toast.makeText(getApplicationContext(),userDetails.getUserName(),Toast.LENGTH_SHORT);
//            userName.setText(userDetails.getUserName());
//            if (userDetails.getUserEmail() != null) {
//                email.setText(userDetails.getUserEmail());
//            }
        }
    }
    @Override
    public void OnTweetPostComplete(Result<Tweet> result, String error) {

    }
    @Override
    public void OnFbSignInComplete(GraphResponse graphResponse, String error) {
//        progressBar.setVisibility(View.GONE);
        if (error == null) {
            try {
                JSONObject jsonObject = graphResponse.getJSONObject();
//                userName.setText(jsonObject.getString("name"));
//                email.setText(jsonObject.getString("email"));
                String id = jsonObject.getString("id");
                String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        twitterHelper.onActivityResult(requestCode, resultCode, data);

        if (isFbLogin) {
//            progressBar.setVisibility(View.VISIBLE);
            isFbLogin = false;
        }
    }
    @Override protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public String getDeviceId() {
        return PermissionUtil.getDeviceId(this);
    }
}
