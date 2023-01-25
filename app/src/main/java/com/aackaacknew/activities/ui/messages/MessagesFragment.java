package com.aackaacknew.activities.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.WeakHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.aackaacknew.activities.BackupMessagesService;
import com.aackaacknew.activities.Home;
import com.aackaacknew.activities.MainActivity;
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.MessageDetailsActivity;
import com.aackaacknew.activities.R;
import com.aackaacknew.activities.Splash;
import com.aackaacknew.activities.ui.explore.ExploreFragment;
import com.aackaacknew.adapters.MessagesListAdapter;
import com.aackaacknew.adapters.MessagesListAdapter_new;
import com.aackaacknew.pojo.Contact;
import com.aackaacknew.pojo.Message;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.Constants;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;

import com.aackaacknew.activities.R;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        messagesViewModel =
//                ViewModelProviders.of(this).get(MessagesViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_messages, container, false);
//        final TextView textView = root.findViewById(R.id.text_messages);
//        messagesViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }




    Cursor cursor_messages;
    boolean msgs_latest_footer, check_dialog, lodingForLatestMessages;
    MainLayout mLayout;
//    ImageView search, refresh;
    ArrayList<String> lstname;
    static ListView lst_messages;
    ListView searchlist;
    String number = "";
    String latestmsg_res = "";
    static String strMyImagePath = "";
    Runnable runnable_beginn;
    int type, type1;
    String searchby;
    boolean checkbakup, boolsearch;
    MyProgressDialog dialog;
    public static boolean mainlayout_visible, checkmess, search_messages;
    public static ArrayList<String> list_body;
    static ArrayList<String> list_num;
    public static ArrayList<String> list_date;
    public static ArrayList<String> list_messtype;
    public static ArrayList<String> list_type;
    public static ArrayList<String> list_name;
    public static ArrayList<String> list_mmstext;
    public static boolean check_msg_click, check_cancelService;
    static Activity mActivity;
    View footerView;
    boolean isBackuping, readyToProceed, checkmms;
    public static boolean messagesload;
    private static  List<Message> messages;
    private Map<String, List<Message>> messages_conversation;
    static MessagesListAdapter load_msgs_adapter;
    static MessagesListAdapter_new load_msgs_adapter_new;
    SharedPreferences preferences;

    ArrayList<String> mmsmid2;
    ArrayList<String> mmsmid1;
    public static ArrayList<String> ArrTitle, ArrName, ArrDesc, ArrTime,
            ArrUrl, ArrType, ArrPhoneNum, ArrFullname, arrmesstype, Arrmedia,
            arrmmstext;
    ArrayList<String> mms_image, mms_add, mms_date, mms_type, mms_text,
            mms_lastbackup, mms_threadid;
    ArrayList<String> midcomp, lstAArrTime, messtype, mmstext, media, lstArrUr,
            lstarrdesc, lstArrPhoneNum, lstArrFullname;

    RelativeLayout relBottom_msgs_loading/*, relBack*/;
    SharedPreferences prefs;
    String strUserId = "", lastbackup = "";
    FrameLayout searchlayout;
    int count_msgs;
    boolean checkbackground;
    JSONArray json_latestmessages;
    EditText edtSearch;
    boolean check_footer;
    TextView tvtxt, tvtitle;
    Button searchBtn, crossBtn;

    int count, messages_scrollcount = 0;
    Thread thread;
    public static ProgressDialog progress;
    boolean search_edt, onclickrefresh;

    String TAG = "MessageFragment";

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages, null);

        boolsearch = false;
        mainlayout_visible = true;
        checkmess = false;
        search_messages = false;
        check_footer = true;
        messages_scrollcount = 0;
        search_edt = false;
        onclickrefresh = false;

        messages_conversation = Collections
                .synchronizedMap(new WeakHashMap<String, List<Message>>());
        lstname = new ArrayList<String>();
        mmsmid2 = new ArrayList<String>();
        mmsmid1 = new ArrayList<String>();
        midcomp = new ArrayList<String>();
        lstAArrTime = new ArrayList<String>();
        messtype = new ArrayList<String>();
        mmstext = new ArrayList<String>();
        media = new ArrayList<String>();
        lstArrUr = new ArrayList<String>();
        lstarrdesc = new ArrayList<String>();
        lstArrPhoneNum = new ArrayList<String>();
        lstArrFullname = new ArrayList<String>();

        footerView = ((LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.listfooter, null, false);
//        search = (ImageView) view.findViewById(R.id.searchnew);
        lst_messages = (ListView) view.findViewById(R.id.list);
        relBottom_msgs_loading = (RelativeLayout) view
                .findViewById(R.id.bottomlayout);
//        relBack = (RelativeLayout) view.findViewById(R.id.relback);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edtSearch = (EditText) view.findViewById(R.id.listsearch);
        searchlayout = (FrameLayout) view.findViewById(R.id.search);

        crossBtn = (Button) view.findViewById(R.id.crossBtn);
        tvtxt = (TextView) view.findViewById(R.id.tv_txt);
        tvtitle = (TextView) view.findViewById(R.id.tv_txttitle);
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        searchlist = (ListView) view.findViewById(R.id.search_list);
//        refresh = (ImageView) view.findViewById(R.id.load);

        Long size = prefs.getLong("messageslength", 0);

        if (size < 0) {
            size = 0L;
        }
        boolean sendingdata = prefs.getBoolean("sendingdata", false);
        Long smssize = prefs.getLong("uploadedLength", 0);

        if (sendingdata) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(Constants.MSG_LENGTH, 0);
            editor.commit();
            tvtitle.setText("");
            tvtxt.setText("SMS Backup " + smssize + "%");
        } else if (size != 0) {
            tvtxt.setText("" + size);
        }

        DataUrls.mmsids22.clear();

        // To get the values from shared preferences..
        strUserId = prefs.getString("userid", "");
        checkbackground = prefs.getBoolean("checkbackground", false);

        preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        lst_messages.setScrollingCacheEnabled(false);
        footerView.setVisibility(View.GONE);

        mLayout = Home.mLayout;

        // To Hide/Show the Menu
//        relBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // hide key board
//                final InputMethodManager imm = (InputMethodManager) mActivity
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
//        });// back button

        // Visible the layout To search
