package net.ym.zzy.favoritenews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.ym.zzy.favoritenews.adapter.CollectedNewsAdapter;
import net.ym.zzy.favoritenews.base.BaseActivity;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsListModel;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.mvp.presenter.CollectedNewsPresenter;
import net.ym.zzy.favoritenews.mvp.view.CollectedNewsView;

/**
 * Created by zengzheying on 15/4/27.
 */
public class CollectsActivity extends BaseActivity implements CollectedNewsView{

    private SwipeRefreshLayout mRefreshLayout;
    private View loading_layout;
    private View load_error;
    private View no_content;
    private ListView mCollectListView;
    private TextView mTitleView;

    private Oauth2AccessToken mAccessToken;

    private CollectedNewsPresenter mCollectedNewsPresenter;

    private CollectedNewsAdapter mCollectedNewsAdapter;

    private boolean isRefresh;

    private int mVisibleLastIndex = 0;
    private int mVisibleItemCount = 0;
    private int mSelectedScrollY = 0;
    private int mPage = 0;

    private View footerView;

    private static final int HAVE_MORE_DATA = 1;
    private static final int HAVE_NO_MORE_DATA = 2;
    private int mCurrentState = HAVE_MORE_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collects);

        initView();

        mCollectedNewsPresenter = new CollectedNewsPresenter(this);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        loadData(mPage, true);
    }

    private void initView(){
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        loading_layout = findViewById(R.id.loading_layout);
        load_error = findViewById(R.id.load_error);
        no_content = findViewById(R.id.no_contents);
        mCollectListView = (ListView)findViewById(R.id.collect_list);
        mTitleView = (TextView)findViewById(R.id.title);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.list_loading_footer, null, false);

        mRefreshLayout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
        load_error.setVisibility(View.GONE);
        no_content.setVisibility(View.GONE);

        mTitleView.setText(R.string.activity_collect_title);

        mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 0;
                loadData(mPage, true);
            }
        });

        load_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage = 0;
                loadData(mPage, true);
            }
        });
    }

    private void loadData(int pageIndex, boolean isRefresh){
        this.isRefresh = isRefresh;
        if (pageIndex == 0 && mCollectListView != null){
            if (mCollectListView.getFooterViewsCount() > 0){
                mCollectListView.removeFooterView(footerView);
            }
        }
        mCollectedNewsPresenter.pullCollectedNewsList(mAccessToken.getUid(), mAccessToken.getToken(), pageIndex, isRefresh);
    }

    @Override
    public void onPullNewsListException(Exception ex) {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.GONE);
        load_error.setVisibility(View.VISIBLE);
        no_content.setVisibility(View.GONE);
    }

    @Override
    public void onPushNewsException(Exception ex) {

    }

    @Override
    public void onLoadingData() {

    }

    @Override
    public void onLoadDataSuccessfully(Model model) {
        NewsListModel newsListModel = (NewsListModel)model;
        mRefreshLayout.setRefreshing(false);
        if (newsListModel != null) {
            if (newsListModel.getNewsModelList().size() == Constants.PAGE_SIZE){
                mCurrentState = HAVE_MORE_DATA;
            }else{
                mCurrentState = HAVE_NO_MORE_DATA;
            }
            if (mCollectedNewsAdapter == null){
                mCollectedNewsAdapter = new CollectedNewsAdapter(this);
                mCollectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        NewsModel newsModel = mCollectedNewsAdapter.getItem(position);
                        intent.putExtra("news", newsModel);
                        startActivity(intent);
                        CollectsActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }
            if (isRefresh){
                mCollectedNewsAdapter.clearData();
            }
            mCollectedNewsAdapter.addData(newsListModel.getNewsModelList());
        }

        if (mPage == 0){
            if (mCollectedNewsAdapter.getCount() >= Constants.PAGE_SIZE) {
                mCollectListView.addFooterView(footerView);
            }
            mCollectListView.setOnScrollListener(mOnScrollListener);
        }

        if (mCurrentState == HAVE_NO_MORE_DATA){
            ((TextView)footerView.findViewById(R.id.tv_loading)).setText("没有更多了");
            footerView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            footerView.setOnClickListener(null);
        }else if (mCurrentState == HAVE_MORE_DATA){
            ((TextView)footerView.findViewById(R.id.tv_loading)).setText("点击加载更多");
            footerView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPage += 1;
                    loadData(mPage, true);
                    ((TextView)footerView.findViewById(R.id.tv_loading)).setText("正在加载...");
                    footerView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
                    footerView.setOnClickListener(null);
                }
            });
        }

        if (mCollectedNewsAdapter.getCount() > 0) {
            mRefreshLayout.setVisibility(View.VISIBLE);
            loading_layout.setVisibility(View.GONE);
            load_error.setVisibility(View.GONE);
            no_content.setVisibility(View.GONE);
            mCollectListView.setAdapter(mCollectedNewsAdapter);
        }else{
            mRefreshLayout.setVisibility(View.GONE);
            loading_layout.setVisibility(View.GONE);
            load_error.setVisibility(View.GONE);
            no_content.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataError() {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
        load_error.setVisibility(View.GONE);
        no_content.setVisibility(View.GONE);
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
        return this;
    }

    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//			int itemsLastIndex = mAdapter.getCount()-1;  //数据集最后一项的索引
//			int lastIndex = itemsLastIndex;
//			Log.d("onScrollListener", "onScroll" + (mVisibleLastIndex == lastIndex));
//			if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
//					&& mVisibleLastIndex == lastIndex) {
//				// 自动加载,在这里放置异步加载数据的代码
//				mPage += 1;
//				loadNewsData(channel_id, mPage, true);
//			}
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d("onScrollListener", "onScroll" + firstVisibleItem);
            mSelectedScrollY = view.getScrollY();
            mVisibleItemCount = visibleItemCount;
            mVisibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        }
    };
}
