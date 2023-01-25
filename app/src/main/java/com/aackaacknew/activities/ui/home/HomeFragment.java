package com.aackaacknew.activities.ui.home;

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
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.R;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aackaacknew.activities.MainActivity;
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.R;
import com.aackaacknew.activities.ui.explore.ExploreFragment;
import com.aackaacknew.adapters.HomeAdapter;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;
import com.aackaacknew.adapters.HomeAdapter;
import com.aackaacknew.utils.MyProgressDialog;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }


    public boolean mainlayout_visible = true, checkbakup;
    MainLayout mLayout;
    ListView lst_home;
    MyProgressDialog dialog;
    SharedPreferences prefs;
    HomeAdapter home_adapter;
//    ProgressBar pbar;
    String loginResponse, shareType = "";
    LinearLayout layoutDefault;
    boolean lodingForLatestMessages;
    public static boolean explore_home_check, follow_status_check, home_new;
    boolean isFavs_first, loading_favs;
    int count, favs_scrollcount = 0, previousTotal_favs = 0,
            nodata_explore = 0;
    RelativeLayout relBottom_msgs_loading;
    TextView defalutextview;

    ArrayList<String> home_Name, home_Subname, home_userid, home_Time,
            home_Pic, home_likestatus, aack_id, home_caption,
            home_conversation, home_aackcontent, home_aackthumb,
            home_sharetype;
    ArrayList<Boolean> home_resharestatus;
    View footerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, null);

        isFavs_first = true;
        loading_favs = true;

        home_Name = new ArrayList<String>();
        home_Subname = new ArrayList<String>();
        home_userid = new ArrayList<String>();
        home_Time = new ArrayList<String>();
        home_Pic = new ArrayList<String>();
        home_likestatus = new ArrayList<String>();
        aack_id = new ArrayList<String>();
        home_caption = new ArrayList<String>();
        home_conversation = new ArrayList<String>();
        home_aackcontent = new ArrayList<String>();
        home_aackthumb = new ArrayList<String>();

        home_sharetype = new ArrayList<String>();
        home_resharestatus = new ArrayList<Boolean>();
        // reaackowner_fullname = new ArrayList<String>();
        // //reaackowner_profilepic = new ArrayList<String>();
        // reaackowner_username = new ArrayList<String>();

        // To get the all id's
        lst_home = (ListView) view.findViewById(R.id.list);