//        search.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchlayout.setVisibility(View.VISIBLE);
//                search_edt = true;
//            }
//        });

        // when called default search button of the device
        edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        if (edtSearch.getText().toString().trim().length() == 0) {
                        } else {
                            searchlist.setVisibility(View.VISIBLE);
                            lst_messages.setVisibility(View.GONE);

                            messages_scrollcount = 0;
                            searchBtn.setVisibility(View.GONE);
                            crossBtn.setVisibility(View.VISIBLE);
                            boolsearch = true;

                            toSearch();// To Search (When called Search image in
                            // edittext/default search button)
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        // when called search button in edittext
        searchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (edtSearch.getText().toString().trim().length() == 0) {
                    } else {
                        searchlist.setVisibility(View.VISIBLE);
                        lst_messages.setVisibility(View.GONE);
                        messages_scrollcount = 0;
                        searchBtn.setVisibility(View.GONE);
                        crossBtn.setVisibility(View.VISIBLE);
                        boolsearch = true;

                        toSearch();// To Search (When called Search image in
                        // edittext/default search button)
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // when called cross button in edittext
//        crossBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    boolsearch = false;
//                    messages_scrollcount = 0;
//                    MessagesFragment fragment = new MessagesFragment();
//
//                    FragmentManager fm = getActivity()
//                            .getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.activity_main_content_fragment, fragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        refresh.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                prefs = PreferenceManager
//                        .getDefaultSharedPreferences(mActivity);
//
//                ConnectivityManager connManager = (ConnectivityManager) mActivity
//                        .getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo mWifi = connManager
//                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//                if (mWifi.isConnected()) {
//
//                    if (BackupMessagesService.check == false) {
//                        BackupMessagesService.backup = false;
//                        if (BackupMessagesService.backup == false) {
//                            // To Start the BackGround service To get messages
//                            // and
//                            // upload to amazon(mms) and myappdemo.net(messages)
//                            DataUrls.svc = new Intent(mActivity,
//                                    BackupMessagesService.class);
//                            mActivity.startService(DataUrls.svc);
//
//                        }
//                    }
//
////                    try {
////                        clear();
////                        FragmentManager fm = getActivity()
////                                .getSupportFragmentManager();
////                        FragmentTransaction ft = fm.beginTransaction();
////                        MessagesFragment fragment = new MessagesFragment();
////                        ft.replace(R.id.activity_main_content_fragment,
////                                fragment);
////                        ft.addToBackStack(null);
////                        ft.commit();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                } else {
//                    if (CheckInternetConnection.isOnline(mActivity)) {
//
////                        if (!MainActivity.alert) {
////                            alertMessageforWIFI();
////                        } else {
////                            MainActivity.alert = true;
////
////                            if (BackupMessagesService.check == false) {
////                                BackupMessagesService.backup = false;
////                                if (BackupMessagesService.backup == false) {
////                                    // To Start the BackGround service To get
////                                    // messages and
////                                    // upload to amazon(mms) and
////                                    // myappdemo.net(messages)
////                                    DataUrls.svc = new Intent(mActivity,
////                                            BackupMessagesService.class);
////                                    mActivity.startService(DataUrls.svc);
////
////                                }
////                            }
////
////                            if (!DataUrls.position) {
////                                try {
////                                    clear();
////                                    FragmentManager fm = getActivity()
////                                            .getSupportFragmentManager();
////                                    FragmentTransaction ft = fm
////                                            .beginTransaction();
////                                    MessagesFragment fragment = new MessagesFragment();
////                                    ft.replace(
////                                            R.id.activity_main_content_fragment,
////                                            fragment);
////                                    ft.addToBackStack(null);
////                                    ft.commit();
////                                } catch (Exception e) {
////                                    e.printStackTrace();
////                                }
////                            }
////                        }
//                    } else {
//                        Toast.makeText(getActivity(),
//                                "No Internet Connectivity", Toast.LENGTH_LONG)
//                                .show();
//                    }
//                }
//
//            }
//        });

        mmsmid2.clear();
        mmsmid1.clear();

        if (checkbackground == true) {

            lst_messages.setAdapter(null);
            messages = Collections.synchronizedList(new ArrayList());
            ArrDesc = new ArrayList<String>();
            ArrName = new ArrayList<String>();
            ArrTime = new ArrayList<String>();
            ArrUrl = new ArrayList<String>();
            ArrFullname = new ArrayList<String>();
            ArrPhoneNum = new ArrayList<String>();
            Arrmedia = new ArrayList<String>();
            arrmesstype = new ArrayList<String>();
            arrmmstext = new ArrayList<String>();
            ArrFullname = new ArrayList<String>();

            // This adapter for to get the messages from service
            load_msgs_adapter = (new MessagesListAdapter(mActivity,
                    R.layout.messages_list_item, ArrName, ArrFullname, ArrDesc,
                    ArrTime, ArrUrl, ArrPhoneNum, Arrmedia, arrmesstype));
            // This adapter for to get the messages from device and display.
            load_msgs_adapter_new = (new MessagesListAdapter_new(mActivity,
                    R.layout.messages_list_item, ArrName, ArrFullname, ArrDesc,
                    ArrTime, ArrUrl, ArrPhoneNum, Arrmedia, arrmesstype));

//            refresh.setVisibility(View.GONE);

            if (load_msgs_adapter != null) {
                lst_messages.setAdapter(load_msgs_adapter);
            }
            if (load_msgs_adapter_new != null) {
                lst_messages.setAdapter(load_msgs_adapter_new);
            }
            // Afeter upload the data to server and get the data to server and
            // display..
            lst_messages.addFooterView(footerView);

            lst_messages.setAdapter(null);
//            lst_messages.addHeaderForList();

            processMessages();

        } else {

            if (messages == null || messages.size() == 0) {
                messages = Collections.synchronizedList(new ArrayList());
            }

            if (ArrDesc == null || ArrDesc.size() == 0) {
                ArrDesc = new ArrayList<String>();
                ArrName = new ArrayList<String>();
                ArrTime = new ArrayList<String>();
                ArrUrl = new ArrayList<String>();
                ArrFullname = new ArrayList<String>();
                ArrPhoneNum = new ArrayList<String>();
                Arrmedia = new ArrayList<String>();
                arrmesstype = new ArrayList<String>();
                arrmmstext = new ArrayList<String>();
            }
            // To get the data from device and display..

            // This adapter for to get the messages from service
            load_msgs_adapter = (new MessagesListAdapter(mActivity,
                    R.layout.messages_list_item, ArrName, ArrFullname, ArrDesc,
                    ArrTime, ArrUrl, ArrPhoneNum, Arrmedia, arrmesstype));
            // This adapter for to get the messages from device and display.
            load_msgs_adapter_new = (new MessagesListAdapter_new(mActivity,
                    R.layout.messages_list_item, ArrName, ArrFullname, ArrDesc,
                    ArrTime, ArrUrl, ArrPhoneNum, Arrmedia, arrmesstype));

//            refresh.setVisibility(View.VISIBLE);
            lst_messages.setAdapter(null);

            if (load_msgs_adapter != null) {
                lst_messages.setAdapter(load_msgs_adapter);
            }

            if (load_msgs_adapter_new != null) {
                lst_messages.setAdapter(load_msgs_adapter_new);
            }

            showdialog();

            if (!messagesload) {

                if (!DataUrls.position) {

                    if (thread != null) {

                        thread = null;
                        if (progress.isShowing()) {
                            dismissdialog();
                        }
                    } else {

                        thread = new Thread(new Runnable() {
                            @SuppressWarnings("static-access")
                            public void run() {
                                try {

                                    thread.sleep((long) 60000);
                                    DataUrls.position = false;
                                    thread = null;

                                    if (progress.isShowing()) {
                                        dismissdialog();

//                                        try {
//                                            FragmentManager fm = getFragmentManager();
//                                            FragmentTransaction ft = fm
//                                                    .beginTransaction();
//                                            MessagesFragment fragment = new MessagesFragment();
//                                            ft.replace(
//                                                    R.id.activity_main_content_fragment,
//                                                    fragment);
//                                            ft.addToBackStack(null);
//                                            ft.commit();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    thread = null;
                                }
                            }
                        });
                        thread.start();

                        DataUrls.position = true;

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

                                if (!isBackuping) {

                                    ArrName.clear();
                                    Arrmedia.clear();
                                    arrmesstype.clear();
                                    arrmmstext.clear();
                                    ArrUrl.clear();
                                    ArrDesc.clear();
                                    ArrTime.clear();
                                    ArrPhoneNum.clear();
                                    ArrFullname.clear();

                                    messages.clear();

                                    readSmsFromDevice();

                                    checkMessages();

                                    if (messages.size() != 0) {
                                        List<Message> messages_temp = new ArrayList<Message>();
                                        List<Message> messages_temp2 = new ArrayList<Message>();

//										for (Message message : messages)
                                        synchronized (messages) {
                                            for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                                                Message message = it.next();
                                                messages_temp.add(message);
                                            }
                                        }
                                        while (messages_temp.size() != 0) {
                                            Message m1, m2, m3;
                                            m1 = messages_temp.get(0);
                                            m3 = messages_temp.get(0);

                                            for (int i = 1; i < messages_temp
                                                    .size() - 1; i++) {
                                                m2 = messages_temp.get(i);
                                                Date d1 = new Date(
                                                        m1.getDate_millis());
                                                Date d2 = new Date(
                                                        m2.getDate_millis());

                                                int x = d1.compareTo(d2);
                                                if (x > 0) {
                                                    m3 = m1;
                                                } else {
                                                    m3 = m2;
                                                    m1 = m2;
                                                }
                                            }
                                            messages_temp2.add(m3);
                                            messages_temp.remove(m3);
                                        }
                                        messages.clear();
                                        for (Message message : messages_temp2) {
                                            messages.add(message);
                                        }
                                        readyToProceed = true;
                                        check_cancelService = true;
                                    } else {
                                        if (progress.isShowing()) {
                                            dismissdialog();
                                        }
                                        mActivity.runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                DataUrls.position = false;
                                                alertMessage();
                                            }
                                        });
                                    }

                                    try {
                                        mActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                load_msgs_adapter
                                                        .notifyDataSetChanged();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    messagesload = true;

                                }

                                if (!isBackuping) {
                                    isBackuping = true;

                                    beginnn_New();
                                }
                            }
                        };
                        new Thread(runnable).start();
                    }
                }
            } else {
                DataUrls.position = false;
                try {
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            load_msgs_adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (progress.isShowing()) {
                    dismissdialog();
                }
            }

            lst_messages.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long arg3) {

                    // firstclick
                    checkbakup = prefs.getBoolean("checkbackup", false);
                    try {

                        if (checkbakup) {
                            SharedPreferences.Editor editor = preferences
                                    .edit();
                            editor.putString("userid", strUserId);
                            editor.putString("phonenumber",
                                    ArrPhoneNum.get(pos));
                            editor.putString("name", ArrName.get(pos));
                            editor.commit();
                        } else {
                            SharedPreferences.Editor editor = preferences
                                    .edit();
                            editor.putString("userid", strUserId);
                            editor.putString("phonenumber",
                                    ArrPhoneNum.get(pos));
                            editor.putString("name", ArrName.get(pos));
                            editor.commit();

                            list_name = new ArrayList<String>();
                            list_body = new ArrayList<String>();
                            list_num = new ArrayList<String>();
                            list_mmstext = new ArrayList<String>();
                            list_date = new ArrayList<String>();
                            list_type = new ArrayList<String>();
                            list_messtype = new ArrayList<String>();

                            List<Message> loadfordisplay = new ArrayList<Message>();
                            synchronized (messages) {
                                for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                                    Message message = it.next();
//							for (Message message : messages) {
                                    if (message.getNumber().equals(
                                            ArrPhoneNum.get(pos))) {
                                        loadfordisplay.add(message);
                                    }
                                }
                            }
                            Collections.sort(loadfordisplay,
                                    COMPARATOR_MESSAGETIME);
                            List<Message> list = new ArrayList<Message>();

                            for (int i = loadfordisplay.size() - 1; (list
                                    .size() < 20 && i >= 0); i--) {
                                list.add(loadfordisplay.get(i));
                            }
                            for (int i = list.size() - 1; i >= 0; i--) {
                                Message message = list.get(i);

                                list_name.add(message.getAddress());
                                list_mmstext.add(message.getBody2());
                                list_body.add(message.getBody());
                                list_num.add(message.getNumber());
                                list_date.add(message.getDate());
                                list_type.add(message.getType());
                                list_messtype.add(message.getmessagetype());
                            }
                            list.clear();
                        }
                        if (!checkbakup) {
                            if (pos == 5) {
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        MessageDetailsActivity.class);
                                checkmess = false;
                                check_msg_click = true;
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    MessageDetailsActivity.class);
                            check_msg_click = true;
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        return view;
    }// oncreateview....

    // To get the data from device and display..

    private void beginnn_New() {
        // Do something long
        runnable_beginn = new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    if (messages.size() != 0 && readyToProceed
                            && i < messages.size()) {
                        Message message = messages.get(i);
                        List<Message> x = messages_conversation.get(message
                                .getAddress());
                        if (x == null) {
                            List<Message> list = new ArrayList<Message>();
                            list.add(message);
                            messages_conversation.put(message.getAddress(),
                                    list);
                            ArrPhoneNum.add(message.getNumber());
                            arrmesstype.add(message.getmessagetype());
                            arrmmstext.add(message.getBody2());

                            if (messages_conversation.size() == 6) {
                                ArrName.add("MESSAGEFORBACKUPPROCESS");
                            } else {
                                ArrName.add(message.getAddress());
                            }

                            ArrUrl.add("");
                            ArrFullname.add(message.getAddress());
                            ArrDesc.add(message.getBody());
                            ArrTime.add("");

                        } else {
                            x.add(message);
                        }

                        lst_messages.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            load_msgs_adapter_new
                                                    .notifyDataSetChanged();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 3000);
                        i++;

                        if (messages_conversation.size() == 6
                                || i == messages.size()) {
                            lst_messages.postDelayed(new Runnable() {
                                public void run() {
                                    mActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            dismissdialog();
                                            DataUrls.position = false;
                                        }
                                    });
                                }
                            }, 4000);
                            break;
                        }
                        if (i == messages.size())
                            break;
                    }
                }
            }
        };
        new Thread(runnable_beginn).start();
    }
    private static boolean messageExists(int i){
        try {
            messages.get( i );
            return true;
        } catch ( IndexOutOfBoundsException e ) {
            return false;
        }
    }
    // To get MMSes from device
    private void checkMessages() {
        String body = " ";
        String[] values = null;
        String[] coloumns = null;
        String threadid = "";

        mms_image = new ArrayList<String>();
        mms_add = new ArrayList<String>();
        mms_date = new ArrayList<String>();
        mms_type = new ArrayList<String>();
        mms_text = new ArrayList<String>();
        mms_lastbackup = new ArrayList<String>();
        mms_threadid = new ArrayList<String>();

        Message mess3;
        Uri uriSMSURI = Uri.parse("content://mms");
        String[] projection = { "*" };
        Cursor curPdu = mActivity.getContentResolver().query(uriSMSURI,
                projection, null, null, null);

        while (curPdu.moveToNext()) {

            mess3 = new Message();
            try {
                String id = curPdu.getString(curPdu.getColumnIndex("_id"));

                mmsmid1.add(id);
                mess3.setId(id);
                try {
                    type = Integer.parseInt(curPdu.getString(curPdu
                            .getColumnIndex("m_type")));
                    type1 = (type == 128) ? 2 : 1;
                    mms_type.add((type == 128) ? "2" : "1");
                    mess3.setType((type == 128) ? "2" : "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    threadid = curPdu.getString(curPdu
                            .getColumnIndex("thread_id"));
                    Log.d("Threadid", threadid);
                } catch (Exception e) {
                    e.printStackTrace();
                    threadid = "0";
                }

                mess3.setThreadid(threadid);

                checkmms = false;
                String phnumber = getAddressNumber(Integer.parseInt(id));

                try {
                    if (phnumber.equalsIgnoreCase(null)
                            || phnumber.equalsIgnoreCase("null")) {
                        phnumber = "0";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    phnumber = "0";
                }

                if (phnumber.trim().length() > 10
                        && phnumber.trim().length() < 14) {
                    mess3.setNumber(phnumber.substring(phnumber.length() - 10,
                            phnumber.length()));
                    mess3.setAddress(phnumber.substring(phnumber.length() - 10,
                            phnumber.length()));
                } else {
                    mess3.setNumber(phnumber);
                    mess3.setAddress(phnumber);
                }
                mms_add.add(phnumber);

                try {
                    curPdu.getString(curPdu.getColumnIndex("date"));
                    Long millisecond2 = Long.parseLong(curPdu.getString(curPdu
                            .getColumnIndex("date")));
                    Long mmsmilli = millisecond2 * 1000;
                    String dateString2 = getDateCurrentTimeZone(millisecond2);
                    mess3.setDate(dateString2);
                    mess3.setDate_millis(mmsmilli);
                    mms_lastbackup.add(dateString2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mess3.setmessagetype("mms");
                messages.add(mess3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] projection1 = { "*" };
        Cursor curPart = mActivity.getContentResolver().query(
                Uri.parse("content://mms/part"), projection1, null, null, null);
        coloumns = curPart.getColumnNames();

        while (curPart.moveToNext()) {
            String mid2 = null;
            String id = null;
            try {
                id = curPart.getString(curPart.getColumnIndex("_id"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String types = curPart.getString(curPart.getColumnIndex("ct"));
            if ("text/plain".equals(types)) {
                String data = curPart
                        .getString(curPart.getColumnIndex("_data"));
                if (data != null) {
                } else {
                    body = curPart.getString(curPart.getColumnIndex("text"));
                }
            }
            if (values == null)
                values = new String[coloumns.length];

            coloumns = curPart.getColumnNames();
            for (int i = 0; i < curPart.getColumnCount(); i++) {
                values[i] = curPart.getString(i);
            }
            mid2 = curPart.getString(curPart.getColumnIndex("mid"));
            if (values[3].startsWith("image")
                    || values[3].startsWith("text/plain")) {
                mid2 = curPart.getString(curPart.getColumnIndex("mid"));
            } else {
                mid2 = curPart.getString(curPart.getColumnIndex("mid"));
                if (values[3].startsWith("video")
                        || values[3].startsWith("audio")) {
                    synchronized (messages) {
                        for (int i = 0; i < messages.size(); i++) {
                            Message m = messages.get(i);

                            if (m != null && m.getId() != null
                                    && m.getId().equals(mid2)) {
                                messages.get(i);
                                if (messageExists(i)) {
                                    messages.remove(i);
                                }
                            }
                        }
                    }
                }
                mid2 = "null";
            }
            if (mid2.equals("null")) {
            } else {
                if (DataUrls.mmsids22.contains(mid2)) {
                    synchronized (messages) {
                        for (int i = 0; i < messages.size(); i++) {
                            Message messd = messages.get(i);
                            if (values[3].startsWith("text/plain")) {
                                if (messd != null && messd.getId() != null
                                        && messd.getId().equals(mid2)) {
                                    messd.setBody2(body);
                                    if (messageExists(i)) {
                                        messages.set(i, messd);
                                    }

                                }
                            } else {
                                messd.setBody2("0");
                                if (messageExists(i)) {
                                    messages.set(i, messd);
                                }
                            }
                        }
                    }
                } else {
                    synchronized (messages) {
                        for (int i = 0; i < messages.size(); i++) {
                            Message m = messages.get(i);
                            if (m != null && m.getId() != null
                                    && m.getId().equals(mid2)) {
                                if (values[3].startsWith("image")) {
                                    m.setBody(id);
                                    for (Contact contact : Splash.contacts) {
                                        if (m.getNumber().equals(contact.getNo())) {
                                            m.setAddress(contact.getName());
                                            m.setNumber(contact.getNo());
                                        }
                                    }
                                    if (messageExists(i)) {
                                        messages.set(i, m);
                                    }
                                    DataUrls.mmsids22.add(mid2);
                                } else {
                                    messages.remove(i);
                                }
                            }
                        }
                    }
                }
            }
        }
        synchronized (messages) {
            for (int i = 0; i < messages.size(); i++) {
                Message mn = messages.get(i);
                if (mn.getBody2() == null || mn.getBody2().trim().length() < 1) {
                    mn.setBody2("0");
                }
            }
        }
        synchronized (messages) {
            for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                Message message = it.next();
//		for (Message message : messages) {
                for (Contact contact : Splash.contacts) {
                    if (message.getNumber().equals(contact.getNo())) {
                        message.setAddress(contact.getName());
                        message.setNumber(contact.getNo());
                    }
                }
            }
        }
        synchronized (messages) {
            for (int i = 0; i < messages.size(); i++) {
                Message mesx = messages.get(i);
                if (mesx.getNumber() == null
                        || (mesx.getNumber().trim().length() < 2)
                        || mesx.getBody() == null
                        || (mesx.getBody().trim().length() == 0)) {
                    if (messageExists(i)) {
                        messages.remove(i);
                    }
                }
            }
        }

        Uri uriSMSURI1 = Uri
                .parse("content://mms-sms/conversations?simple=true");
        String[] projection2 = { "*" };
        String where1 = "date" + ">" + 0;
        Cursor cur1 = mActivity.getContentResolver().query(uriSMSURI1,
                projection2, where1, null, "date");
        ArrayList<String> arr = new ArrayList<String>();

        while (cur1.moveToNext()) {

            try {

                if (cur1.getString(cur1.getColumnIndex("recipient_ids"))
                        .contains(" ")) {
                    arr.add(cur1.getString(cur1.getColumnIndex("_id")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        synchronized (messages) {
            for (int i = 0; i < arr.size(); i++) {
                for (int k = 0; k < messages.size(); k++) {
                    if (arr.get(i).equals(messages.get(k).getThreadid())) {
                        if (messageExists(i)) {
                            messages.remove(k);
                        }
                        k = k - 1;
                    }
                }
            }
        }
    }// check messages.......

    // Afeter upload the data to server and get the data to server and
    // display..
    private void processMessages() {

        ArrName.clear();
        Arrmedia.clear();
        arrmesstype.clear();
        ArrUrl.clear();
        ArrDesc.clear();
        arrmmstext.clear();
        ArrTime.clear();
        ArrPhoneNum.clear();
        ArrFullname.clear();
        lstname.clear();
        msgs_latest_footer = false;
//        search.setVisibility(View.VISIBLE);

        Typeface.createFromAsset(mActivity.getAssets(),
                "fonts/Roboto-Light.ttf");
        check_dialog = true;

        showdialog();

        lst_messages.setAdapter(load_msgs_adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissdialog();
            }
        }, 2000);

        LatestMessages lms = new LatestMessages(0);
        lms.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        lst_messages.setOnScrollListener(new EndlessScrollListener_msgs_New(
                lst_messages));

        searchlist.setOnScrollListener(new EndlessScrollListener_msgs_New(
                searchlist));
        searchlist.addFooterView(footerView);

        lst_messages.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                checkbakup = prefs.getBoolean("checkbackup", false);
                pos = pos - 1;

                try {
                    if (checkbakup) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", strUserId);
                        editor.putString("phonenumber", ArrPhoneNum.get(pos));
                        editor.putString("name", ArrName.get(pos));
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", strUserId);
                        editor.putString("phonenumber", ArrPhoneNum.get(pos));
                        editor.putString("name", ArrName.get(pos));
                        editor.commit();

                        list_body = new ArrayList<String>();
                        list_num = new ArrayList<String>();
                        list_mmstext = new ArrayList<String>();
                        list_date = new ArrayList<String>();
                        synchronized (messages) {
                            for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                                Message message = it.next();
//						for (Message message : messages) {
                                if (message.getNumber()
                                        .equals(ArrPhoneNum.get(pos))) {
                                    list_name.add(message.getAddress());
                                    list_body.add(message.getBody());
                                    list_mmstext.add(message.getBody2());
                                    list_num.add(message.getNumber());
                                    list_date.add(message.getDate());
                                }
                            }
                        }
                    }
                    checkbakup = prefs.getBoolean("checkbackup", false);
                    if (!checkbakup) {
                        if (pos == lst_messages.getCount() - 1) {
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    MessageDetailsActivity.class);
                            check_msg_click = true;
                            startActivity(intent);
                        }
                    } else {
                        if (pos == lst_messages.getCount() - 1) {
                            if (ArrName.get(pos).equals(
                                    "MESSAGEFORBACKUPPROCESS")) {
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        MessageDetailsActivity.class);
                                check_msg_click = true;
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    MessageDetailsActivity.class);
                            check_msg_click = true;
                            startActivity(intent);
                        }
                    }
                    // finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });// onitem click

        searchlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                checkbakup = prefs.getBoolean("checkbackup", false);

                try {
                    if (checkbakup) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", strUserId);
                        editor.putString("phonenumber", ArrPhoneNum.get(pos));
                        editor.putString("name", ArrName.get(pos));
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userid", strUserId);
                        editor.putString("phonenumber", ArrPhoneNum.get(pos));
                        editor.putString("name", ArrName.get(pos));
                        editor.commit();

                        list_body = new ArrayList<String>();
                        list_num = new ArrayList<String>();
                        list_mmstext = new ArrayList<String>();
                        list_date = new ArrayList<String>();
                        synchronized (messages) {
                            for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                                Message message = it.next();
//						for (Message message : messages) {
                                if (message.getNumber()
                                        .equals(ArrPhoneNum.get(pos))) {
                                    list_name.add(message.getAddress());
                                    list_body.add(message.getBody());
                                    list_mmstext.add(message.getBody2());
                                    list_num.add(message.getNumber());
                                    list_date.add(message.getDate());
                                }
                            }
                        }
                    }
                    checkbakup = prefs.getBoolean("checkbackup", false);
                    if (!checkbakup) {
                        if (pos == lst_messages.getCount() - 1) {
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    MessageDetailsActivity.class);
                            check_msg_click = true;
                            startActivity(intent);
                        }
                    } else {
                        if (pos == lst_messages.getCount() - 1) {
                            if (ArrName.get(pos).equals(
                                    "MESSAGEFORBACKUPPROCESS")) {
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        MessageDetailsActivity.class);
                                check_msg_click = true;
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    MessageDetailsActivity.class);
                            check_msg_click = true;
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (search_edt) {
                    if (s.length() == 0) {
                        crossBtn.callOnClick();
                    }
                }
            }
        });

        // when click on edittext
        edtSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mLayout.isMenuShown()) {
                    mLayout.toggleMenu();
                }
            }
        });

        // Set a listener to be invoked when the list should be refreshed.
