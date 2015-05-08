package net.ym.zzy.favoritenews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.view.TagGroup;

/**
 * Created by zengzheying on 15/5/7.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_GeneralDialogTheme);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null, false);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        setContentView(view, new ViewGroup.LayoutParams(view.getMeasuredWidth(), view.getMeasuredHeight()));
    }
}