//        pbar = (ProgressBar) view.findViewById(R.id.progressBar_top);
        layoutDefault = (LinearLayout) view
                .findViewById(R.id.defalutextviewlayout);
        relBottom_msgs_loading = (RelativeLayout) view
                .findViewById(R.id.bottomlayout);


        defalutextview = (TextView) view.findViewById(R.id.defalutextview);

        footerView = ((LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter,
                null, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLayout = Home.mLayout;

        lst_home.setScrollingCacheEnabled(false);

        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf");
        defalutextview.setTypeface(typeFace);

        // To Hide/Show the Menu
//        relBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // hide key board
//                final InputMethodManager imm = (InputMethodManager) getActivity()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//
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
        lst_home.setOnScrollListener(new EndlessScrollListenerFavs());

        if (CheckInternetConnection.isOnline(getActivity())) {
            doInHome home = new doInHome(0);
            home.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(getActivity(), DataUrls.dialogtitle,
                    Toast.LENGTH_SHORT).show();
        }
        // When called List Item(Home_Aack)
        lst_home.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
//                    Intent profiledet = new Intent(getActivity(),
//                            HomeDetailsActivity.class);
//
//                    explore_home_check = true;
//                    home_new = true;
//
//                    HomeDetailsActivity.comments.clear();
//                    HomeDetailsActivity.commentspic.clear();
//                    HomeDetailsActivity.time.clear();
//                    HomeDetailsActivity.commentersname.clear();
//                    HomeDetailsActivity.likersname.clear();
//                    HomeDetailsActivity.likerspic.clear();
//                    HomeDetailsActivity.comment_time.clear();
//
//                    profiledet.putExtra("userid", home_userid.get(arg2));
//                    profiledet.putExtra("aack", home_aackcontent.get(arg2));
//                    profiledet.putExtra("name", home_Name.get(arg2));
//                    profiledet.putExtra("username", home_Subname.get(arg2));
//                    profiledet.putExtra("pic", home_Pic.get(arg2));
//                    profiledet.putExtra("aackid", aack_id.get(arg2));
//
//                    ExploreFragment.homeaackid = aack_id.get(arg2);
//                    ExploreFragment.homepic = home_Pic.get(arg2);
//                    ExploreFragment.homename = home_Subname.get(arg2);
//                    ExploreFragment.homeaack = home_aackcontent.get(arg2);
//                    ExploreFragment.homeuserid = home_userid.get(arg2);
//                    ExploreFragment.homelikestatue = home_likestatus.get(arg2);
//                    ExploreFragment.homestrname = home_Name.get(arg2);
//                    ExploreFragment.homestrusername = home_Subname.get(arg2);
//                    ExploreFragment.homeconversation = home_conversation
//                            .get(arg2);
//                    ExploreFragment.homecaption = home_caption.get(arg2);
//                    ExploreFragment.homeresharestatus = home_resharestatus
//                            .get(arg2);
//
//                    follow_status_check = true;
//                    startActivity(profiledet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // when click on text to go Explore
        layoutDefault.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ExploreFragment fragment = new ExploreFragment();
//                ft.replace(R.id.activity_main_content_fragment, fragment);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });
        return view;
    }// oncreateview..

    // To get the followers..
    class doInHome extends AsyncTask<URL, Integer, Long> {
        JSONArray jsonArray_home = new JSONArray();
        int msgscount;

        public doInHome(int msgscount) {
            super();
            this.msgscount = msgscount;
            lodingForLatestMessages = true;
        }

        protected void onPreExecute() {
            if (isFavs_first) {
                relBottom_msgs_loading.setVisibility(View.GONE);
                dialog = MyProgressDialog.show(getActivity(), null, null);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Long doInBackground(URL... arg0) {
            try {
                String strDate = getDateTime();
                prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                String strUserId = prefs.getString("userid", "");

                loginResponse = UrltoValue.getValuefromUrl(DataUrls.home
                        + "userid=" + strUserId + "&devicedatetime="
                        + strDate.replaceAll(" ", "%20") + "&start="
                        + msgscount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            count = 0;
            if (!loginResponse.equals("zero") && loginResponse != null) {
                try {
                    JSONObject jobject = new JSONObject(loginResponse);
                    jsonArray_home = jobject.getJSONArray("useraacks");
                    if (jsonArray_home.length() != 0) {
                        nodata_explore++;
                        for (int i = 0; i < jsonArray_home.length(); i++) {
                            count++;
                            JSONObject jsonObject = jsonArray_home
                                    .getJSONObject(i);
                            home_Name.add(jsonObject.getString("nickname"));
                            home_Subname.add(jsonObject.getString("username"));
                            home_userid.add(jsonObject.getString("userid"));
                            home_Pic.add(jsonObject.getString("userpic"));
                            home_Time.add(jsonObject.getString("aackdate"));
                            home_likestatus.add(jsonObject
                                    .getString("likestatus"));
                            aack_id.add(jsonObject.getString("aackid"));
                            home_caption.add(jsonObject.getString("caption"));
                            home_conversation.add(jsonObject
                                    .getString("conversation"));
                            home_aackcontent.add(jsonObject
                                    .getString("aackcontent"));
                            home_aackthumb.add(jsonObject
                                    .getString("aackthumb"));

                            shareType = jsonObject.getString("sharetype");
                            home_sharetype.add(shareType);
                            home_resharestatus.add(jsonObject
                                    .getBoolean("reshareStatus"));

                        }
                    } else {
                        lst_home.removeFooterView(footerView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Internal Error..",
                        Toast.LENGTH_LONG).show();
            }
            if (jsonArray_home.length() != 0) {
                layoutDefault.setVisibility(View.GONE);
                lst_home.setVisibility(View.VISIBLE);

                if (isFavs_first) {
                    home_adapter = new HomeAdapter(getActivity(),
                            R.layout.homelistitem, home_Name, home_Subname,
                            home_Time, home_Pic, home_conversation,
                            home_caption, home_aackthumb, home_sharetype);
                    lst_home.addFooterView(footerView);
                    footerView.setVisibility(View.GONE);
                    lst_home.setAdapter(home_adapter);
                } else {
                    home_adapter.notifyDataSetChanged();
                }
                if (isFavs_first) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                isFavs_first = false;
            } else {
                if (isFavs_first) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                count = 0;
                if (isFavs_first)
                    layoutDefault.setVisibility(View.VISIBLE);

                lst_home.removeFooterView(footerView);
            }
            relBottom_msgs_loading.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
            lodingForLatestMessages = false;

            if (nodata_explore == 0) {
                layoutDefault.setVisibility(View.VISIBLE);
                lst_home.setVisibility(View.GONE);
            }
        }
    }// async..........

    // To get Date && Time
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Endless Scroll Listener for loading 10 by 10
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
                if (lst_home.getLastVisiblePosition() == lst_home.getCount() - 1) {
                    if (!lodingForLatestMessages) {
                        new doInHome(lst_home.getCount()
                                - lst_home.getFooterViewsCount()).execute();
                    }
                }
            }
        }
    }
}