package net.ym.zzy.favoritenews.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengzheying on 15/3/28.
 */
public class NewsModel implements Serializable, Model{

    /** 新闻类型 */
    private String newsCategory;

    /** ID */
    private int id;

    /** 标题 */
    private String title;

    /** 新闻源 */
    private String source;

    /** 新闻源地址 URL */
    private String source_url;

    /** 发布时间 */
    private long publishTime;

    /** 图片列表字符串 */
    private String picListString;

    /** 图片 列表 */
    private List<String> picList;

    private ArrayList<String> tags;

    private boolean recommend;

    /** 图片类型是否为大图 */
    private boolean isLarge;
    /** 阅读状态 ，读过的话显示灰色背景 */
    private boolean readStatus;
    /** 收藏状态 */
    private boolean collectStatus;
    /** 喜欢 状态 */
    private boolean likeStatus;
    /** 感兴趣状态 */
    private boolean interestedStatus;

    private int commentCount;

    @Override
    public String toString() {
        return "id:     " + getId() + "\n" +
               "title:  " + getTitle() + "\n" +
               "source: " + getSource() + "\n" +
               "cat:    " + getNewsCategory() + "\n" +
               "pageurl:" + getSource_url() + "\n"  +
               "date:   " + getPublishTime() + "\n" +
               "picurl: " + getPicListString();

    }


    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }


    public String getPicListString() {
        return picListString;
    }

    public void setPicListString(String picListString) {
        this.picListString = picListString;
    }

    public String getPicOne() {
        if (picList != null && picList.size() > 0) {
            return picList.get(0);
        }else{
            return null;
        }
    }

    public String getPicTwo() {
        if (picList != null && picList.size() > 1) {
            return picList.get(1);
        }else{
            return null;
        }
    }

    public String getPicThr() {
        if (picList != null && picList.size() > 2) {
            return picList.get(2);
        }else{
            return null;
        }
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(boolean collectStatus) {
        this.collectStatus = collectStatus;
    }

    public boolean getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public boolean getInterestedStatus() {
        return interestedStatus;
    }

    public void setInterestedStatus(boolean interestedStatus) {
        this.interestedStatus = interestedStatus;
    }

    public boolean getIsLarge() {
        return isLarge;
    }

    public void setIsLarge(boolean isLarge) {
        this.isLarge = isLarge;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NewsModel))
            return false;
        if (o == this)
            return true;
        return ((NewsModel)o).getId() == getId();
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(String tags) {
        if (tags != null && !tags.trim().equals("") && !tags.trim().equals("null tag")) {
            this.tags = new ArrayList<>();
            String[] array = tags.replace("|", " ").split(" ");
            for (int i = 0; i < array.length && i < 10; i++){
                if (!array[i].trim().equals("")){
                    this.tags.add(array[i]);
                }
            }
        }
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
