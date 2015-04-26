package net.ym.zzy.favoritenews;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.ym.zzy.favoritenews.adapter.CommentsAdapter;
import net.ym.zzy.favoritenews.base.BaseActivity;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.mvp.model.CommentListModel;
import net.ym.zzy.favoritenews.mvp.model.Model;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.mvp.presenter.CommentPresenter;
import net.ym.zzy.favoritenews.mvp.view.CommentListView;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentListActivity extends BaseActivity implements CommentListView{

    private ListView comment_list;
    private View loading;
    private View no_comments;
    private View load_error;
    private Oauth2AccessToken mAccessToken;
    private NewsModel mNewsModel;

    private CommentsAdapter mCommentsAdapter;

    private CommentPresenter mCommentPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        initViews();

        mNewsModel = (NewsModel)getIntent().getSerializableExtra("news");

        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        mCommentPresenter = new CommentPresenter(this);
        mCommentPresenter.pullCommentList(mAccessToken.getUid(), mAccessToken.getToken(), mNewsModel.getId());
    }

    private void initViews(){
        comment_list = (ListView)findViewById(R.id.comment_list);
        loading = findViewById(R.id.loading_layout);
        no_comments = findViewById(R.id.no_comments);
        load_error = findViewById(R.id.load_error);

        comment_list.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        no_comments.setVisibility(View.GONE);
        load_error.setVisibility(View.GONE);

        load_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentPresenter.pullCommentList(mAccessToken.getUid(), mAccessToken.getToken(), mNewsModel.getId());
            }
        });
    }

    @Override
    public void onLoadingData() {
        comment_list.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        no_comments.setVisibility(View.GONE);
        load_error.setVisibility(View.GONE);
    }

    @Override
    public void onLoadDataSuccessfully(Model model) {
        CommentListModel commentListModel = (CommentListModel)model;
        if (commentListModel != null){
            if (mCommentsAdapter == null){
                mCommentsAdapter = new CommentsAdapter(this);
                comment_list.setAdapter(mCommentsAdapter);
            }
            mCommentsAdapter.addData(commentListModel.getCommentModels());
            if (mCommentsAdapter.getCount() == 0){
                comment_list.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                no_comments.setVisibility(View.VISIBLE);
                load_error.setVisibility(View.GONE);
            }else{
                comment_list.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                no_comments.setVisibility(View.GONE);
                load_error.setVisibility(View.GONE);
            }
        }else{
            comment_list.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            no_comments.setVisibility(View.VISIBLE);
            load_error.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadDataError() {
        comment_list.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        no_comments.setVisibility(View.GONE);
        load_error.setVisibility(View.VISIBLE);
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
