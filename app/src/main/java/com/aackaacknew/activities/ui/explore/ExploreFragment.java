package com.aackaacknew.activities.ui.explore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aackaacknew.activities.Home;
import com.aackaacknew.activities.MainActivity;
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.R;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.ImageLoader_homeGrid;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExploreFragment extends Fragment implements View.OnClickListener {

    private ExploreViewModel exploreViewModel;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        exploreViewModel =
//                ViewModelProviders.of(this).get(ExploreViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_explore, container, false);
//        final TextView textView = root.findViewById(R.id.text_explore);
//        exploreViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }



    Button btn_menu, searchimage, cross_btn;
    public boolean mainlayout_visible, checkbakup;
    MainLayout mLayout;
    MyProgressDialog dialog;
    String strUserId, searchby;
    FollowAdapter followAdapter;
    ListView lst_explore, lst_explore_search;
    View footerView;
    String searchresponse;
    ImageLoader_homeGrid loader2;
    EditText edtSearchs;
//    RelativeLayout menulayout, relBack;
    public static String homeaackid, homeuserid, homepic, homename, homeaack,
            homestrname, homestrusername, homeconversation, homecaption,
            homelikestatue;
    public static boolean homeresharestatus;
    boolean isFavs_first, loading_favs, issearch;
    int count, favs_scrollcount = 0, previousTotal_favs = 0;
    List<String> pic_List, name_List, userid_List, caption_List, image_List,
            relation_List, nickname_List, time_List, fullimage_List,
            aackId_List, pic_List_temp, name_List_temp, userid_List_temp,
            caption_List_temp, image_List_temp, relation_List_temp,
            nickname_List_temp, time_List_temp, fullimage_List_temp,
            aackId_List_temp, sharetype_List, sharetype_List_temp;
    List<Boolean> resharestatus_List, resharestatus_List_temp;

    Handler handler;
    SharedPreferences prefs;
    boolean loadingForLatestMessages, loadingForsearch;
    int explore = 0;

    RelativeLayout rlsendbtn;
    Button send;
    boolean explore_search;
    static Activity mActivity;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.follow, null);
        DataUrls.initImageLoader(getActivity());

        explore_search = false;
        isFavs_first = true;
        loading_favs = true;
        issearch = true;
        mainlayout_visible = true;

        pic_List = new ArrayList<String>();
        name_List = new ArrayList<String>();
        userid_List = new ArrayList<String>();
        caption_List = new ArrayList<String>();
        image_List = new ArrayList<String>();
        relation_List = new ArrayList<String>();
        nickname_List = new ArrayList<String>();
        time_List = new ArrayList<String>();
        fullimage_List = new ArrayList<String>();
        aackId_List = new ArrayList<String>();
        sharetype_List = new ArrayList<String>();
        resharestatus_List = new ArrayList<Boolean>();

        pic_List_temp = new ArrayList<String>();
        name_List_temp = new ArrayList<String>();
        userid_List_temp = new ArrayList<String>();
        caption_List_temp = new ArrayList<String>();
        image_List_temp = new ArrayList<String>();
        relation_List_temp = new ArrayList<String>();
        nickname_List_temp = new ArrayList<String>();
        time_List_temp = new ArrayList<String>();
        fullimage_List_temp = new ArrayList<String>();
        aackId_List_temp = new ArrayList<String>();
        sharetype_List_temp = new ArrayList<String>();
        resharestatus_List_temp = new ArrayList<Boolean>();

        // To get the all id's
//        btn_menu = (Button) view.findViewById(R.id.menu);
        lst_explore = (ListView) view.findViewById(R.id.listnew);
//        HomeDetailsActivity.check_home = true;
        footerView = inflater.inflate(R.layout.listfooter, null);
        searchimage = (Button) view.findViewById(R.id.search_btn);
        cross_btn = (Button) view.findViewById(R.id.cross_btn);
        edtSearchs = (EditText) view.findViewById(R.id.edtsearch);
//        relBack = (RelativeLayout) view.findViewById(R.id.relback);
        lst_explore_search = (ListView) view.findViewById(R.id.listnew_search);

