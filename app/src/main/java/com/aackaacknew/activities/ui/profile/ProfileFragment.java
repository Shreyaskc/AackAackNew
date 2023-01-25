package com.aackaacknew.activities.ui.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aackaacknew.activities.Home;
import com.aackaacknew.activities.R;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.activities.BackupMessagesService;
import com.aackaacknew.activities.ContactsService;
import com.aackaacknew.activities.HomeDetailsActivity;
import com.aackaacknew.activities.MainActivity;
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.R;
import com.aackaacknew.activities.SignInActivity;
import com.aackaacknew.activities.SignInSignupActivity;
import com.aackaacknew.activities.UpdateProfileActivity;
import com.aackaacknew.activities.ui.explore.ExploreFragment;
import com.aackaacknew.activities.ui.home.HomeFragment;
import com.aackaacknew.activities.ui.messages.MessagesFragment;
import com.aackaacknew.adapters.ProfileAdapter;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.ImageLoader1;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.RoundImageView;
import com.aackaacknew.utils.UrltoValue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ProfileFragment extends Fragment {

//    private ProfileViewModel profileViewModel;
//    public static Bitmap image;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel =
//                ViewModelProviders.of(this).get(ProfileViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_profile);
//        profileViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }




    public boolean mainlayout_visible;
    MainLayout mLayout;
    RelativeLayout rlFollowingCount, rlFollowersCount, rlAacksCount/*, relBack*/;
    public static String str = "1", strUserId;
    MyProgressDialog dialog;
    SharedPreferences prefs;
    String strLoginType = "2", strMessage = "", shareType = "";
    public static String strfollowstatus = "";
    ProfileAdapter profile_adapter;
    ListView lst_profile;
    View footerView;
    boolean lodingForLatestMessages;
    public static DisplayImageOptions optionscustomimge = null;
    String strUserid, strResponse_update = "", strUsername_Update,
            strProfilepic_Update, strPassword_Update, strLogintype_Update,
            strFirstname_Update, strEmail_Update, strLastname_Update,
            strNum_Update;
    String strFullname, strFollowersCount, strAacksCount, strUsername,
            strProfilepic, strFollowingCount;

    ArrayList<String> profile_Aackcontent, profile_Caption,
            profile_Conversation, profile_Date, profile_Pic, profile_userid,
            profile_aackid, profile_aackthumb, profile_sharetype,
            reaackowner_fullname, reaackowner_username;
    ArrayList<Boolean> profile_resharestatus;

    public static ArrayList<String> profile_username = new ArrayList<String>();
    public static ArrayList<String> profile_nickname = new ArrayList<String>();
    boolean isFavs_first = true, loading_favs = true;
    int count, favs_scrollcount = 0, previousTotal_favs = 0;
    TextView txtName, txtaackme, txtaacks, txtfollowers, txtfollowing,
            txtSubname, txtAacksCount, txtFollowersCount, txtFollowingCount,
            txtHeader;
    public static RoundImageView imgProfilePic;
//    Button btn_settings;
    public static String profilepic;
    public static View view;
    public static Bitmap image;
    ImageLoader1 i1;
//    Button btndelete;
    String searchresponse;
    public static boolean delete, profile;
    ArrayList<Integer> array;
    ProfileFragment profilefrag;
    public boolean profileimg = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, null);

        mainlayout_visible = true;
        strLoginType = "2";
        delete = false;
        profilefrag = this;
        profileimg = false;

        profile_Aackcontent = new ArrayList<String>();
        profile_Caption = new ArrayList<String>();
        profile_Conversation = new ArrayList<String>();
        profile_Date = new ArrayList<String>();
        profile_Pic = new ArrayList<String>();
        profile_userid = new ArrayList<String>();
        profile_aackid = new ArrayList<String>();
        profile_aackthumb = new ArrayList<String>();

        profile_sharetype = new ArrayList<String>();
        reaackowner_fullname = new ArrayList<String>();
        // reaackowner_profilepic = new ArrayList<String>();
        reaackowner_username = new ArrayList<String>();
        profile_resharestatus = new ArrayList<Boolean>();

        array = new ArrayList<Integer>();

        // To get the all id's
        txtHeader = (TextView) view.findViewById(R.id.header);
