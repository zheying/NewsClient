package net.ym.zzy.favoritenews.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.tool.DateTools;
import net.ym.zzy.favoritenews.tool.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public class CollectedNewsAdapter extends BaseAdapter{

    private final int ANIMATION_DURATION = 350;

    List<NewsModel> newsList;
    Activity activity;
    LayoutInflater inflater = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    /** 弹出的更多选择框  */
    private PopupWindow popupWindow;
    public CollectedNewsAdapter(Activity activity, List<NewsModel> newsList) {
        this.activity = activity;
        this.newsList = newsList;
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
    }

    public CollectedNewsAdapter(Activity activity) {
        this.activity = activity;
        this.newsList = new ArrayList<>();
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
    }


    public int addData(List<NewsModel> newsList){
        int count = 0;
        if (newsList != null && newsList.size() > 0){
            for (int i = newsList.size() - 1; i >= 0; i--){
                NewsModel news = newsList.get(i);
                if (!this.newsList.contains(news)){
                    this.newsList.add(0, news);
                    count++;
                }
            }
        }

        return count;
    }

    public void clearData(){
        if (newsList != null){
            newsList.clear();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public NewsModel getItem(int position) {
        // TODO Auto-generated method stub
        if (newsList != null && newsList.size() != 0) {
            return newsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder;
        View view = convertView;
        if (view == null || ((ViewHolder)view.getTag()).needInflate) {
            view = inflater.inflate(R.layout.list_item, null);
            mHolder = new ViewHolder();
            mHolder.item_layout = (LinearLayout)view.findViewById(R.id.item_layout);
//			mHolder.comment_layout = (RelativeLayout)view.findViewById(R.id.comment_layout);
            mHolder.item_title = (TextView)view.findViewById(R.id.item_title);
            mHolder.item_source = (TextView)view.findViewById(R.id.item_source);
            mHolder.list_item_local = (TextView)view.findViewById(R.id.list_item_local);
//			mHolder.comment_count = (TextView)view.findViewById(R.id.comment_count);
            mHolder.publish_time = (TextView)view.findViewById(R.id.publish_time);
//			mHolder.item_abstract = (TextView)view.findViewById(R.id.item_abstract);
            mHolder.alt_mark = (ImageView)view.findViewById(R.id.alt_mark);
            mHolder.right_image = (ImageView)view.findViewById(R.id.right_image);
            mHolder.item_image_layout = (LinearLayout)view.findViewById(R.id.item_image_layout);
            mHolder.item_image_0 = (ImageView)view.findViewById(R.id.item_image_0);
            mHolder.item_image_1 = (ImageView)view.findViewById(R.id.item_image_1);
            mHolder.item_image_2 = (ImageView)view.findViewById(R.id.item_image_2);
            mHolder.large_image = (ImageView)view.findViewById(R.id.large_image);
            mHolder.popicon = (ImageView)view.findViewById(R.id.popicon);
//			mHolder.comment_content = (TextView)view.findViewById(R.id.comment_content);
            mHolder.right_padding_view = (View)view.findViewById(R.id.right_padding_view);
            //头部的日期部分
            mHolder.layout_list_section = (LinearLayout)view.findViewById(R.id.layout_list_section);
            mHolder.layout_list_section.setVisibility(View.GONE);
            mHolder.section_text = (TextView)view.findViewById(R.id.section_text);
            mHolder.section_day = (TextView)view.findViewById(R.id.section_day);
            mHolder.needInflate = false;

            view.setTag(mHolder);
        } else{
            mHolder = (ViewHolder) view.getTag();
        }
        //获取position对应的数据
        NewsModel news = getItem(position);
        Log.d(activity.getPackageName(), news.toString());
        mHolder.item_title.setText(news.getTitle());
        mHolder.item_source.setText(news.getSource());
//		mHolder.comment_count.setText("评论" + news.getCommentNum());
        mHolder.publish_time.setText(DateTools.getPublishTimeString(news.getPublishTime()));
        List<String> imgUrlList = news.getPicList();
        mHolder.popicon.setVisibility(View.VISIBLE);
//		mHolder.comment_count.setVisibility(View.VISIBLE);
        mHolder.right_padding_view.setVisibility(View.VISIBLE);
        if(imgUrlList !=null && imgUrlList.size() !=0){
            if(imgUrlList.size() < 3){
                mHolder.item_image_layout.setVisibility(View.GONE);
                //是否是大图
                if(news.getIsLarge()){
                    mHolder.large_image.setVisibility(View.VISIBLE);
                    mHolder.right_image.setVisibility(View.GONE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.large_image, options);
                    mHolder.popicon.setVisibility(View.GONE);
//					mHolder.comment_count.setVisibility(View.GONE);
                    mHolder.right_padding_view.setVisibility(View.GONE);
                }else{
                    mHolder.large_image.setVisibility(View.GONE);
                    mHolder.right_image.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, options);
                }
            }else{
                mHolder.large_image.setVisibility(View.GONE);
                mHolder.right_image.setVisibility(View.GONE);
                mHolder.item_image_layout.setVisibility(View.VISIBLE);
                imageLoader.displayImage(imgUrlList.get(0), mHolder.item_image_0, options);
                imageLoader.displayImage(imgUrlList.get(1), mHolder.item_image_1, options);
                imageLoader.displayImage(imgUrlList.get(2), mHolder.item_image_2, options);
            }
        }else{
            mHolder.right_image.setVisibility(View.GONE);
            mHolder.large_image.setVisibility(View.GONE);
            mHolder.item_image_layout.setVisibility(View.GONE);
        }

        mHolder.list_item_local.setVisibility(View.GONE);

        //判断该新闻是否已读
        if(!news.getReadStatus()){
            mHolder.item_layout.setSelected(true);
        }else{
            mHolder.item_layout.setSelected(false);
        }
        //设置+按钮点击效果
        mHolder.popicon.setVisibility(View.INVISIBLE);
        //头部的相关东西
        return view;
    }

    static class ViewHolder {
        LinearLayout item_layout;
        //title
        TextView item_title;
        //图片源
        TextView item_source;
        //类似推广之类的标签
        TextView list_item_local;
        //评论数量
//		TextView comment_count;
        //发布时间
        TextView publish_time;
        //新闻摘要
//		TextView item_abstract;
        //右上方TAG标记图片
        ImageView alt_mark;
        //右边图片
        ImageView right_image;
        //3张图片布局
        LinearLayout item_image_layout; //3张图片时候的布局
        ImageView item_image_0;
        ImageView item_image_1;
        ImageView item_image_2;
        //大图的图片的话布局
        ImageView large_image;
        //pop按钮
        ImageView popicon;
        //评论布局
//		RelativeLayout comment_layout;
//		TextView comment_content;
        //paddingview
        View right_padding_view;

        //头部的日期部分
        LinearLayout layout_list_section;
        TextView section_text;
        TextView section_day;

        boolean needInflate = true;
    }


}