//        lst_messages.setOnRefreshListener(new OnRefreshListener() {
//
//            public void onRefresh() {
//
//                onclickrefresh = true;
//
//                if (BackupMessagesService.check == false) {
//                    BackupMessagesService.backup = false;
//                    if (BackupMessagesService.backup == false) {
//                        // To Start the BackGround service To get messages and
//                        // upload to amazon(mms) and myappdemo.net(messages)
//                        DataUrls.svc = new Intent(getActivity(),
//                                BackupMessagesService.class);
//                        getActivity().startService(DataUrls.svc);
//                    }
//                }
//
//                // Do work to refresh the list here.
//                new PullToRefreshDataTask(0).execute();
//            }
//        });
    }// processmessages...

    // To get the messages from server..
    class LatestMessages extends AsyncTask<URL, Integer, Long> {
        int messages_scrollcount;

        public LatestMessages(int messages_scrollcount) {
            super();
            this.messages_scrollcount = messages_scrollcount;
            lodingForLatestMessages = true;
        }

        protected void onPreExecute() {
            if (progress.isShowing()) {
                dismissdialog();
            }

            if (check_footer) {
                dialog = MyProgressDialog.show(getActivity(), null, null);
                check_footer = false;
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Long doInBackground(URL... arg0) {
            boolean valids = false;
            try {
                valids = CheckInternetConnection.isOnline(getActivity());
            } catch (Exception e) {
                valids = false;
                e.printStackTrace();
            }
            if (valids) {
                try {
                    count_msgs = 0;
                    String devicetime = getDateTime();
                    String url= DataUrls.get_msgs + "userid="
                            + strUserId + "&devicedatetime="
                            + devicetime.trim().replace(" ", "%20")
                            + "&start=" + messages_scrollcount;
                    latestmsg_res = UrltoValue
                            .getValuefromUrl(url);

                    if (!latestmsg_res.equals("zero") && latestmsg_res != null) {
                        json_latestmessages = new JSONArray(latestmsg_res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, DataUrls.dialogtitle,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(Long result) {
            if (!CheckInternetConnection.isOnline(mActivity)) {
                Toast.makeText(mActivity, DataUrls.dialogtitle,
                        Toast.LENGTH_SHORT).show();
                mActivity.finish();
            } else {

                msgs_latest_footer = true;

                if (lst_messages.getFooterViewsCount() == 0) {
                    lst_messages.setEnabled(true);
                }
                lodingForLatestMessages = false;
                if (messages_scrollcount == 0 && ArrName.size() != 0)
                    lst_messages.setSelection(0);
                try {
                    if (json_latestmessages.length() == 0) {

                        msgs_latest_footer = false;
                        lst_messages.removeFooterView(footerView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                lst_messages.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        if (!latestmsg_res.equals("zero")) {
                                            json_latestmessages = new JSONArray(
                                                    latestmsg_res);
                                            for (int i = 0; i < json_latestmessages
                                                    .length(); i++) {

                                                JSONObject jo = json_latestmessages
                                                        .getJSONObject(i);
                                                if (jo.getString("username")
                                                        .trim().length() > 1) {
                                                    count_msgs++;
                                                    messtype.add(jo
                                                            .getString("messagetype"));
                                                    arrmesstype.add(jo
                                                            .getString("messagetype"));
                                                    mmstext.add(jo
                                                            .getString("mmstext"));
                                                    arrmmstext.add(jo
                                                            .getString("mmstext"));
                                                    media.add(jo
                                                            .getString("media"));
                                                    Arrmedia.add(jo
                                                            .getString("media"));
                                                    lstname.add(jo
                                                            .getString("username"));
                                                    ArrName.add(jo
                                                            .getString("username"));
                                                    lstArrUr.add(jo
                                                            .getString("logo"));
                                                    ArrUrl.add(jo
                                                            .getString("logo"));
                                                    lstArrFullname.add(jo
                                                            .getString("fullname"));
                                                    ArrFullname.add(jo
                                                            .getString("fullname"));
                                                    lstarrdesc.add(jo
                                                            .getString("message"));
                                                    ArrDesc.add(jo
                                                            .getString("message"));
                                                    lstAArrTime.add(jo
                                                            .getString("message_datetime"));
                                                    ArrTime.add(jo
                                                            .getString("message_datetime"));
                                                    lstArrPhoneNum.add(jo
                                                            .getString("number"));
                                                    ArrPhoneNum.add(jo
                                                            .getString("number"));
                                                }
                                            }
                                        } else {
                                            lst_messages
                                                    .removeFooterView(footerView);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    load_msgs_adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            try {
                if (check_footer == false) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            if (!onclickrefresh) {
//                if (lst_messages != null)
//                    lst_messages.clickOnRefresh();
//            }
        }
    }// latestmessagesAsync..

    // For Loading 10 by 10
    public class EndlessScrollListener_msgs_New implements OnScrollListener {
        ListView listView;

        public EndlessScrollListener_msgs_New(ListView listView) {
            super();
            this.listView = listView;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                if (listView.getLastVisiblePosition() == listView.getCount() - 1) {

                    if (boolsearch) {
                        if (!lodingForLatestMessages) {
                            new Doinsearch(listView.getCount()
                                    - listView.getFooterViewsCount()).execute();
                        }
                    } else if (!lodingForLatestMessages) {

                        new LatestMessages(listView.getCount()
                                - listView.getFooterViewsCount()).execute();
                    }
                }
            }
        }
    }// endlessscroll listener...

    // To get current date and time
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // To sort the Messages by using Date
    private static Comparator<Message> COMPARATOR_MESSAGETIME = new Comparator<Message>() {
        // This is where the sorting happens........
        public int compare(Message o1, Message o2) {
            Date d1 = new Date(o1.getDate_millis());
            Date d2 = new Date(o2.getDate_millis());
            int x = d1.compareTo(d2);
            return x;
        }
    };

    // To get sms from device
    public void readSmsFromDevice() {
        int xcount = 0;
        Uri uriSMSURI = Uri.parse("content://sms/");
        String[] projection = { "address", "body", "date", "type", "thread_id" };
        cursor_messages = mActivity.getContentResolver().query(uriSMSURI,
                projection, null, null, null);
        while (cursor_messages.moveToNext()) {
            if (xcount == 1000) {
                break;
            } else {
                try {
                    Message mess1 = new Message();
                    String number = cursor_messages.getString(cursor_messages
                            .getColumnIndex("address"));
                    try {
                        Log.d("number", "number" + number.length());
                    } catch (Exception e) {
                        e.printStackTrace();
                        number = "0";
                    }
                    number = number.replaceAll("[\\W]", "");

                    if (number.length() > 1) {
                        if (number.trim().length() > 10) {
                            mess1.setNumber(number.substring(
                                    number.length() - 10, number.length()));
                            mess1.setAddress(number.substring(
                                    number.length() - 10, number.length()));
                        } else {
                            mess1.setNumber(number);
                            mess1.setAddress(number);
                        }
                        try {
                            String _id = cursor_messages
                                    .getString(cursor_messages
                                            .getColumnIndex("_id"));
                            mess1.setId(_id);
                        } catch (Exception e) {
                            mess1.setId("0");
                        }
                        mess1.setBody(cursor_messages.getString(cursor_messages
                                .getColumnIndex("body")));

                        String type = cursor_messages.getString(cursor_messages
                                .getColumnIndex("type"));
                        Long millisecond = Long.parseLong(cursor_messages
                                .getString(cursor_messages
                                        .getColumnIndex("date")));
                        String dateString = DateFormat.format(
                                "yyyy/MM/dd hh:mm:ss a", new Date(millisecond))
                                .toString();

                        mess1.setDate_millis(millisecond);
                        mess1.setDate(dateString);
                        mess1.setType(type);
                        mess1.setmessagetype("sms");

                        try {
                            String threadid = cursor_messages
                                    .getString(cursor_messages
                                            .getColumnIndex("thread_id"));
                            Log.d("Threadid", threadid);
                            mess1.setThreadid(threadid);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mess1.setThreadid("0");
                        }

                        for (Contact contact : Splash.contacts) {
                            if (mess1.getNumber().equals(contact.getNo())) {
                                mess1.setAddress(contact.getName());
                                mess1.setNumber(contact.getNo());
                            }
                        }
                        messages.add(mess1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                xcount++;
            }
        }
    }// readsms

    // To convert the timestamp to currenttime format

    public static String getDateCurrentTimeZone(long timestamp) {
        String localTime = null;
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = calendar.getTimeZone();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
            sdf.setTimeZone(tz);
            localTime = sdf.format(new Date(timestamp * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTime;
    }

    // To get the mms by using id from the device
    public static String GetMmsAttachment(String _id) {
        String imagepath = null;
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        try {
            is = mActivity.getContentResolver().openInputStream(partURI);
            Bitmap bitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeStream(is), 130, 130, false);
            imagepath = convertBitmapToFile(bitmap, "MyApp-SubFolder");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagepath;
    }

    // To convert the bitmap to file..
    public static String convertBitmapToFile(Bitmap bitmap, String foldername) {
        String extr = Environment.getExternalStorageDirectory().toString();
        File mFolder = new File(extr + "/MyApp");
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
        String strF = mFolder.getAbsolutePath();
        File mSubFolder = new File(strF + "/" + foldername);

        if (!mSubFolder.exists()) {
            mSubFolder.mkdir();
        }
        String s = Calendar.getInstance().getTimeInMillis() + ".png";
        File f = new File(mSubFolder.getAbsolutePath(), s);
        strMyImagePath = f.getAbsolutePath();
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);

            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMyImagePath;
    }

    // To convert the image into base64 format with the imagepath
    public static String base64imageformat(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 30, baos); // bm is the
            // bitmap
            // object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encodedImage;
        } else
            return "";
    }

    // To Search (When called Search image in edittext/default search button)
    public void toSearch() {
        searchBtn.setVisibility(View.GONE);
        searchby = edtSearch.getText().toString().trim();
        check_footer = true;

        if (searchby.trim().length() != 0) {
            ArrName.clear();
            ArrUrl.clear();
            ArrDesc.clear();
            ArrTime.clear();
            ArrPhoneNum.clear();
            ArrFullname.clear();
            Arrmedia.clear();
            arrmesstype.clear();

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    searchlist.setAdapter(load_msgs_adapter);

                }
            });
            Doinsearch serch = new Doinsearch(messages_scrollcount);
            serch.execute();
        } else {
            ArrName.clear();
            ArrUrl.clear();
            ArrDesc.clear();
            ArrTime.clear();
            ArrPhoneNum.clear();
            ArrFullname.clear();
            Arrmedia.clear();
            arrmesstype.clear();
            for (String string : lstarrdesc) {
                ArrDesc.add(string);
            }
            for (String string : lstAArrTime) {
                ArrTime.add(string);
            }
            for (String string : lstArrFullname) {
                ArrFullname.add(string);
            }
            for (String string : lstname) {
                ArrName.add(string);
            }
            for (String string : media) {
                Arrmedia.add(string);
            }
            for (String string : messtype) {
                arrmesstype.add(string);
            }
            for (String string : lstArrPhoneNum) {
                ArrPhoneNum.add(string);
            }
            for (String string : lstArrUr) {
                ArrUrl.add(string);
            }

            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    load_msgs_adapter.notifyDataSetChanged();
                }
            });
        }
    }

    // AsyncTask class for search by using username/message
    class Doinsearch extends AsyncTask<URL, Integer, Long> {
        public Doinsearch(int scrollcount) {
            super();
            messages_scrollcount = scrollcount;
            lodingForLatestMessages = true;
        }

        public void onPreExecute() {

            if (check_footer) {
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
                if ((searchby.length() > 0) && (!searchby.startsWith("#"))) {

                    String hashtagresponse = null;
                    try {
                        // Service for search by hashtag
                        hashtagresponse = UrltoValue
                                .getValuefromUrl(DataUrls.messagesearch
                                        + "&userid=" + strUserId + "&search="
                                        + URLEncoder.encode(searchby, "utf-8")
                                        + "&start=" + messages_scrollcount);

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        if (!hashtagresponse.equalsIgnoreCase("null")
                                || hashtagresponse.equalsIgnoreCase(null)) {
                            ja2 = new JSONArray(hashtagresponse);

                        }
                    } catch (JSONException e1) {
                        ja2 = new JSONArray();
                        e1.printStackTrace();
                    }

                    for (int i = 0; i < ja2.length(); i++) {
                        try {
                            jo2 = ja2.getJSONObject(i);
                            ArrUrl.add(jo2.getString("logo"));
                            ArrName.add(jo2.getString("username"));
                            ArrDesc.add(jo2.getString("message"));
                            ArrTime.add(jo2.getString("message_datetime"));
                            ArrPhoneNum.add(jo2.getString("number"));
                            ArrFullname.add(jo2.getString("fullname"));
                            Arrmedia.add(jo2.getString("media"));
                            arrmesstype.add(jo2.getString("messagetype"));

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
                    load_msgs_adapter.notifyDataSetChanged();
                    searchlayout.setVisibility(View.VISIBLE);
                    search_edt = true;
                }
            });
            lodingForLatestMessages = false;

            if (check_footer) {
                try {
                    dialog.dismiss();
                    check_footer = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                searchlist.removeFooterView(footerView);
            }
        }
    }

    // To show the progress Dialog
    public void showdialog() {
        progress = ProgressDialog.show(mActivity, null,
                "Loading Please Wait....");
    }

    // To dismiss the progress Dialog
    public void dismissdialog() {
        if (progress != null && progress.isShowing())
            progress.dismiss();
    }

    // Alert Message for Selected aack is not supported to share to the social
    // networks to their limits
    void alertMessage() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
//        alertDialog.setTitle("Aack Aack");
//        alertDialog.setMessage("You dont have Messages in your device");
//        // You Can Customise your Title here
//        alertDialog.setCancelable(false);
//        alertDialog.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//
//                        ExploreFragment fragment = new ExploreFragment();
//                        getFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.activity_main_content_fragment,
//                                        fragment).commit();
//                    }
//                });
//        alertDialog.show();
    }

    // Async class for to get latest messages when pull
    private class PullToRefreshDataTask extends AsyncTask<URL, Integer, Long> {

        int messages_scrollcount;

        public PullToRefreshDataTask(int messages_scrollcount) {
            super();
            this.messages_scrollcount = messages_scrollcount;
            lodingForLatestMessages = true;
        }

        @Override
        protected Long doInBackground(URL... arg0) {

            if (isCancelled()) {
                return null;
            }

            boolean valids = false;
            try {
                valids = CheckInternetConnection.isOnline(getActivity());
            } catch (Exception e) {
                valids = false;
                e.printStackTrace();
            }
            if (valids) {
                try {
                    count_msgs = 0;
                    String devicetime = getDateTime();
                    latestmsg_res = UrltoValue
                            .getValuefromUrl(DataUrls.get_msgs + "userid="
                                    + strUserId + "&devicedatetime="
                                    + devicetime.trim().replace(" ", "%20")
                                    + "&start=" + messages_scrollcount);

                    if (!latestmsg_res.equals("zero") && latestmsg_res != null) {
                        json_latestmessages = new JSONArray(latestmsg_res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, DataUrls.dialogtitle,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (!CheckInternetConnection.isOnline(mActivity)) {
                Toast.makeText(mActivity, DataUrls.dialogtitle,
                        Toast.LENGTH_SHORT).show();
                mActivity.finish();
            } else {

                ArrName.clear();
                Arrmedia.clear();
                arrmesstype.clear();
                ArrUrl.clear();
                ArrDesc.clear();
                arrmmstext.clear();
                ArrTime.clear();
                ArrPhoneNum.clear();
                ArrFullname.clear();
                lstname.clear();
                msgs_latest_footer = false;
//                search.setVisibility(View.VISIBLE);

                msgs_latest_footer = true;

                if (lst_messages.getFooterViewsCount() == 0) {
                    lst_messages.setEnabled(true);
                }
                lodingForLatestMessages = false;
                if (messages_scrollcount == 0 && ArrName.size() != 0)
                    lst_messages.setSelection(0);
                try {
                    if (json_latestmessages.length() == 0) {

                        msgs_latest_footer = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (!latestmsg_res.equals("zero")) {
                                    json_latestmessages = new JSONArray(
                                            latestmsg_res);
                                    for (int i = 0; i < json_latestmessages
                                            .length(); i++) {

                                        JSONObject jo = json_latestmessages
                                                .getJSONObject(i);
                                        if (jo.getString("username").trim()
                                                .length() > 1) {
                                            count_msgs++;
                                            messtype.add(jo
                                                    .getString("messagetype"));
                                            arrmesstype.add(jo
                                                    .getString("messagetype"));
                                            mmstext.add(jo.getString("mmstext"));
                                            arrmmstext.add(jo
                                                    .getString("mmstext"));
                                            media.add(jo.getString("media"));
                                            Arrmedia.add(jo.getString("media"));
                                            lstname.add(jo
                                                    .getString("username"));
                                            ArrName.add(jo
                                                    .getString("username"));
                                            lstArrUr.add(jo.getString("logo"));
                                            ArrUrl.add(jo.getString("logo"));
                                            lstArrFullname.add(jo
                                                    .getString("fullname"));
                                            ArrFullname.add(jo
                                                    .getString("fullname"));
                                            lstarrdesc.add(jo
                                                    .getString("message"));
                                            ArrDesc.add(jo.getString("message"));
                                            lstAArrTime.add(jo
                                                    .getString("message_datetime"));
                                            ArrTime.add(jo
                                                    .getString("message_datetime"));
                                            lstArrPhoneNum.add(jo
                                                    .getString("number"));
                                            ArrPhoneNum.add(jo
                                                    .getString("number"));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            load_msgs_adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            lst_messages.onRefreshComplete();
        }

        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
//            lst_messages.onLoadMoreComplete();
        }
    }

    // To get the mms address from device..
    private String getAddressNumber(int id) {
        String selectionAdd = new String("msg_id=" + id);
        Uri uriAddress = Uri.parse("content://mms/" + id + "/addr");
        String[] columns = { "*" };
        Cursor cAdd = mActivity.getContentResolver().query(uriAddress, columns,
                selectionAdd, null, null);
        String name = null;
        if (cAdd.moveToFirst()) {
            do {
                if (type1 == 1) {
                    if (checkmms == false) {
                        number = cAdd.getString(cAdd.getColumnIndex("address"))
                                .replaceAll("[\\W]", "");
                        if (number.trim().length() > 10) {
                            number = number.substring(number.length() - 10,
                                    number.length());
                        }
                        if (number != null) {
                            try {
                                name = number;
                                number = "";
                                number = "";
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                                if (name == null) {
                                    name = number;
                                    number = "";
                                }
                            }
                        }
                    }
                    checkmms = true;
                } else {
                    if (cAdd.getString(cAdd.getColumnIndex("address")).trim()
                            .equals("insert-address-token")) {
                    } else {
                        number = cAdd.getString(cAdd.getColumnIndex("address"))
                                .replaceAll("[\\W]", "");
                        if (number.trim().length() > 10) {
                            number = number.substring(number.length() - 10,
                                    number.length());
                        }
                        if (number != null) {
                            try {
                                if (name == null || name.equals("")
                                        || name.length() == 0) {
                                    name = number;
                                    number = "";
                                } else {
                                    name = name + "," + number;
                                    number = "";
                                }
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                                if (name == null || name.equals("")
                                        || name.length() == 0) {
                                    name = number;
                                    number = "";
                                } else {
                                    name = name + "," + number;
                                    number = "";
                                }
                            }
                        }
                    }
                }
            } while (cAdd.moveToNext());
        }
        if (cAdd != null) {
            cAdd.close();
        }
        return name;
    }// end of getting address

    // To clear the static ArrayLists
    public static void clear() {
//		messages = new ArrayList<Message>();
        messages  = Collections.synchronizedList(new ArrayList());
        if (lst_messages != null) {
            lst_messages.setAdapter(null);
        }
        ArrDesc = new ArrayList<String>();
        ArrName = new ArrayList<String>();
        ArrTime = new ArrayList<String>();
        ArrUrl = new ArrayList<String>();
        ArrFullname = new ArrayList<String>();
        ArrPhoneNum = new ArrayList<String>();
        Arrmedia = new ArrayList<String>();
        arrmesstype = new ArrayList<String>();
        arrmmstext = new ArrayList<String>();
        messagesload = false;

    }

    // Alert Message for if Wifi not available
//    void alertMessageforWIFI() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
//        // You Can Customise your Title here
//        alertDialog.setTitle("Aack Aack");
//        alertDialog.setMessage("Needs WiFi only or Data Charges");
//
//        alertDialog.setCancelable(false);
//        alertDialog.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.alert = true;
//
//                        try {
//                            clear();
//                            FragmentManager fm = getActivity()
//                                    .getSupportFragmentManager();
//                            FragmentTransaction ft = fm.beginTransaction();
//                            MessagesFragment fragment = new MessagesFragment();
//                            ft.replace(R.id.activity_main_content_fragment,
//                                    fragment);
//                            ft.addToBackStack(null);
//                            ft.commit();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        dialog.cancel();
//                    }
//                });
//
//        alertDialog.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Home.alert = false;
//
//                        FragmentManager fm = getActivity()
//                                .getSupportFragmentManager();
//                        ExploreFragment fragment = new ExploreFragment();
//                        FragmentTransaction ft = fm.beginTransaction();
//                        ft.replace(R.id.activity_main_content_fragment,
//                                fragment);
//                        ft.addToBackStack(null);
//                        ft.commit();
//
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
//    }
}