//        btn_settings = (Button) view.findViewById(R.id.settings);
//        relBack = (RelativeLayout) view.findViewById(R.id.relback);
        lst_profile = (ListView) view.findViewById(R.id.list);
        rlFollowingCount = (RelativeLayout) view.findViewById(R.id.im3);
        rlFollowersCount = (RelativeLayout) view.findViewById(R.id.im2);
        rlAacksCount = (RelativeLayout) view.findViewById(R.id.im1);
        txtSubname = (TextView) view.findViewById(R.id.subname);
        txtName = (TextView) view.findViewById(R.id.name);
        txtaacks = (TextView) view.findViewById(R.id.aacks);
        txtfollowers = (TextView) view.findViewById(R.id.followers);
        txtfollowing = (TextView) view.findViewById(R.id.following);
        txtaackme = (TextView) view.findViewById(R.id.aackme);
        txtAacksCount = (TextView) view.findViewById(R.id.aackscount);
        txtFollowersCount = (TextView) view.findViewById(R.id.followerscount);
        txtFollowingCount = (TextView) view.findViewById(R.id.followingcount);
        imgProfilePic = (RoundImageView) view.findViewById(R.id.image);
//        btndelete = (Button) view.findViewById(R.id.btndelete);

        footerView = inflater.inflate(R.layout.listfooter, null);
        DataUrls.initImageLoader(getActivity());
        mLayout = Home.mLayout;

        i1 = new ImageLoader1(getActivity());

        if (image != null) {
            profileimg = false;
            try {
                imgProfilePic.setImageBitmap(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            profileimg = true;
        }

        // this is used for setting textview for different colors
        txtaacks.setTextColor(Color.parseColor("#e92727"));
        txtfollowers.setTextColor(Color.parseColor("#000000"));
        txtfollowing.setTextColor(Color.parseColor("#000000"));
        lst_profile.setScrollingCacheEnabled(false);

        // To Hide/Show the Menu
//        relBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // hide key board
//                final InputMethodManager imm = (InputMethodManager) getActivity()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//                // Show/hide the menu
//                if (mainlayout_visible) {
//                    mLayout.toggleMenu();
//                    mainlayout_visible = false;
//                } else {
//                    mLayout.toggleMenu();
//                    mainlayout_visible = true;
//                }
//            }
//        });

        // when called settings button
//        btn_settings.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog_Settings();
//            }
//        });

//        btndelete.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!delete) {
//                    delete = true;
//
//                    if (profile_adapter != null)
//                        profile_adapter.notifyDataSetChanged();
//                } else {
//                    for (int i = 0; i < profile_Aackcontent.size(); i++) {
//                        View view = getViewByPosition(i, lst_profile);
//
//                        CheckBox checkbox = (CheckBox) view
//                                .getTag(R.id.checkbox);
//
//                        boolean check = isCheckedOrNot(checkbox);
//
//                        if (check) {
//                            array.add(i);
//                        } else {
//                            if (array.contains(i)) {
//                                array.remove(i);
//                            }
//                        }
//                    }
//
//                    if (array.size() != 0) {
//                        alertDelete();
//                    } else {
//                        delete = false;
//
//                        if (profile_adapter != null)
//                            profile_adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });

        txtaacks.setText("AACKS");
        txtfollowers.setText("FOLLOWERS");
        txtfollowing.setText("FOLLOWING");
        txtaackme.setText("aackMe");

        // When called the Following
        rlFollowingCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is used for setting textview for different colors
                txtaacks.setTextColor(Color.parseColor("#000000"));
                txtfollowers.setTextColor(Color.parseColor("#000000"));
                txtfollowing.setTextColor(Color.parseColor("#e92727"));

//                btndelete.setVisibility(View.GONE);
                delete = false;

                if (profile_adapter != null)
                    profile_adapter.notifyDataSetChanged();

                isFavs_first = true;
                loading_favs = true;
                count = 0;
                favs_scrollcount = 0;
                previousTotal_favs = 0;

                str = "3";
                profile_Aackcontent.clear();
                profile_Caption.clear();
                profile_Conversation.clear();
                profile_Date.clear();
                profile_Pic.clear();
                profile_userid.clear();
                profile_aackthumb.clear();
                profile_aackid.clear();
                profile_username.clear();
                profile_nickname.clear();

                profile_sharetype.clear();
                profile_resharestatus.clear();
                reaackowner_fullname.clear();
                reaackowner_username.clear();

                if (CheckInternetConnection.isOnline(getActivity())) {
                    doInProfileListData dob1 = new doInProfileListData(0);
                    dob1.execute();
                } else {
                    if (strUsername == null) {
                        prefs = PreferenceManager
                                .getDefaultSharedPreferences(getActivity());
                        strUsername = prefs.getString("username", "0");
                    }

                    txtName.setText(strFullname);
                    txtSubname.setText("@" + strUsername);
                    txtFollowersCount.setText(strFollowersCount);
                    txtFollowingCount.setText(strFollowingCount);

                    txtAacksCount.setText(strAacksCount);

                    Toast.makeText(getActivity(), "No Internet Connectivity",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // When called the followers
        rlFollowersCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is used for setting textview for different colors
                txtaacks.setTextColor(Color.parseColor("#000000"));
                txtfollowers.setTextColor(Color.parseColor("#e92727"));
                txtfollowing.setTextColor(Color.parseColor("#000000"));

//                btndelete.setVisibility(View.GONE);
                delete = false;

                if (profile_adapter != null)
                    profile_adapter.notifyDataSetChanged();

                isFavs_first = true;
                loading_favs = true;
                count = 0;
                favs_scrollcount = 0;
                previousTotal_favs = 0;

                str = "2";
                profile_Aackcontent.clear();
                profile_Caption.clear();
                profile_Conversation.clear();
                profile_Date.clear();
                profile_Pic.clear();
                profile_userid.clear();
                profile_aackthumb.clear();
                profile_aackid.clear();
                profile_username.clear();
                profile_nickname.clear();

                profile_sharetype.clear();
                profile_resharestatus.clear();
                reaackowner_fullname.clear();
                reaackowner_username.clear();

                if (CheckInternetConnection.isOnline(getActivity())) {
                    doInProfileListData dob2 = new doInProfileListData(0);
                    dob2.execute();
                } else {

                    if (strUsername == null) {
                        prefs = PreferenceManager
                                .getDefaultSharedPreferences(getActivity());
                        strUsername = prefs.getString("username", "0");
                    }

                    txtName.setText(strFullname);
                    txtSubname.setText("@" + strUsername);
                    txtFollowersCount.setText(strFollowersCount);
                    txtFollowingCount.setText(strFollowingCount);

                    txtAacksCount.setText(strAacksCount);
                    Toast.makeText(getActivity(), "No Internet Connectivity",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // When called the Aacks
        rlAacksCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is used for setting textview for different colors
                txtaacks.setTextColor(Color.parseColor("#e92727"));
                txtfollowers.setTextColor(Color.parseColor("#000000"));
                txtfollowing.setTextColor(Color.parseColor("#000000"));

//                btndelete.setVisibility(View.VISIBLE);

                str = "1";

                isFavs_first = true;
                loading_favs = true;
                count = 0;
                favs_scrollcount = 0;
                previousTotal_favs = 0;

                profile_Aackcontent.clear();
                profile_Caption.clear();
                profile_Conversation.clear();
                profile_Date.clear();
                profile_Pic.clear();
                profile_userid.clear();
                profile_aackthumb.clear();
                profile_aackid.clear();

                profile_sharetype.clear();
                reaackowner_fullname.clear();
                reaackowner_username.clear();
                profile_resharestatus.clear();

                if (CheckInternetConnection.isOnline(getActivity())) {
                    doInProfileListData profilelist = new doInProfileListData(0);
                    profilelist
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    if (strUsername == null) {
                        prefs = PreferenceManager
                                .getDefaultSharedPreferences(getActivity());
                        strUsername = prefs.getString("username", "0");
                    }

                    txtName.setText(strFullname);
                    txtSubname.setText("@" + strUsername);
                    txtFollowersCount.setText(strFollowersCount);
                    txtFollowingCount.setText(strFollowingCount);

                    txtAacksCount.setText(strAacksCount);
                    Toast.makeText(getActivity(), "No Internet Connectivity",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        lst_profile.setOnScrollListener(new EndlessScrollListenerFavs());

        rlAacksCount.callOnClick();// By Defalut we called Aacks
        // when click on the Aack(listview item)
        lst_profile.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    profile = true;
                    HomeDetailsActivity.comments.clear();
                    HomeDetailsActivity.commentspic.clear();
                    HomeDetailsActivity.time.clear();
                    HomeDetailsActivity.commentersname.clear();
                    HomeDetailsActivity.likersname.clear();
                    HomeDetailsActivity.likerspic.clear();
                    HomeDetailsActivity.comment_time.clear();

                    Intent profiledet = new Intent(getActivity(),
                            HomeDetailsActivity.class);

                    if (str.equals("1")) {
                        if (profile_sharetype.get(arg2).equals("myshare")) {
                            profiledet.putExtra("name", strFullname);
                            profiledet.putExtra("username", strUsername);
                            profiledet.putExtra("pic", profile_Pic.get(arg2));
                        } else {
                            profiledet.putExtra("name",
                                    reaackowner_fullname.get(arg2));
                            profiledet.putExtra("username",
                                    reaackowner_username.get(arg2));
                            profiledet.putExtra("pic", profile_Pic.get(arg2));

                        }
                    } else {
                        profiledet.putExtra("name", strFullname);
                        profiledet.putExtra("username", strUsername);
                        profiledet.putExtra("pic", profile_Pic.get(arg2));
                    }

                    profiledet.putExtra("userid", profile_userid.get(arg2));
                    profiledet.putExtra("aack", profile_Aackcontent.get(arg2));
                    profiledet.putExtra("aackid", profile_aackid.get(arg2));

                    ExploreFragment.homeresharestatus = profile_resharestatus
                            .get(arg2);
                    ExploreFragment.homepic = profile_Pic.get(arg2);
                    ExploreFragment.homeaackid = profile_aackid.get(arg2);
                    ExploreFragment.homeaack = profile_Aackcontent.get(arg2);
                    ExploreFragment.homeuserid = profile_userid.get(arg2);
                    ExploreFragment.homelikestatue = "0";
                    ExploreFragment.homeconversation = profile_Conversation
                            .get(arg2);
                    ExploreFragment.homecaption = profile_Caption.get(arg2);

                    HomeFragment.follow_status_check = true;

                    if (str.equals("1")) {
                        if (profile_sharetype.get(arg2).equals("myshare")) {
                            ExploreFragment.homename = strUsername;
                            ExploreFragment.homestrname = strFullname;
                            ExploreFragment.homestrusername = strUsername;
                        } else if (profile_sharetype.get(arg2)
                                .equals("reshare")) {
                            ExploreFragment.homename = reaackowner_username
                                    .get(arg2);
                            ExploreFragment.homestrname = reaackowner_fullname
                                    .get(arg2);
                            ExploreFragment.homestrusername = reaackowner_username
                                    .get(arg2);
                        }
                    } else {
                        ExploreFragment.homename = profile_username.get(arg2);
                        ExploreFragment.homestrname = profile_nickname
                                .get(arg2);
                        ExploreFragment.homestrusername = profile_username
                                .get(arg2);
                    }
                    startActivity(profiledet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }// oncreateview..

    // async for profile list data...
    class doInProfileListData extends AsyncTask<URL, Integer, Long> {
        int msgscount;

        public doInProfileListData(int msgscount) {
            super();
            this.msgscount = msgscount;
            lodingForLatestMessages = true;
        }

        protected void onPreExecute() {
            if (isFavs_first) {
                dialog = MyProgressDialog.show(getActivity(), null, null);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Long doInBackground(URL... arg0) {
            try {
                String strDate = null, loginResponse = null;
                strDate = getDateTime();
                prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());

                strUserId = prefs.getString("userid", "");
                if (str.equals("1")) {
                    loginResponse = UrltoValue.getValuefromUrl(DataUrls.profile
                            + "userid=" + strUserId + "&devicedatetime="
                            + strDate.replaceAll(" ", "%20") + "&start="
                            + msgscount);

                } else if (str.equals("2")) {
                    loginResponse = UrltoValue
                            .getValuefromUrl(DataUrls.profiles_followers
                                    + "userid=" + strUserId
                                    + "&devicedatetime="
                                    + strDate.replaceAll(" ", "%20")
                                    + "&start=" + msgscount);
                } else if (str.equals("3")) {
                    loginResponse = UrltoValue
                            .getValuefromUrl(DataUrls.profiles_following
                                    + "userid=" + strUserId
                                    + "&devicedatetime="
                                    + strDate.replaceAll(" ", "%20")
                                    + "&start=" + msgscount);
                }
                if (str.equals("1")) {
                    count = 0;
                    if (!loginResponse.equals("zero")) {
                        try {
                            JSONObject jobject = new JSONObject(loginResponse);
                            strUserId = jobject.getString("userid");
                            strFullname = jobject.getString("fullname");
                            strUsername = jobject.getString("username");
                            strFollowingCount = jobject
                                    .getString("followingcount");
                            strFollowersCount = jobject
                                    .getString("followerscount");
                            strAacksCount = jobject.getString("aackscount");
                            strProfilepic = jobject.getString("profilepic");
                            strLoginType = jobject.getString("logintype");

                            JSONArray jsonArray = jobject.getJSONArray("aacks");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    count++;// added for aptr elements

                                    JSONObject jsonObject = jsonArray
                                            .getJSONObject(i);
                                    profile_aackthumb.add(jsonObject
                                            .getString("aackthumb"));
                                    profile_Caption.add(jsonObject
                                            .getString("caption"));
                                    profile_Conversation.add(jsonObject
                                            .getString("conversationfrom"));
                                    profile_Date.add(jsonObject
                                            .getString("devicedatetime"));
                                    profile_userid.add(jsonObject
                                            .getString("userid"));
                                    profile_aackid.add(jsonObject
                                            .getString("aackid"));
                                    profile_Aackcontent.add(jsonObject
                                            .getString("aackcontent"));

                                    shareType = jsonObject
                                            .getString("sharetype");
                                    profile_sharetype.add(shareType);

                                    profile_resharestatus.add(jsonObject
                                            .getBoolean("reshareStatus"));

                                    if (shareType.equals("myshare")) {

                                        reaackowner_fullname.add("");
                                        profile_Pic.add(jsonObject
                                                .getString("profilepic"));
                                        reaackowner_username.add("");
                                    } else if (shareType.equals("reshare")) {
                                        reaackowner_fullname.add(jsonObject
                                                .getString("Ownerfullname"));
                                        profile_Pic.add(jsonObject
                                                .getString("Ownerprofilepic"));
                                        reaackowner_username.add(jsonObject
                                                .getString("Ownerusername"));
                                    }
                                }
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lst_profile
                                                .removeFooterView(footerView);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Network Error",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (str.equals("2")) {
                    count = 0;
                    if (!loginResponse.equals("zero")) {
                        try {
                            JSONObject jobject = new JSONObject(loginResponse);
                            strFollowersCount = jobject
                                    .getString("followeecount");
                            JSONArray jsonArray = jobject
                                    .getJSONArray("useraacks");

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    count++;// added for aptr elements

                                    JSONObject jsonObject = jsonArray
                                            .getJSONObject(i);

                                    profile_aackthumb.add(jsonObject
                                            .getString("aackthumb"));
                                    profile_Caption.add(jsonObject
                                            .getString("caption"));
                                    profile_Conversation.add(jsonObject
                                            .getString("conversation"));
                                    profile_Date.add(jsonObject
                                            .getString("aackdate"));
                                    profile_Pic.add(jsonObject
                                            .getString("userpic"));
                                    profile_userid.add(jsonObject
                                            .getString("userid"));
                                    profile_aackid.add(jsonObject
                                            .getString("aackid"));
                                    profile_Aackcontent.add(jsonObject
                                            .getString("aackcontent"));
                                    profile_nickname.add(jsonObject
                                            .getString("username"));
                                    profile_username.add(jsonObject
                                            .getString("nickname"));
                                    strfollowstatus = jsonObject
                                            .getString("followstatus");

                                    shareType = jsonObject
                                            .getString("sharetype");
                                    profile_sharetype.add(shareType);

                                    profile_resharestatus.add(jsonObject
                                            .getBoolean("reshareStatus"));

                                }
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lst_profile
                                                .removeFooterView(footerView);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Network Error",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (str.equals("3")) {
                    count = 0;
                    if (!loginResponse.equals("zero")) {
                        try {
                            JSONObject jobject = new JSONObject(loginResponse);
                            strFollowingCount = jobject
                                    .getString("followingcount");

                            JSONArray jsonArray = jobject
                                    .getJSONArray("useraacks");

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    count++;// added for aptr elements
                                    JSONObject jsonObject = jsonArray
                                            .getJSONObject(i);

                                    profile_aackthumb.add(jsonObject
                                            .getString("aackthumb"));
                                    profile_Caption.add(jsonObject
                                            .getString("caption"));
                                    profile_Conversation.add(jsonObject
                                            .getString("conversation"));
                                    profile_Date.add(jsonObject
                                            .getString("aackdate"));
                                    profile_Pic.add(jsonObject
                                            .getString("userpic"));
                                    profile_userid.add(jsonObject
                                            .getString("userid"));
                                    profile_aackid.add(jsonObject
                                            .getString("aackid"));
                                    profile_Aackcontent.add(jsonObject
                                            .getString("aackcontent"));
                                    profile_nickname.add(jsonObject
                                            .getString("username"));
                                    profile_username.add(jsonObject
                                            .getString("nickname"));
                                    strfollowstatus = jsonObject
                                            .getString("followstatus");

                                    shareType = jsonObject
                                            .getString("sharetype");
                                    profile_sharetype.add(shareType);
                                    profile_resharestatus.add(jsonObject
                                            .getBoolean("reshareStatus"));

                                }
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lst_profile
                                                .removeFooterView(footerView);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Network Error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            try {
                DisplayImageOptions optionscustomimge = new DisplayImageOptions.Builder()
                        .cacheInMemory(true).cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        .build();

                if (profileimg) {
                    ImageLoader.getInstance().displayImage(
                            strProfilepic.replace(" ", "%20"), imgProfilePic,
                            optionscustomimge, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strUsername == null) {
                prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                strUsername = prefs.getString("username", "0");
            }

            txtName.setText(strFullname);
            txtSubname.setText("@" + strUsername);
            txtFollowersCount.setText(strFollowersCount);
            txtFollowingCount.setText(strFollowingCount);

            txtAacksCount.setText(strAacksCount);

            if (isFavs_first) {
                profile_adapter = new ProfileAdapter(getActivity(),
                        R.layout.homelistitem, strUsername,
                        profile_Conversation, profile_Caption, profile_Date,
                        profile_Pic, strFullname, profile_aackthumb,
                        profile_aackid, strUserId, profile_sharetype,
                        reaackowner_fullname, reaackowner_username);
                lst_profile.addFooterView(footerView);
                footerView.setVisibility(View.GONE);
                lst_profile.setAdapter(profile_adapter);
            } else {
                if (profile_adapter != null)
                    profile_adapter.notifyDataSetChanged();
            }
            if (isFavs_first) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            isFavs_first = false;
            lodingForLatestMessages = false;
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }// async..........

    // To get Date & Time
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // for loading 10 by 10.
    public class EndlessScrollListenerFavs implements OnScrollListener {
        public EndlessScrollListenerFavs() {
        }

        public EndlessScrollListenerFavs(int visibleThreshold) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                if (lst_profile.getLastVisiblePosition() == lst_profile
                        .getCount() - 1) {
                    if (!lodingForLatestMessages) {
                        new doInProfileListData(lst_profile.getCount()
                                - lst_profile.getFooterViewsCount()).execute();
                    }
                }
            }
        }
    }

    // when called settings button
    public void showDialog_Settings() {
        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final Button edit = (Button) dialog.findViewById(R.id.edit);
        final Button logout = (Button) dialog.findViewById(R.id.logout);

        // When called Logout in setting dialog
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean checkbakup = false;
                if (prefs != null) {
                    checkbakup = prefs.getBoolean("checkbackup", false);
                }

                if (checkbakup) {
                    if (CheckInternetConnection.isOnline(getActivity())) {
                        LogoutTask task = new LogoutTask();
                        task.execute();
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), DataUrls.dialogtitle,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showDialog_Logout();
                }
            }
        });

        // When called Edit in settings dialog
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CheckInternetConnection.isOnline(getActivity())) {
                    UpdateTask task = new UpdateTask();
                    task.execute();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), DataUrls.dialogtitle,
                            Toast.LENGTH_SHORT).show();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.show();
    }

    // When called Logout in setting dialog
    public void showDialog_Logout() {
        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logoutcheck_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final RelativeLayout ok = (RelativeLayout) dialog.findViewById(R.id.ok);
        final RelativeLayout cancel = (RelativeLayout) dialog
                .findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CheckInternetConnection.isOnline(getActivity())) {
                    LogoutTask task = new LogoutTask();
                    task.execute();
                    try {
                        MessagesFragment.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), DataUrls.dialogtitle,
                            Toast.LENGTH_SHORT).show();
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    // Service for Logout
    public class LogoutTask extends AsyncTask<URL, Integer, Long> {
        protected void onPreExecute() {
            dialog = MyProgressDialog.show(getActivity(), null, null);
        }

        @Override
        protected Long doInBackground(URL... params) {
            try {
                logout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                MessagesFragment.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (strMessage.equals("Logout Success")) {
                    // To stop Background service
                    getActivity().stopService(
                            new Intent(getActivity(),
                                    BackupMessagesService.class));
                    getActivity().stopService(
                            new Intent(getActivity(), ContactsService.class));
                    BackupMessagesService.check = false;

                    // To close the Facebook session
                    if (DataUrls.providers.contains("fb")) {
//                        try {
//                            if (Session.getActiveSession() != null) {
//                                Session.getActiveSession()
//                                        .closeAndClearTokenInformation();
//                            }
//                            Session.setActiveSession(null);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    // To Logout from the Twitter session
                    if (DataUrls.providers.contains("tweet")) {
                        try {
//                            MessageShareActivity.twitt.logout();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // To Logout from the tumblr
                    if (DataUrls.providers.contains("tumblr")) {
                        try {
//                            TumblrLoginActivity.logOutOfTumblr(getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // To Logout from the imgur
                    if (DataUrls.providers.contains("imgur")) {
                        try {
//                            ImgurAuthorization.getInstance().logout();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    SharedPreferences preferences = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userid", null);
                    editor.putString("checkvalue", null);
                    editor.putString("username", null);
                    editor.clear();
                    System.gc();

                    editor.putBoolean("demo", true);
                    editor.commit();

                    Intent i = new Intent(getActivity().getBaseContext(),
                            SignInSignupActivity.class);
                    getActivity().startActivity(i);
                    getActivity().finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // This method is used logout if the user login through social fields
    public void logout() {
        String response = null;
        if (CheckInternetConnection.isOnline(getActivity())) {
            response = UrltoValue.getValuefromUrl(DataUrls.logout + "userid="
                    + strUserId);
        } else {
            Toast.makeText(getActivity(), DataUrls.dialogtitle,
                    Toast.LENGTH_SHORT).show();
        }
        try {
            JSONObject jsonOb = new JSONObject(response);
            strMessage = jsonOb.getString("message");

//            if (SignInActivity.logintype.equals("4")) {
//                try {
//                    SignInActivity.twitt.logout();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (SignInActivity.logintype.equals("2")) {
//                try {
//                    if (Session.getActiveSession() != null) {
//                        Session.getActiveSession()
//                                .closeAndClearTokenInformation();
//                    }
//                    Session.setActiveSession(null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Async class for update the changes
    public class UpdateTask extends AsyncTask<URL, Integer, Long> {
        protected void onPreExecute() {
            dialog = MyProgressDialog.show(getActivity(), null, null);
        }

        @Override
        protected Long doInBackground(URL... params) {
            try {
                String strResponse_update = UrltoValue
                        .getValuefromUrl(DataUrls.userprofile_update
                                + "userid=" + strUserId);
                JSONObject jsonObj = new JSONObject(strResponse_update);

                strFirstname_Update = jsonObj.getString("firstname");
                strLastname_Update = jsonObj.getString("lastname");
                strUsername_Update = jsonObj.getString("username");
                strEmail_Update = jsonObj.getString("email");
                strProfilepic_Update = jsonObj.getString("profilepic");
                strPassword_Update = jsonObj.getString("password");
                strLogintype_Update = jsonObj.getString("logintype");
                strNum_Update = jsonObj.getString("number");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(),
                    UpdateProfileActivity.class);
            intent.putExtra("firstname", strFirstname_Update);
            intent.putExtra("lastname", strLastname_Update);
            intent.putExtra("username", strUsername_Update);
            intent.putExtra("email", strEmail_Update);
            intent.putExtra("profilepic", strProfilepic_Update);
            intent.putExtra("password", strPassword_Update);
            intent.putExtra("logintype", strLogintype_Update);
            intent.putExtra("number", strNum_Update);
            intent.putExtra("userid", strUserId);
            startActivity(intent);
        }
    }

    // Async class for deleting aacks
    class deleteaack extends AsyncTask<URL, Integer, Long> {
        long aackid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = MyProgressDialog.show(getActivity(), null, null);
        }

        @Override
        protected Long doInBackground(URL... params) {
            strUserId = prefs.getString("userid", "");
            try {

                for (int i = 0; i < array.size(); i++) {

                    if (profile_sharetype.get(array.get(i)).equals("reshare")
                            && profile_resharestatus.get(array.get(i))) {

                        searchresponse = UrltoValue
                                .getValuefromUrl(DataUrls.delete_reshareaack
                                        + "userid="
                                        + strUserId
                                        + "&aackid="
                                        + Long.parseLong(profile_aackid
                                        .get(array.get(i))));
                    } else {

                        searchresponse = UrltoValue
                                .getValuefromUrl(DataUrls.delete_aack
                                        + "userid="
                                        + strUserId
                                        + "&aackid="
                                        + Long.parseLong(profile_aackid
                                        .get(array.get(i))));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            dialog.dismiss();

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            Fragment newFragment = profilefrag;
            profilefrag.onDestroy();
            ft.remove(profilefrag);
            ft.replace(((ViewGroup) getView().getParent()).getId(), newFragment);
            // container is the ViewGroup of current fragment
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    // To get the current view position in listview
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    // To know the view is checked or Not
    private boolean isCheckedOrNot(CheckBox checkbox) {
        if (checkbox.isChecked())
            return true;
        else
            return false;
    }

    // Alert for to delete selected aacks
    void alertDelete() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // You Can Customise your Title here
        alertDialog.setTitle("Aack Aack");
        alertDialog.setMessage("Are you sure you want to delete?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        new deleteaack().execute();
                        dialog.cancel();
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        FragmentManager manager = getActivity()
                                .getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        Fragment newFragment = profilefrag;
                        profilefrag.onDestroy();
                        ft.remove(profilefrag);
                        ft.replace(((ViewGroup) getView().getParent()).getId(),
                                newFragment);
                        // container is the ViewGroup of current fragment
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
        alertDialog.show();
    }
}