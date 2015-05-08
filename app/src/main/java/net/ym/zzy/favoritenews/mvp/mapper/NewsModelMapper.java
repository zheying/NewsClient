package net.ym.zzy.favoritenews.mvp.mapper;

import android.util.Log;

import net.ym.zzy.domain.entity.News;
import net.ym.zzy.favoritenews.mvp.model.NewsListModel;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zengzheying on 15/4/15.
 */
public class NewsModelMapper {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static NewsModel transform(News news){
        if (news != null){
            NewsModel newsModel = new NewsModel();
            newsModel.setCollectStatus(news.getCollectStatus());
            newsModel.setId(news.getId());
            newsModel.setInterestedStatus(news.getInterestedStatus());
            newsModel.setIsLarge(news.getIsLarge());
            newsModel.setLikeStatus(news.getLikeStatus());
            newsModel.setNewsCategory(news.getNewsCategory());
            newsModel.setPicList(news.getPicList());
            newsModel.setPicListString(news.getPicListString());
            newsModel.setSource(news.getSource());
            newsModel.setSource_url(news.getSource_url());
            newsModel.setTitle(news.getTitle());
            newsModel.setReadStatus(news.getReadStatus());
            newsModel.setCommentCount(news.getCommentCount());
            newsModel.setTags(news.getTags());
            newsModel.setRecommend(news.isRecommend());
            try {
                Log.d("NewsModel", news.getPublishTime());
                Date date = sdf.parse(news.getPublishTime());
                newsModel.setPublishTime(date.getTime() / 1000);
            }catch (Exception ex){

            }
            return newsModel;
        }
        return null;
    }

    public static NewsListModel transform(List<News> newsList){
        if (newsList != null){
            ArrayList<NewsModel> newsModels = new ArrayList<>();
            for (News news : newsList){
                if (news.getPublishTime() == null || "".equals(news.getPublishTime().trim())){
                    continue;
                }
                newsModels.add(transform(news));
            }
            NewsListModel newsListModel = new NewsListModel();
            newsListModel.setNewsModelList(newsModels);
            return newsListModel;
        }
        return null;
    }
}