//        rlsendbtn = (RelativeLayout) view.findViewById(R.id.rlsendbtn);
//        send = (Button) view.findViewById(R.id.sendbtn);

        searchimage.setOnClickListener(ExploreFragment.this);
        cross_btn.setOnClickListener(ExploreFragment.this);
        lst_explore.setVisibility(View.VISIBLE);
        lst_explore_search.setVisibility(View.GONE);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLayout = Home.mLayout;

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        followAdapter = new FollowAdapter(getActivity(), pic_List_temp,
                name_List_temp, userid_List_temp, caption_List_temp,
                image_List_temp, relation_List_temp, nickname_List_temp,
                time_List_temp, fullimage_List_temp, aackId_List_temp,
                sharetype_List_temp);

        footerView.setVisibility(View.GONE);
        lst_explore.setScrollingCacheEnabled(false);
        lst_explore_search.setScrollingCacheEnabled(false);

        // when called default search button of the device
        edtSearchs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    toSearch();// To Search (When called Search image in
                    // edittext/default search button)
                }
                return false;
            }
        });

        // TextChangedListener for edittext
        edtSearchs.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().trim().length() == 0) {
                    if (explore_search) {
                        cross_btn.callOnClick();
                    }
                    explore_search = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when click on edittext
        edtSearchs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mLayout.isMenuShown()) {
                    mLayout.toggleMenu();
                }
            }
        });

        // when click on send button to send logcat file
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendmail();
//            }
//        });

        // To Hide/Show the Menu
//        relBack.setOnClickListener(new View.OnClickListener() {
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
        // When called Listview Item(Explore_aack)
        lst_explore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                        //TODO add details page
//                    HomeDetailsActivity.comments.clear();
//                    HomeDetailsActivity.commentspic.clear();
//                    HomeDetailsActivity.time.clear();
//                    HomeDetailsActivity.commentersname.clear();
//                    HomeDetailsActivity.likersname.clear();
//                    HomeDetailsActivity.likerspic.clear();
//                    HomeDetailsActivity.comment_time.clear();
//
//                    Intent profiledet = new Intent(getActivity(),
//                            HomeDetailsActivity.class);

                    homeaackid = aackId_List_temp.get(arg2);
                    homepic = pic_List_temp.get(arg2);
                    homeaack = fullimage_List_temp.get(arg2);
                    homeuserid = userid_List_temp.get(arg2);
                    homestrname = nickname_List_temp.get(arg2);
                    homestrusername = name_List_temp.get(arg2);
                    homecaption = caption_List_temp.get(arg2);
                    homeresharestatus = resharestatus_List.get(arg2);

//                    HomeFragment.follow_status_check = true;
//                    startActivity(profiledet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // When called Listview Item(Explore_search result aack)
        lst_explore_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
//                    HomeDetailsActivity.comments.clear();
//                    HomeDetailsActivity.commentspic.clear();
//                    HomeDetailsActivity.time.clear();
//                    HomeDetailsActivity.commentersname.clear();
//                    HomeDetailsActivity.likersname.clear();
//                    HomeDetailsActivity.likerspic.clear();
//                    HomeDetailsActivity.comment_time.clear();
//
//                    Intent profiledet = new Intent(getActivity(),
//                            HomeDetailsActivity.class);
//                    homeaackid = aackId_List_temp.get(arg2);
//                    homepic = pic_List_temp.get(arg2);
//                    homeaack = fullimage_List_temp.get(arg2);
//                    homeuserid = userid_List_temp.get(arg2);
//                    homestrname = nickname_List_temp.get(arg2);
//                    homestrusername = name_List_temp.get(arg2);
//                    homecaption = caption_List_temp.get(arg2);
//                    homeresharestatus = resharestatus_List.get(arg2);
//
//                    HomeFragment.follow_status_check = true;
//                    startActivity(profiledet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lst_explore.addFooterView(footerView);
        lst_explore_search.addFooterView(footerView);

        lst_explore.setAdapter(followAdapter);
        lst_explore_search.setAdapter(followAdapter);

//        lst_explore.setOnScrollListener(new EndlessScrollListenerFavs());
//        lst_explore_search.setOnScrollListener(new EndlessScrollListenerFavs());

