package com.aackaacknew.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class DataUrls {
	public static Intent svc;

	public static String dialogtitle = "No internet connectivity";
	public static String dialogmsg = "Sorry, but you need to be connected to the internet to use AackAack";
	public static String dialogbutton = "OK";
	public static ArrayList<String> mmsids = new ArrayList<String>();
	public static ArrayList<String> mmsids22 = new ArrayList<String>();
	public static String backup_msg = "Please wait. Your Messages are Backing-Up. It may take a few seconds.";

	public static boolean checkhome, homenew;// profilenew;
	public static String social_sharing = "";
	public static String from = "";
	public static String share_myfav1 = "";
	public static boolean backup_staus = false, bool_start_5msgs = false;
	public static String screen_look = "", strCaption = "",
			conversation_to = "Friend";

	public static String baseURL = "http://35.163.222.1/AackAack"; // aackaacknew

	public static String clienthost = baseURL + "/index.php";

	public static String amazonURL = "https://aackaack-s3.s3.amazonaws.com/";

	public static boolean fb, twitter, tumblr, email, imgur;
	public static boolean profileupdate;
	public static String from_share = "";
	public static boolean position;

	public static ArrayList<String> providers = new ArrayList<String>();
	public static Bitmap bit;

	public static String twitactivity = new String();

	public static String bitlyURL = "https://api-ssl.bitly.com/v3/shorten?access_token=efa94ad1ac9b5dcc578969b941804efca1855ca9";

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)

		.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.threadPoolSize(5).threadPriority(Thread.MIN_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		ImageLoader.getInstance().init(config);
	}

	static class PatchInputStream extends FilterInputStream {
		public PatchInputStream(InputStream in) {
			super(in);
		}

		public long skip(long n) throws IOException {
			long m = 0L;
			while (m < n) {
				long _m = in.skip(n - m);
				if (_m == 0L)
					break;
				m += _m;
			}
			return m;
		}
	}

	// For Message Search in messages screen
	public static String messagesearch = clienthost + "/api/searchmsgs?";
	// For social signin for facebook,twitter,instagram
	public static String socialsignin = clienthost
			+ "/new_user_social/signup?logintype=";
	// For socialSignup for facebook,twitter,instagram.
	public static String updateprofile = clienthost
			+ "/socialSignin/updateProfile?logintype=";
	// For general registration
	public static String login = clienthost + "/newuser/addUser?";
	// For general login
	public static String general_login = clienthost
			+ "/newuser/loginUser?logintype=3&username=";

	public static String exploresearch = clienthost
			+ "/search/hashtags?string=";

	public static String getcomments = clienthost
			+ "/comments/getComments?aackid=";
	public static String addcomment = clienthost
			+ "/comments/addComment?userid=";

	public static String aackcount = clienthost
			+ "/like_aack/aackCount?aackid=";
	public static String likedusers = clienthost
			+ "/like_aack/likedUsers?aackid=";

	public static String likeaack = clienthost + "/like_aack/likeAack?userid=";

	public static String home = clienthost + "/api/getMessageufollow?";
	public static String samplearray = clienthost + "/samplebackupapi/backup";

	public static String profiles_followers = clienthost
			+ "/getfolloweelist/getFolloweeList?";
	public static String profiles_following = clienthost
			+ "/getfollowerslist/getFollowList?";

	public static String get_msgs = clienthost + "/api/getMessages?";
	public static String fullchat = clienthost + "/api/getMessageBody?";
	public static String add_aack = clienthost + "/api/addAack?";
	public static String share_aack = clienthost + "/api/shareAack?";

	public static String search = clienthost + "/api/searchUsers?";

	public static String unfollow = clienthost + "/api/unFollow?";
	public static String addfallowers = clienthost + "/api/addFollow?";

	public static String logout = clienthost + "/api/logOut?";
	public static String profile = clienthost + "/api/profile?";

	public static String reaack = clienthost + "/api/reshareAack?";

	public static String favs = clienthost + "/api/getFavAacks?";
	public static String favs_details = clienthost + "/api/getUseraacks?";

	public static String home_details = clienthost + "/api/detailprofile?";

	public static String followstatus = clienthost + "/api/getFollowStatus?";

	public static String delete_aack = clienthost + "/delete/aack?";

	public static String delete_reshareaack = clienthost + "/api/del_reshare?";

	public static String userprofile_update = clienthost + "/userprofile/view?";
	public static String profile_update = clienthost + "/userprofile/update?";
	public static String forgotPassword = clienthost + "/forgotpassword/user?";

	public static String lastmessage_time = clienthost
			+ "/getmessagelasttime/gettime?";

	public static String exploresearch_users = clienthost + "/search/Users?";

}
