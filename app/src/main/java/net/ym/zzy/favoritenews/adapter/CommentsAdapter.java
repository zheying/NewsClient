package net.ym.zzy.favoritenews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.mvp.model.CommentModel;
import net.ym.zzy.favoritenews.tool.DateTools;
import net.ym.zzy.favoritenews.tool.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengzheying on 15/4/24.
 */
public class CommentsAdapter extends BaseAdapter {

    List<CommentModel> mCommentModels;
    Context mContext;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public CommentsAdapter(Context context) {
        mContext = context;
        mCommentModels = new ArrayList<>();
        options = Options.getListOptions();
    }

    public void addData(List<CommentModel> commentModels){
        if (commentModels != null){
            for (CommentModel commentModel : commentModels){
                if (!mCommentModels.contains(commentModel)){
                    mCommentModels.add(commentModel);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mCommentModels.size();
    }

    @Override
    public CommentModel getItem(int position) {
        return mCommentModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView)view.findViewById(R.id.avatar);
            viewHolder.user_name = (TextView)view.findViewById(R.id.user_name);
            viewHolder.comment_content = (TextView)view.findViewById(R.id.comment_content);
            viewHolder.comment_time = (TextView)view.findViewById(R.id.comment_time);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        CommentModel commentModel = getItem(position);
        viewHolder.user_name.setText(commentModel.getUserName());
        viewHolder.comment_content.setText(commentModel.getCommentContent());
        viewHolder.comment_time.setText(DateTools.getCommentTimeString(commentModel.getCommentTime()));
        imageLoader.displayImage(commentModel.getAvatarUrl(), viewHolder.avatar, options);
        return view;
    }

    class ViewHolder{
        ImageView avatar;
        TextView user_name;
        TextView comment_content;
        TextView comment_time;
    }
}
