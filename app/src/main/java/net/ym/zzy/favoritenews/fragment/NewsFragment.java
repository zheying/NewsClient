package net.ym.zzy.favoritenews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.ym.zzy.favoritenews.DetailsActivity;
import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.adapter.NewsAdapter;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsListModel;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.mvp.presenter.CollectedNewsPresenter;
import net.ym.zzy.favoritenews.mvp.presenter.NewsListPresenter;
import net.ym.zzy.favoritenews.mvp.view.CollectedNewsView;
import net.ym.zzy.favoritenews.mvp.view.NewsListView;
import net.ym.zzy.favoritenews.tool.Constants;
import net.ym.zzy.favoritenews.view.HeadListView;

public class NewsFragment extends Fragment implements NewsListView{
	private final static String TAG = "NewsFragment";
	Activity activity;
    List<NewsModel> newsList = new ArrayList<NewsModel>();
	HeadListView mListView;
	NewsAdapter mAdapter;
	String text;
	int channel_id;
	View detail_loading;
	public final static int SET_NEWSLIST = 0;
	//Toast提示框
	private RelativeLayout notify_view;
	private TextView notify_view_text;

	private Oauth2AccessToken mAccessToken;

    private SwipeRefreshLayout swipeRefreshLayout;

	private NewsListPresenter mNewsListPresenter;

	private int mVisibleLastIndex = 0;
	private int mVisibleItemCount = 0;
	private int mPage = 0;

	private static final int HAVE_MORE_DATA = 1;
	private static final int HAVE_NO_MORE_DATA = 2;
	private int mCurrentState = HAVE_MORE_DATA;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;
		mNewsListPresenter = new NewsListPresenter(this);
		mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
		loadNewsData(channel_id, mPage, true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}
	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			//fragment可见时加载数据
			if(newsList !=null && newsList.size() !=0) {
                handler.obtainMessage(SET_NEWSLIST).sendToTarget();
            }
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		mListView = (HeadListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		detail_loading = view.findViewById(R.id.detail_loading);
		//Toast提示框
		notify_view = (RelativeLayout)view.findViewById(R.id.notify_view);
		notify_view_text = (TextView)view.findViewById(R.id.notify_view_text);
		item_textview.setText(text);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mPage = 0;
				loadNewsData(channel_id, mPage, true);
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
	}

	private void loadNewsData(int catalog, int pageIndex, boolean isRefresh){
		mNewsListPresenter.loadData(mAccessToken.getUid(), mAccessToken.getToken(), catalog, pageIndex, isRefresh);
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SET_NEWSLIST:
				detail_loading.setVisibility(View.GONE);
				if(mAdapter == null){
                    mAdapter = new NewsAdapter(activity);
					mAdapter.setPopupClickListener(mPopupClickListener);
				}
                int count = mAdapter.addData(newsList);
				mListView.setAdapter(mAdapter);
				mListView.setOnScrollListener(mOnScrollListener);
				mListView.setPinnedHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_item_section, mListView, false));
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(activity, DetailsActivity.class);
						if(channel_id == Constants.CHANNEL_CITY){
							if(position != 0){
								intent.putExtra("news", mAdapter.getItem(position - 1));
								startActivity(intent);
								activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
							}
						}else{
							intent.putExtra("news", mAdapter.getItem(position));
							startActivity(intent);
							activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						}
					}
				});
				if (count > 0 && mPage == 0) {
					initNotify(count);
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	
	/* 初始化通知栏目*/
	private void initNotify(final int count) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notify_view_text.setText(String.format(getString(R.string.ss_pattern_update), count));
				notify_view.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notify_view.setVisibility(View.GONE);
					}
				}, 2000);
			}
		}, 1000);
	}
	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("onDestroyView", "channel_id = " + channel_id);
		mAdapter = null;
	}
	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "channel_id = " + channel_id);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}



	@Override
	public void onLoadingData() {

	}

	@Override
	public void onLoadDataSuccessfully(Model model) {
		NewsListModel newsListModel = (NewsListModel)model;
		newsList = newsListModel.getNewsModelList();
		if (newsList != null && newsList.size() == net.ym.zzy.favoritenews.Constants.PAGE_SIZE){
			mCurrentState = HAVE_MORE_DATA;
		}else {
			mCurrentState = HAVE_NO_MORE_DATA;
		}
		handler.obtainMessage(SET_NEWSLIST).sendToTarget();
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	public void onLoadDataError() {
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}
		Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSendingData() {

	}

	@Override
	public void onSendDataSuccessfully() {

	}

	@Override
	public void onSendDataError() {

	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	NewsAdapter.PopupClickListener mPopupClickListener = new NewsAdapter.PopupClickListener() {

		@Override
		public void onCollectClick(NewsModel newsModel) {
			CollectedNewsPresenter mCollectedNewsPresenter = new CollectedNewsPresenter(new ListCollectNewsView(newsModel));
			mCollectedNewsPresenter.pushCollectedNews(mAccessToken.getUid(), mAccessToken.getToken(), newsModel.getId());
		}

		@Override
		public void onDislikeClick(int news_id) {
			mNewsListPresenter.dislikeNew(mAccessToken.getUid(), mAccessToken.getToken(), news_id);
		}

		class ListCollectNewsView implements CollectedNewsView {

			NewsModel mNewsModel;

			public ListCollectNewsView(NewsModel newsModel) {
				mNewsModel = newsModel;
			}

			@Override
			public void onPullNewsListException(Exception ex) {

			}

			@Override
			public void onPushNewsException(Exception ex) {

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
				if (!mNewsModel.getCollectStatus()) {
					Toast.makeText(getContext(), R.string.collect_success, Toast.LENGTH_SHORT).show();
					mNewsModel.setCollectStatus(true);
				} else {
					Toast.makeText(getContext(), R.string.collect_cancel, Toast.LENGTH_SHORT).show();
					mNewsModel.setCollectStatus(false);
				}
			}

			@Override
			public void onSendDataError() {

			}

			@Override
			public Context getContext() {
				return getActivity();
			}
		}

	};

	AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			int itemsLastIndex = mAdapter.getCount()-1;  //数据集最后一项的索引
			int lastIndex = itemsLastIndex;
			Log.d("onScrollListener", "onScroll" + (mVisibleLastIndex == lastIndex));
			if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
					&& mVisibleLastIndex == lastIndex) {
				// 自动加载,在这里放置异步加载数据的代码
				mPage += 1;
				loadNewsData(channel_id, mPage, true);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Log.d("onScrollListener", "onScroll");
			mVisibleItemCount = visibleItemCount;
			mVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		}
	};


}
