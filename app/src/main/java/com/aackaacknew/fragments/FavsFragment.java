package com.aackaacknew.fragments;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.aackaacknew.activities.FavsDetailsActivity;
import com.aackaacknew.activities.Home;
import com.aackaacknew.activities.MainActivity;
import com.aackaacknew.activities.MainLayout;
import com.aackaacknew.activities.R;
import com.aackaacknew.activities.ui.profile.ProfileFragment;
import com.aackaacknew.adapters.FavsListAdapter;
import com.aackaacknew.utils.CheckInternetConnection;
import com.aackaacknew.utils.DataUrls;
import com.aackaacknew.utils.MyProgressDialog;
import com.aackaacknew.utils.UrltoValue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class FavsFragment extends Fragment
{
	ArrayList<String> favs_conversation,favs_count,favs_picture,favs_username,favs_AackUserid,favs_screenLook,favs_lastmessage;
	FavsListAdapter favs_adapter;
	Button btn_menu;
	RelativeLayout relBottom_msgs_loading,relBack;
	public boolean mainlayout_visible=true,checkbakup,lodingForLatestMessages;
	MainLayout mLayout;
	MyProgressDialog dialog;
	boolean isFavs_first;
	int count;
	String strAacksFavs_count, strPic_Favs, strName_Favs, strUsername_Favs,strAackuserid_Favs;
	TextView txtAacksFavCount, txtSubname_Favs, txtTitle_Favs,tvfavs;
	ImageView imgHeadPic_Favs;
	SharedPreferences prefs;
	View footerView;
	ListView lst_favs;
	public static String strUserId,strAack_id;
	public boolean favs =false;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View view = inflater.inflate(R.layout.favs, null);
        
		isFavs_first=true;
		  favs = false;
		
		favs_conversation = new ArrayList<String>();
		favs_count = new ArrayList<String>();
		favs_picture = new ArrayList<String>();
		favs_username = new ArrayList<String>();
		favs_AackUserid = new ArrayList<String>();
		favs_screenLook=new ArrayList<String>();
		favs_lastmessage=new ArrayList<String>();

        
        //To get the all id's..
//        btn_menu = (Button)view. findViewById(R.id.menu);
        lst_favs=(ListView)view. findViewById(R.id.list);
        footerView = inflater.inflate(R.layout.listfooter, null);
        relBottom_msgs_loading = (RelativeLayout)view.findViewById(R.id.bottomlayout);
//        relBack= (RelativeLayout)view.findViewById(R.id.relback);
        DataUrls.initImageLoader(getActivity());
        txtSubname_Favs = (TextView) view.findViewById(R.id.subname);
		txtTitle_Favs = (TextView) view.findViewById(R.id.title);
		txtAacksFavCount = (TextView) view.findViewById(R.id.count);
		imgHeadPic_Favs = (ImageView) view.findViewById(R.id.image);
		tvfavs=(TextView)view.findViewById(R.id.tv_favs);
		
        mLayout= Home.mLayout;
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        lst_favs.setScrollingCacheEnabled(false);
        
        
        if(ProfileFragment.image!=null)
        {
        	favs = false;
        	try
	        {
        		imgHeadPic_Favs.setImageBitmap(ProfileFragment.image);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
        }
        else
        {
        	favs = true;
        }
        
     // To Hide/Show the Menu 
        relBack.setOnClickListener(new OnClickListener() 
        {
    		@Override
    		public void onClick(View v) 
    		{
    			//hide key board
    			final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    		
    			// Show/hide the menu
    			if(mainlayout_visible)
    			{
    				mLayout.toggleMenu();
    				mainlayout_visible=false;
    			}
    			else
    			{
    				mLayout.toggleMenu();
                    mainlayout_visible=true;
    			}
    		}
    	});
        
        lst_favs.setOnScrollListener(new EndlessScrollListenerFavs());

        if (CheckInternetConnection.isOnline(getActivity())) 
        {
        	doInFavsListtData ls_favs = new doInFavsListtData(0);
        	ls_favs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	
        }
        else
        {
     		Toast.makeText(getActivity(),DataUrls.dialogtitle, Toast.LENGTH_SHORT).show();
     	}
        // When called List Item(Favs_Aack)
     	lst_favs.setOnItemClickListener(new OnItemClickListener() 
     	{
     		@Override
     		public void onItemClick(AdapterView<?> arg0,View arg1, int pos, long arg3) 
     		{
     			try 
     			{
     				Intent intent = new Intent(getActivity(),FavsDetailsActivity.class);
     				strAack_id=favs_AackUserid.get(pos);
     				intent.putExtra("screenLook", favs_screenLook.get(pos));
     				intent.putExtra("conversationfrom", favs_conversation.get(pos));
     				intent.putExtra("number", favs_AackUserid.get(pos));
     				intent.putExtra("lastmessage", favs_lastmessage.get(pos));
     				startActivity(intent);
     			}
     			catch (Exception e) 
     			{
     				e.printStackTrace();
     			}
     		}
		});
     	return view;
    }//oncreate.......
    
	// async for favs list data...
	class doInFavsListtData extends AsyncTask<URL, Integer, Long> 
	{
		int msgscount;
		public doInFavsListtData(int msgscount)
		{
			super();
			this.msgscount = msgscount;
			lodingForLatestMessages=true;
		}
		protected void onPreExecute() 
		{
			if (isFavs_first)
			{
				dialog = MyProgressDialog.show(getActivity(), null, null);
			}
			else
			{
				footerView.setVisibility(View.VISIBLE);
			}
		}
		@Override
		protected Long doInBackground(URL... arg0) 
		{
			try 
			{
				String loginResponse = null;
				prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				strUserId = prefs.getString("userid", "");
				loginResponse = UrltoValue.getValuefromUrl(DataUrls.favs+ "userid=" + strUserId + "&start=" + msgscount);
				
				count = 0;
				if (!loginResponse.equals("zero")) 
				{
					try 
					{
						JSONObject jobject = new JSONObject(loginResponse);
						strAacksFavs_count = jobject.getString("favscount");
						strPic_Favs = jobject.getString("userpic");
						strName_Favs = jobject.getString("fullname");
						strUsername_Favs = jobject.getString("username");
						JSONArray jsonArray = jobject.getJSONArray("data");

						if (jsonArray.length() != 0)
						{
							for (int i = 0; i < jsonArray.length(); i++) 
							{
								count++;// added for aptr elements
								JSONObject jsonObject = jsonArray.getJSONObject(i);

								favs_picture.add(jsonObject.getString("picture"));
								favs_username.add(jsonObject.getString("username"));
								favs_conversation.add(jsonObject.getString("conversationfrom"));
								favs_count.add(jsonObject.getString("count"));
								favs_AackUserid.add(jsonObject.getString("number"));
								favs_screenLook.add(jsonObject.getString("screenLook"));
								favs_lastmessage.add(jsonObject.getString("lastmessage"));
							}
						}
						else
						{
							getActivity().runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									lst_favs.removeFooterView(footerView);
								}
							});
						}
					}
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Long result) 
		{
			try 
			{
				lodingForLatestMessages=false;
				DisplayImageOptions	optionscustomimge = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.build();
					
				txtAacksFavCount.setText(strAacksFavs_count);
				txtTitle_Favs.setText(strName_Favs);
				txtSubname_Favs.setText("@" + strUsername_Favs);
				
				if(favs)
				{
					ImageLoader.getInstance().displayImage(strPic_Favs.replace(" ", "%20"), imgHeadPic_Favs,optionscustomimge,null);
				}
				if(strAacksFavs_count.equals("0"))
				{
					
					tvfavs.setText("");
					tvfavs.setText("This is where you save your special AACKs you like to read often, but are only for you");
					tvfavs.setVisibility(View.VISIBLE);
					lst_favs.setVisibility(View.GONE);
				}
				else
				{
					tvfavs.setVisibility(View.GONE);
					lst_favs.setVisibility(View.VISIBLE);
				}
			} 
			catch (Exception e) 
			{
				Resources res = getResources(); // need this to fetch the drawable
				Drawable draw = res.getDrawable( R.drawable.default_profilepic );
				imgHeadPic_Favs.setImageDrawable(draw);
				e.printStackTrace();
			}
			if (isFavs_first) 
			{
				favs_adapter = new FavsListAdapter(getActivity(),R.layout.favs_list_item, favs_username,favs_conversation, favs_count, favs_picture,favs_AackUserid);
				lst_favs.addFooterView(footerView);
				footerView.setVisibility(View.GONE);
				lst_favs.setAdapter(favs_adapter);
			} 
			else
			{
				favs_adapter.notifyDataSetChanged();
			}
			if(isFavs_first)
			{
				try{dialog.dismiss();}catch(Exception e){e.printStackTrace();}
			}
			isFavs_first=false;
			relBottom_msgs_loading.setVisibility(View.GONE);
		}
	}// async..........

	//EndlessScroll listener for loading 10 by 10
	public class EndlessScrollListenerFavs implements OnScrollListener 
	{
	    public EndlessScrollListenerFavs() 
	    {
	    }
	    public EndlessScrollListenerFavs(int visibleThreshold) 
	    {
	    }
	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
	    {
	    }
	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) 
	    {
			if(scrollState==SCROLL_STATE_IDLE)
			{
				if(lst_favs.getLastVisiblePosition()==lst_favs.getCount()-1)
				{
					if(!lodingForLatestMessages)
					{
						new doInFavsListtData(lst_favs.getCount()-lst_favs.getFooterViewsCount()).execute();
					}
				}
			}
	    }
	}
}
