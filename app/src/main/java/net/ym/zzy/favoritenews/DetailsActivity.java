package net.ym.zzy.favoritenews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.constant.WBConstants;

import net.ym.zzy.favoritenews.base.BaseActivity;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.mvp.presenter.CommentPresenter;
import net.ym.zzy.favoritenews.mvp.view.CommentView;
import net.ym.zzy.favoritenews.service.NewsDetailsService;
import net.ym.zzy.favoritenews.tool.DateTools;
import net.ym.zzy.favoritenews.weibo.auth.WBShareHelper;

@SuppressLint("JavascriptInterface")
public class DetailsActivity extends BaseActivity implements CommentView, IWeiboHandler.Response{

	private TextView title;
	private ProgressBar progressBar;
	private FrameLayout customview_layout;
	private String news_url;
	private String news_title;
	private String news_source;
	private String news_date;
	private NewsModel news;
	private TextView action_comment_count;
	private View actionWriteComment;
	private View actionViewComment;
	private EditText commentEditText;
	private View commentLayout;
	private View actionRepost;
	WebView webView;
	private Oauth2AccessToken mAccessToken;
	private CommentPresenter mCommentPresenter;
	private View sendText;

	private WBShareHelper mWBShareHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mAccessToken = AccessTokenKeeper.readAccessToken(this);

		setContentView(R.layout.details);
		setNeedBackGesture(true);//设置需要手势监听
		getData();
		initView();
		initWebView();
		mCommentPresenter = new CommentPresenter(this);

		mWBShareHelper = new WBShareHelper(this);
		if (savedInstanceState != null) {
			mWBShareHelper.handleWeiboResponse(getIntent(), this);
		}
	}
	/* 获取传递过来的数据 */
	private void getData() {
		news = (NewsModel) getIntent().getSerializableExtra("news");
//		news_url = news.getSource_url();
		if (mAccessToken == null || "".equals(mAccessToken.getUid())) {
			news_url = Constants.HOST + "view_news/" + news.getId() + "/";
		}else{
			news_url = Constants.HOST + "view_news/" + news.getId() + "/?uid=" + mAccessToken.getUid();
		}
		news_title = news.getTitle();
		news_source = news.getSource();
		news_date = DateTools.getNewsDetailsDate(String.valueOf(news.getPublishTime()));
	}

	private void initWebView() {
		webView = (WebView)findViewById(R.id.wb_details);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		if (!TextUtils.isEmpty(news_url)) {
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);//设置可以运行JS脚本
//			settings.setTextZoom(120);//Sets the text zoom of the page in percent. The default is 100.
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			settings.setUseWideViewPort(true); //打开页面时， 自适应屏幕
			settings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕

			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
			settings.setLoadWithOverviewMode(true);

			settings.setSupportZoom(true);// 用于设置webview放大
			settings.setBuiltInZoomControls(false);
			settings.setPluginState(WebSettings.PluginState.ON);
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			webView.setBackgroundResource(R.color.transparent);
			// 添加js交互接口类，并起别名 imagelistner
			webView.addJavascriptInterface(new JavascriptInterface(getApplicationContext()), "imagelistner");
			webView.setWebChromeClient(new MyWebChromeClient());
			webView.setWebViewClient(new MyWebViewClient());
			webView.loadUrl(news_url);
		}
	}

	private void initView() {
		actionWriteComment = findViewById(R.id.action_write_comment);
		actionViewComment = findViewById(R.id.action_view_comment);
		commentLayout = findViewById(R.id.comment_layout);
		commentEditText = (EditText)findViewById(R.id.comment);
		sendText = findViewById(R.id.sendText);

		actionRepost = findViewById(R.id.action_repost);

		title = (TextView) findViewById(R.id.title);
		progressBar = (ProgressBar) findViewById(R.id.ss_htmlprogessbar);
		customview_layout = (FrameLayout) findViewById(R.id.customview_layout);
		//底部栏目
		action_comment_count = (TextView) findViewById(R.id.action_comment_count);
		
//		progressBar.setVisibility(View.VISIBLE);
		title.setTextSize(13);
		title.setVisibility(View.VISIBLE);
//		title.setText(news_url);
		action_comment_count.setText(String.valueOf(news.getCommentCount()));

		actionWriteComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commentLayout.setVisibility(View.VISIBLE);
				commentEditText.requestFocus();

			}
		});

		actionViewComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailsActivity.this, CommentListActivity.class);
				intent.putExtra("news", news);
				startActivity(intent);
			}
		});

		commentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					commentLayout.setVisibility(View.GONE);
					InputMethodManager inputManager = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
				} else {
					commentLayout.setVisibility(View.VISIBLE);
					InputMethodManager inputManager = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(commentEditText, 0);
				}
			}
		});

		sendText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = commentEditText.getText().toString();
				mCommentPresenter.pushNewsComment(mAccessToken.getUid(), mAccessToken.getToken(), news.getId(), content);
			}
		});

		actionRepost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWBShareHelper.shareWeibo(news);
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWBShareHelper.handleWeiboResponse(intent, this);
	}

	@Override
	public void onResponse(BaseResponse baseResponse) {
		switch (baseResponse.errCode) {
			case WBConstants.ErrorCode.ERR_OK:
				Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				Toast.makeText(this,
						"分享失败 " + "Error Message: " + baseResponse.errMsg,
						Toast.LENGTH_LONG).show();
				break;
		}
	}

	@Override
	public void onBackPressed() {
		if (webView != null){
			webView.reload();
			webView.onPause();
		}
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+',';"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl);  "
				+ "    }  " + "}" + "})()");
	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

        @android.webkit.JavascriptInterface
		public void openImage(String img) {

			//
			String[] imgs = img.split(",");
			ArrayList<String> imgsUrl = new ArrayList<String>();
			for (String s : imgs) {
				imgsUrl.add(s);
				Log.i("图片的URL>>>>>>>>>>>>>>>>>>>>>>>", s);
			}
			Intent intent = new Intent();
			intent.putStringArrayListExtra("infos", imgsUrl);
			intent.setClass(context, ImageShowActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		if (webView != null){
			webView.reload();
			webView.onPause();
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			WebResourceResponse response = null;
			response = super.shouldInterceptRequest(view, url);
			return response;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			Log.d("URL", url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			title.setText(news_url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			progressBar.setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	
	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if(newProgress != 100){
				progressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public void onLoadingData() {

	}

	@Override
	public void onLoadDataSuccessfully(Model model) {

	}

	@Override
	public void onLoadDataError() {

	}

	@Override
	public void onSendingData() {

	}

	@Override
	public void onSendDataSuccessfully() {
		Intent intent = new Intent(DetailsActivity.this, CommentListActivity.class);
		intent.putExtra("news", news);
		startActivity(intent);
	}

	@Override
	public void onSendDataError() {

	}

	@Override
	public Context getContext() {
		return this;
	}
}
