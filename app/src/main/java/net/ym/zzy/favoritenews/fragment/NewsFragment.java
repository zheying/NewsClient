package net.ym.zzy.favoritenews.fragment;

import java.io.Serializable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.ym.zzy.domain.entity.json.NewsJson;
import net.ym.zzy.domain.respository.DataRepository;
import net.ym.zzy.favorite.data.respository.DataReposityImpl;
import net.ym.zzy.favoritenews.DetailsActivity;
import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.adapter.NewsAdapter;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsListModel;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.mvp.presenter.NewsListPresenter;
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
	ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	//Toast提示框
	private RelativeLayout notify_view;
	private TextView notify_view_text;

    private SwipeRefreshLayout swipeRefreshLayout;

	private NewsListPresenter mNewsListPresenter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;
		mNewsListPresenter = new NewsListPresenter(this);
		loadNewsData(channel_id, 0, true);
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
//			}else{
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						try {
//							Thread.sleep(2);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//					}
//				}).start();
//			}
		}else{
			//fragment不可见时不执行操作
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
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
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
				loadNewsData(channel_id, 0, true);
			}
		});
		return view;
	}

//	private void initData() {
////		newsList = Constants.getNewsList();
//        DataRepository dataRepository = DataReposityImpl.getInstance();
//        dataRepository.getNewsList(getActivity(), 1, 0, true, new DataRepository.ResponseCallback() {
//			@Override
//			public void onResponse(Serializable ser) {
//				NewsJson newsJson = (NewsJson) ser;
//				if (newsJson != null && newsJson.getCode() == 0) {
//					newsList = newsJson.getData().getNewsList();
//					handler.obtainMessage(SET_NEWSLIST).sendToTarget();
//				}
//
//				if (swipeRefreshLayout != null) {
//					swipeRefreshLayout.setRefreshing(false);
//				}
//			}
//
//			@Override
//			public void onException(Exception ex) {
//				Toast.makeText(getActivity(), "异常", Toast.LENGTH_SHORT).show();
//				ex.printStackTrace(System.err);
//			}
//		});
//	}

	private void loadNewsData(int catalog, int pageIndex, boolean isRefresh){
		mNewsListPresenter.loadData(catalog, pageIndex, isRefresh);
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
				}
                int count = mAdapter.addData(newsList);
				mListView.setAdapter(mAdapter);
				mListView.setOnScrollListener(mAdapter);
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
				if (count > 0) {
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
}
