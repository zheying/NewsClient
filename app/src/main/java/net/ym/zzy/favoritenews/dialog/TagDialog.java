package net.ym.zzy.favoritenews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.view.TagGroup;

import java.util.List;

/**
 * Created by zengzheying on 15/4/27.
 */
public class TagDialog extends Dialog {

    TagGroup mTagGroup;
    Button mOkButton;

    int width;

    public TagDialog(Context context) {
        super(context, R.style.Theme_GeneralDialogTheme);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels / 4 * 3;

    }

    public void addTags(List<String> tags){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_tags, null, false);
        mTagGroup = (TagGroup)view.findViewById(R.id.tags);
        mOkButton = (Button)view.findViewById(R.id.commit);
        mTagGroup.setTags(tags);

        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        setContentView(view, new ViewGroup.LayoutParams(view.getMeasuredWidth(), view.getMeasuredHeight()));
    }

    public void addButtonClickListener(View.OnClickListener clickListener){
        mOkButton.setOnClickListener(clickListener);
    }

    public List<String> getTags(){
        return mTagGroup.getCheckedTags();
    }
}
