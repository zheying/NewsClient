package net.ym.zzy.favoritenews.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.mvp.model.NewsModel;
import net.ym.zzy.favoritenews.tool.Constants;
import net.ym.zzy.favoritenews.tool.DateTools;
import net.ym.zzy.favoritenews.tool.Options;
import net.ym.zzy.favoritenews.view.HeadListView;

public class NewsAdapter extends BaseAdapter implements SectionIndexer, HeadListView.HeaderAdapter, OnScrollListener{

    private final int ANIMATION_DURATION = 350;

	List<NewsModel> newsList;
	Activity activity;
	LayoutInflater inflater = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	/** 弹出的更多选择框  */
	private PopupWindow popupWindow;
	public NewsAdapter(Activity activity, List<NewsModel> newsList) {
		this.activity = activity;
		this.newsList = newsList;
		inflater = LayoutInflater.from(activity);
		options = Options.getListOptions();
		initPopWindow();
		initDateHead();
	}

    public NewsAdapter(Activity activity) {
        this.activity = activity;
        this.newsList = new ArrayList<NewsModel>();
        inflater = LayoutInflater.from(activity);
        options = Options.getListOptions();
        initPopWindow();
        initDateHead();
    }
	
	private List<Integer> mPositions;
	private List<String> mSections;

    public int addData(List<NewsModel> newsList){
		int count = 0;
        boolean dataSetChanged = false;
        if (newsList != null && newsList.size() > 0){
            for (int i = 0; i <= newsList.size() - 1; i++){
                NewsModel news = newsList.get(i);
                if (!this.newsList.contains(news)){
                    this.newsList.add(news);
                    dataSetChanged = true;
					count++;
                }
            }
        }
        if (dataSetChanged){
            initDateHead();
        }
		return count;
    }

	public void clearData(){
		if (newsList != null){
			newsList.clear();
			notifyDataSetChanged();
		}
	}
	
	private void initDateHead() {
		mSections = new ArrayList<String>();
		mPositions= new ArrayList<Integer>();
		for(int i = 0; i <newsList.size();i++){
			if(i == 0){
				mSections.add(DateTools.getSection(String.valueOf(newsList.get(i).getPublishTime())));
				mPositions.add(i);
				continue;
			}
			if(i != newsList.size()){
//				if(!DateTools.getPublishTimeString(newsList.get(i).getPublishTime()).trim().equals(
//						DateTools.getPublishTimeString(newsList.get(i - 1).getPublishTime()).trim())){
				if (!DateTools.isSameDay(newsList.get(i).getPublishTime(), newsList.get(i).getPublishTime())){
					Log.d("TestString", DateTools.getSection(String.valueOf(newsList.get(i).getPublishTime())) +
					"==" + DateTools.getSection(String.valueOf(newsList.get(i).getPublishTime())) + "?" +
							DateTools.getPublishTimeString(newsList.get(i).getPublishTime()).trim().equals(
									DateTools.getPublishTimeString(newsList.get(i - 1).getPublishTime()).trim()));
					mSections.add(DateTools.getSection(String.valueOf(newsList.get(i).getPublishTime())));
					mPositions.add(i);
				}
			}
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
		mHolder.popicon.setOnClickListener(new popAction(position, view));
		//头部的相关东西
		int section = getSectionForPosition(position);
		if (getPositionForSection(section) == position) {
			mHolder.layout_list_section.setVisibility(View.VISIBLE);
			mHolder.section_text.setText(mSections.get(section));
		} else {
			mHolder.layout_list_section.setVisibility(View.GONE);
		}
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
	/** 根据属性获取对应的资源ID  */
	public int getAltMarkResID(int mark,boolean isfavor){
		if(isfavor){
			return R.drawable.ic_mark_favor;
		}
		switch (mark) {
		case Constants.mark_recom:
			return R.drawable.ic_mark_recommend;
		case Constants.mark_hot:
			return R.drawable.ic_mark_hot;
		case Constants.mark_frist:
			return R.drawable.ic_mark_first;
		case Constants.mark_exclusive:
			return R.drawable.ic_mark_exclusive;
		case Constants.mark_favor:
			return R.drawable.ic_mark_favor;
		default:
			break;
		}
		return -1;
	}
	
	/** popWindow 关闭按钮 */
	private ImageView btn_pop_close;
    private View btn_pop_dislike;
	private View btn_pop_favor;
	private TextView btn_pop_favor_tv;
	
	/**
	 * 初始化弹出的pop
	 * */
	private void initPopWindow() {
		View popView = inflater.inflate(R.layout.list_item_pop, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		//设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		btn_pop_close = (ImageView) popView.findViewById(R.id.btn_pop_close);
        btn_pop_dislike = popView.findViewById(R.id.ll_pop_dislike);
		btn_pop_favor = popView.findViewById(R.id.ll_pop_favor);
		btn_pop_favor_tv = (TextView)popView.findViewById(R.id.tv_pop_favor);
	}
	
	/** 
	 * 显示popWindow
	 * */
	public void showPop(final View parent, int x, int y, final int postion) {

		if (getItem(postion).getCollectStatus()){
			btn_pop_favor_tv.setText(R.string.pop_collect_cancel);
		}else {
			btn_pop_favor_tv.setText(R.string.pop_collect);
		}

		//设置popwindow显示位置
		popupWindow.showAtLocation(parent, 0, x, y);
		//获取popwindow焦点
		popupWindow.setFocusable(true);
		//设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		if (popupWindow.isShowing()) {
			
		}
		btn_pop_close.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				popupWindow.dismiss();
			}
		});