//        if (MessagesFragment.progress != null
//                && MessagesFragment.progress.isShowing()) {
//            MessagesFragment.progress.dismiss();
//        }
        if (CheckInternetConnection.isOnline(getActivity())) {
            FollowDob dob = new FollowDob(0);
            dob.execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connectivity",
                    Toast.LENGTH_LONG).show();
        }

        return view;
    }// oncreateview

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT)
                    .show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getActivity(), "portrait", Toast.LENGTH_SHORT)
                    .show();
        }
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(getActivity(), "keyboard visible",
                    Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(getActivity(), "keyboard hidden", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // AsyncTask class for Explore Aacks(aack shared by users)
    class FollowDob extends AsyncTask<URL, Integer, Long> {
        JSONArray ja;
        JSONObject jo, jo1, jo2;
        int msgscount;

        public FollowDob(int msgscount) {
            super();
            this.msgscount = msgscount;
            loadingForLatestMessages = true;
        }

        public void onPreExecute() {

            if (isFavs_first) {
                footerView.setVisibility(View.GONE);
                dialog = MyProgressDialog.show(getActivity(), null, null);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }

        protected Long doInBackground(URL... arg0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            strUserId = prefs.getString("userid", "");
            try {
                String url = DataUrls.search
                        + "userid=" + strUserId + "&start=" + msgscount
                        + "&devicedatetime="
                        + currentDateandTime.replace(" ", "%20");
                searchresponse = UrltoValue.getValuefromUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            try {
                count = 0;
                jo1 = new JSONObject(searchresponse);
                ja = jo1.getJSONArray("users");

                for (int i = 0; i < ja.length(); i++) {
                    count++;
                    jo2 = ja.getJSONObject(i);
                    pic_List.add(jo2.getString("profilepic"));
                    name_List.add(jo2.getString("username"));
                    userid_List.add(jo2.getString("userid"));
                    caption_List.add(jo2.getString("caption"));
                    image_List.add(jo2.getString("aackthumb"));
                    relation_List.add(jo2.getString("conversationfrom"));
                    nickname_List.add(jo2.getString("nickname"));
                    time_List.add(jo2.getString("datetime"));
                    fullimage_List.add(jo2.getString("aackimage"));
                    aackId_List.add(jo2.getString("aackid"));
                    sharetype_List.add(jo2.getString("sharetype"));
                    resharestatus_List.add(jo2.getBoolean("reshareStatus"));
                }
                pic_List_temp.clear();
                name_List_temp.clear();
                userid_List_temp.clear();
                caption_List_temp.clear();
                image_List_temp.clear();
                relation_List_temp.clear();
                nickname_List_temp.clear();
                time_List_temp.clear();
                fullimage_List_temp.clear();
                aackId_List_temp.clear();
                sharetype_List_temp.clear();
                resharestatus_List_temp.clear();

                for (String string : pic_List) {
                    pic_List_temp.add(string);
                }
                for (String string : name_List) {
                    name_List_temp.add(string);
                }
                for (String string : aackId_List) {
                    aackId_List_temp.add(string);
                }
                for (String string : fullimage_List) {
                    fullimage_List_temp.add(string);
                }
                for (String string : time_List) {
                    time_List_temp.add(string);
                }
                for (String string : nickname_List) {
                    nickname_List_temp.add(string);
                }
                for (String string : relation_List) {
                    relation_List_temp.add(string);
                }
                for (String string : image_List) {
                    image_List_temp.add(string);
                }
                for (String string : caption_List) {
                    caption_List_temp.add(string);
                }
                for (String string : userid_List) {
                    userid_List_temp.add(string);
                }
                for (String string : sharetype_List) {
                    sharetype_List_temp.add(string);
                }
                for (boolean string : resharestatus_List) {
                    resharestatus_List_temp.add(string);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    followAdapter.notifyDataSetChanged();
                }
            });

            try {
                if (isFavs_first) {
                    dialog.dismiss();
                } else {
                    lst_explore.removeFooterView(footerView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFavs_first = false;
            loadingForLatestMessages = false;

        }
    }

    // FollowAdapter ------------------------------------
    class FollowAdapter extends BaseAdapter {
        private Context mContext;
        List<String> pic_List_temp, name_List_temp, userid_List_temp,
                caption_List_temp, image_List_temp, relation_List_temp,
                nickname_List_temp, time_List_temp, fullimage_List_temp,
                aackId_List_temp, sharetype;

        public FollowAdapter(Context c, List<String> pic_List_temp,
                             List<String> name_List_temp, List<String> userid_List_temp,
                             List<String> caption_List_temp, List<String> image_List_temp,
                             List<String> relation_List_temp,
                             List<String> nickname_List_temp, List<String> time_List_temp,
                             List<String> fullimage_List_temp,
                             List<String> aackId_List_temp, List<String> sharetype_List) {
            mContext = c;
            this.pic_List_temp = pic_List_temp;
            this.name_List_temp = name_List_temp;
            this.userid_List_temp = userid_List_temp;
            this.caption_List_temp = caption_List_temp;
            this.image_List_temp = image_List_temp;
            this.relation_List_temp = relation_List_temp;
            this.nickname_List_temp = nickname_List_temp;
            this.time_List_temp = time_List_temp;
            this.fullimage_List_temp = fullimage_List_temp;
            this.aackId_List_temp = aackId_List_temp;
            this.sharetype = sharetype_List;

            loader2 = new ImageLoader_homeGrid(mContext);
        }

        public int getCount() {
            return name_List_temp.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            LayoutInflater inflater = getActivity().getLayoutInflater();
            row = inflater.inflate(R.layout.homelistitem, null);
            FetchHolder holder = new FetchHolder(row);

            Typeface typeFace2 = Typeface.createFromAsset(getActivity()
                    .getAssets(), "fonts/Roboto-Medium.ttf");
            Typeface typeFace = Typeface.createFromAsset(getActivity()
                    .getAssets(), "fonts/Roboto-Regular.ttf");

            holder.txtNamep.setTypeface(typeFace);
            holder.txtusername.setTypeface(typeFace);
            holder.txtDesc.setTypeface(typeFace);
            holder.txtDate.setTypeface(typeFace2);
            holder.txtRelation2.setTypeface(typeFace);
            holder.txtNamep.setText(nickname_List_temp.get(position));
            holder.txtusername.setText("@" + name_List_temp.get(position));

            if (sharetype.get(position).equals("myshare")) {
                holder.imgreaack.setVisibility(View.GONE);
            } else if (sharetype.get(position).equals("reshare")) {
                holder.imgreaack.setVisibility(View.VISIBLE);
            }

            if (caption_List_temp.get(position).contains("#")) {
                ForegroundColorSpan span = new ForegroundColorSpan(
                        Color.parseColor("#41749b"));
                SpannableString s1 = new SpannableString(
                        caption_List_temp.get(position));
                String[] s2 = caption_List_temp.get(position).split(" ");

                int currIndex = 0;
                for (String word : s2) {
                    if (word.startsWith("#")) {
                        s1.setSpan(span, currIndex, currIndex + word.length(),
                                0);
                    }
                    currIndex += (word.length() + 1);
                }
                holder.txtDesc.setText(s1);
            } else {
                holder.txtDesc.setTextColor(Color.parseColor("#999999"));
                holder.txtDesc.setText(caption_List_temp.get(position));
            }
            holder.txtDate.setText(time_List_temp.get(position));
            holder.txtRelation2.setText(relation_List_temp.get(position));

            try {
                DisplayImageOptions optionscustomimge = new DisplayImageOptions.Builder()
                        .cacheInMemory(true).cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        .displayer(new RoundedVignetteBitmapDisplayer(300, 4))
                        .build();

                ImageLoader.getInstance().displayImage(
                        pic_List_temp.get(position), holder.imgPic,
                        optionscustomimge, null);
                loader2.DisplayImage(image_List_temp.get(position),
                        holder.aackcontentimv);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }
    } // FollowAdapter123

    class FetchHolder {
        private TextView txtNamep = null;
        private TextView txtDesc = null;
        private TextView txtDate = null;
        private TextView txtusername = null;
        private TextView txtRelation2 = null;
        private ImageView aackcontentimv = null;
        private ImageView imgPic = null, imgreaack = null;

        FetchHolder(View row) {
            txtNamep = (TextView) row.findViewById(R.id.name);
            txtusername = (TextView) row.findViewById(R.id.subname);
            txtDesc = (TextView) row.findViewById(R.id.caption);
            aackcontentimv = (ImageView) row.findViewById(R.id.aack);
            txtDate = (TextView) row.findViewById(R.id.time);
            imgPic = (ImageView) row.findViewById(R.id.image);
            txtRelation2 = (TextView) row.findViewById(R.id.conversation);
            imgreaack = (ImageView) row.findViewById(R.id.reaack);
        }
    } // Fetchadapter

    @Override
    public void onClick(View v) {
        // when called search icon in edittext
        if (v.equals(searchimage)) {
            toSearch();// To Search (When called Search image in
            // edittext/default search button)
        } else if (v.equals(cross_btn)) {
            ExploreFragment fragment = new ExploreFragment();
//            fragmentTransaction(fragment);
            explore_search = false;
        }
    }

    // To transfer from one fragment to another fragment
//    public void fragmentTransaction(Fragment fragment) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.activity_main_content_fragment, fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//    }

    // AsyncTask class for search by using username/hashtag
    class Doinsearch extends AsyncTask<URL, Integer, Long> {
        int msgscount;

        public Doinsearch(int msgscount) {
            super();
            this.msgscount = msgscount;
            loadingForsearch = true;
        }

        public void onPreExecute() {
            if (issearch) {
                footerView.setVisibility(View.GONE);
                dialog = MyProgressDialog.show(getActivity(), null, null);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Long doInBackground(URL... arg0) {
            if (CheckInternetConnection.isOnline(getActivity())) {
                count = 0;
                JSONObject jo2;
                JSONArray ja2 = null;

                if (searchby.startsWith("#")) {

                    String hashtagresponse = null;
                    try {
                        // Service for search by hashtag
                        String url = DataUrls.exploresearch
                                + URLEncoder.encode(searchby, "utf-8")
                                .trim() + "&userid="
                                + strUserId + "&start=" + msgscount;
                        hashtagresponse = UrltoValue
                                .getValuefromUrl(url);
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        if (!hashtagresponse.equalsIgnoreCase("null")
                                || hashtagresponse.equalsIgnoreCase(null)) {
                            ja2 = new JSONArray(hashtagresponse);

                            if (ja2.length() == 0) {
                                try {
                                    hashtagresponse = UrltoValue
                                            .getValuefromUrl(DataUrls.exploresearch
                                                    + URLEncoder
                                                    .encode(searchby
                                                                    .replace(
                                                                            " ",
                                                                            "")
                                                                    .replace(
                                                                            "#",
                                                                            "")
                                                                    .trim(),
                                                            "utf-8")
                                                    .trim()
                                                    + "&userid="
                                                    + strUserId
                                                    + "&start=" + msgscount);

                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e1) {
                        ja2 = new JSONArray();
                        e1.printStackTrace();
                    }

                    try {
                        if (!hashtagresponse.equalsIgnoreCase("null")
                                || !hashtagresponse.equalsIgnoreCase(null)) {
                            ja2 = new JSONArray(hashtagresponse);
                        }
                    } catch (Exception e) {
                        ja2 = new JSONArray();
                        e.printStackTrace();
                    }
                    for (int i = 0; i < ja2.length(); i++) {
                        try {
                            jo2 = ja2.getJSONObject(i);
                            pic_List_temp.add(jo2.getString("profilepic"));
                            name_List_temp.add(jo2.getString("username"));
                            userid_List_temp.add(jo2.getString("userid"));
                            caption_List_temp.add(jo2.getString("caption"));
                            image_List_temp.add(jo2.getString("aackthumb"));
                            relation_List_temp.add(jo2
                                    .getString("conversationfrom"));
                            nickname_List_temp.add(jo2.getString("nickname"));
                            time_List_temp.add(jo2.getString("datetime"));
                            fullimage_List_temp.add(jo2.getString("aackimage"));
                            aackId_List_temp.add(jo2.getString("aackid"));
                            sharetype_List_temp.add(jo2.getString("sharetype"));

                            resharestatus_List_temp.add(jo2
                                    .getBoolean("reshareStatus"));
                            count++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if ((searchby.length() > 0)
                        && (!searchby.startsWith("#"))) {
                    String usernameresponse = null;
                    try {
                        // service for searcg by user
                        String url = DataUrls.exploresearch_users
                                + "userid=" + strUserId + "&user="
                                + URLEncoder.encode(searchby, "utf-8")
                                + "&start=" + msgscount;
                        usernameresponse = UrltoValue
                                .getValuefromUrl(url);

                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        if (!usernameresponse.equalsIgnoreCase("null")
                                || !usernameresponse.equalsIgnoreCase(null)) {
                            ja2 = new JSONArray(usernameresponse);
                            if (ja2.length() == 0) {
                                try {
                                    usernameresponse = UrltoValue
                                            .getValuefromUrl(DataUrls.exploresearch
                                                    + URLEncoder.encode(
                                                    searchby, "utf-8")
                                                    + "&userid="
                                                    + strUserId
                                                    + "&start=" + msgscount);

                                } catch (UnsupportedEncodingException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e1) {
                        ja2 = new JSONArray();
                        e1.printStackTrace();
                    }
                    try {
                        if (!usernameresponse.equalsIgnoreCase("null")
                                || !usernameresponse.equalsIgnoreCase(null)) {
                            ja2 = new JSONArray(usernameresponse);
                        }
                    } catch (Exception e) {
                        ja2 = new JSONArray();
                        e.printStackTrace();
                    }
                    for (int i = 0; i < ja2.length(); i++) {
                        try {
                            jo2 = ja2.getJSONObject(i);
                            pic_List_temp.add(jo2.getString("profilepic"));
                            name_List_temp.add(jo2.getString("username"));
                            userid_List_temp.add(jo2.getString("userid"));
                            caption_List_temp.add(jo2.getString("caption"));
                            image_List_temp.add(jo2.getString("aackthumb"));
                            relation_List_temp.add(jo2
                                    .getString("conversationfrom"));
                            nickname_List_temp.add(jo2.getString("nickname"));
                            time_List_temp.add(jo2.getString("datetime"));
                            fullimage_List_temp.add(jo2.getString("aackimage"));
                            aackId_List_temp.add(jo2.getString("aackid"));
                            sharetype_List_temp.add(jo2.getString("sharetype"));
                            resharestatus_List_temp.add(jo2
                                    .getBoolean("reshareStatus"));
                            count++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    followAdapter.notifyDataSetChanged();
                }
            });
            if (issearch) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lst_explore_search.removeFooterView(footerView);
            }
            issearch = false;
            loadingForsearch = false;
        }
    }

    // To Search (When called Search image in edittext/default search button)
    public void toSearch() {

        if (edtSearchs.getText().toString().trim().contains(" ")) {
            String[] searchtext = edtSearchs.getText().toString().trim()
                    .split(" ");
            searchby = searchtext[0];
        } else {
            searchby = edtSearchs.getText().toString().trim();
        }

        issearch = true;
        explore = 1;
        lst_explore.setVisibility(View.GONE);
        lst_explore_search.setVisibility(View.VISIBLE);
        explore_search = true;

        if (searchby.trim().length() != 0) {
            pic_List_temp.clear();
            name_List_temp.clear();
            userid_List_temp.clear();
            caption_List_temp.clear();
            image_List_temp.clear();
            relation_List_temp.clear();
            nickname_List_temp.clear();
            time_List_temp.clear();
            fullimage_List_temp.clear();
            aackId_List_temp.clear();
            sharetype_List_temp.clear();
            resharestatus_List_temp.clear();

            searchimage.setVisibility(View.GONE);
            cross_btn.setVisibility(View.VISIBLE);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    followAdapter.notifyDataSetChanged();
                }
            });
            Doinsearch serch = new Doinsearch(0);
            serch.execute();
        } else {
            pic_List_temp.clear();
            name_List_temp.clear();
            userid_List_temp.clear();
            caption_List_temp.clear();
            image_List_temp.clear();
            relation_List_temp.clear();
            nickname_List_temp.clear();
            time_List_temp.clear();
            fullimage_List_temp.clear();
            aackId_List_temp.clear();
            sharetype_List_temp.clear();
            resharestatus_List_temp.clear();

            for (String string : pic_List) {
                pic_List_temp.add(string);
            }
            for (String string : name_List) {
                name_List_temp.add(string);
            }
            for (String string : aackId_List) {
                aackId_List_temp.add(string);
            }
            for (String string : fullimage_List) {
                fullimage_List_temp.add(string);
            }
            for (String string : time_List) {
                time_List_temp.add(string);
            }
            for (String string : nickname_List) {
                nickname_List_temp.add(string);
            }
            for (String string : relation_List) {
                relation_List_temp.add(string);
            }
            for (String string : image_List) {
                image_List_temp.add(string);
            }
            try {
                for (String string : caption_List) {
                    caption_List_temp.add(string);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String string : userid_List) {
                userid_List_temp.add(string);
            }

            for (String string : sharetype_List) {
                sharetype_List_temp.add(string);
            }

            for (boolean string : resharestatus_List) {
                resharestatus_List_temp.add(string);
            }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    followAdapter.notifyDataSetChanged();
                }
            });
            if (lst_explore.getFooterViewsCount() == 1) {
                try {
                    lst_explore.removeFooterView(footerView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // for loading 10 by 10.
    public class EndlessScrollListenerFavs implements AbsListView.OnScrollListener {
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
                if (explore != 1) {

                    if (lst_explore.getLastVisiblePosition() == lst_explore
                            .getCount() - 1) {
                        if (!loadingForLatestMessages) {
                            new FollowDob(lst_explore.getCount()
                                    - lst_explore.getFooterViewsCount())
                                    .execute();
                        }
                    }
                } else {

                    if (lst_explore_search.getLastVisiblePosition() == lst_explore_search
                            .getCount() - 1) {
                        if (!loadingForsearch) {
                            new Doinsearch(lst_explore_search.getCount()
                                    - lst_explore_search.getFooterViewsCount())
                                    .execute();
                        }
                    }

                }
            }
        }
    }
}