package net.ym.zzy.favoritenews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.ym.zzy.favoritenews.adapter.CollectedNewsAdapter;
import net.ym.zzy.favoritenews.base.BaseActivity;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsListModel;
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

    private Oauth2AccessToken mAccessToken;

    private CollectedNewsPresenter mCollectedNewsPresenter;

    private CollectedNewsAdapter mCollectedNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collects);

        initView();

        mCollectedNewsPresenter = new CollectedNewsPresenter(this);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        loadData(0, true);
    }

    private void initView(){
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        loading_layout = findViewById(R.id.loading_layout);
        load_error = findViewById(R.id.load_error);
        no_content = findViewById(R.id.no_contents);
        mCollectListView = (ListView)findViewById(R.id.collect_list);

        mRefreshLayout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
        load_error.setVisibility(View.GONE);
        no_content.setVisibility(View.GONE);

        mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(0, true);
            }
        });

        load_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(0, true);
            }
        });
    }

    private void loadData(int pageIndex, boolean isRefresh){
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
            if (mCollectedNewsAdapter == null){
                mCollectedNewsAdapter = new CollectedNewsAdapter(this);
            }
            mCollectedNewsAdapter.addData(newsListModel.getNewsModelList());
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
}