        btn_pop_dislike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				if (mPopupClickListener != null) {
					mPopupClickListener.onDislikeClick(getItem(postion).getId());
				}
                popupWindow.dismiss();
                deleteCell(parent, postion);
            }
        });

		btn_pop_favor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopupClickListener != null){
					mPopupClickListener.onCollectClick(getItem(postion));
				}
				popupWindow.dismiss();
			}
		});
	}
	
	/** 
	 * 每个ITEM中more按钮对应的点击动作
	 * */
	public class popAction implements OnClickListener{
		int position;
        View cell;
		public popAction(int position, View cell){
			this.position = position;
            this.cell = cell;
		}
		@Override
		public void onClick(View v) {
			int[] arrayOfInt = new int[2];
			//获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
	        int x = arrayOfInt[0];
	        int y = arrayOfInt[1];
	        showPop(cell, x , y, position);
		}
	}
	
	/* 是不是城市频道，  true：是   false :不是*/
	public boolean isCityChannel = false;
	
	/* 是不是第一个ITEM，  true：是   false :不是*/
	public boolean isfirst = true;

	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	//滑动监听
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof HeadListView) {
			Log.d("first", "first:" + view.getFirstVisiblePosition());
			if(isCityChannel){
				if(view.getFirstVisiblePosition() == 0){
					isfirst = true;
				}else{
					isfirst = false;
				}
				((HeadListView) view).configureHeaderView(firstVisibleItem - 1);
			}else{
				((HeadListView) view).configureHeaderView(firstVisibleItem);
			}
		}
	}
	
	@Override
	public int getHeaderState(int position) {
		// TODO Auto-generated method stub
		int realPosition = position;
		if(isCityChannel){
			if(isfirst){
				return HEADER_GONE;
			}
		}
		if (realPosition < 0 || position >= getCount()) {
			return HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return HEADER_PUSHED_UP;
		}
		return HEADER_VISIBLE;
	}

	@Override
	public void configureHeader(View header, int position, int alpha) {
        if (getCount() > 0) {
            int realPosition = position;
            int section = getSectionForPosition(realPosition);
            String title = (String) getSections()[section];
            ((TextView) header.findViewById(R.id.section_text)).setText(title);
//            ((TextView) header.findViewById(R.id.section_day)).setText("今天");
        }
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mSections.toArray();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if (sectionIndex < 0 || sectionIndex >= mPositions.size()) {
			return -1;
		}
		return mPositions.get(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

    private void collapse(final View v, Animation.AnimationListener animationListener){
        final int initialHeight = v.getHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (animationListener != null){
            anim.setAnimationListener(animationListener);
        }

        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }

    private void deleteCell(final View v, final int index){
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                newsList.remove(index);

                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;

                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        collapse(v, animationListener);
    }

	private PopupClickListener mPopupClickListener;

	public void setPopupClickListener(PopupClickListener popupClickListener) {
		mPopupClickListener = popupClickListener;
	}

	public interface PopupClickListener{
		void onCollectClick(NewsModel newsModel);
		void onDislikeClick(int news_id);
	}
}
