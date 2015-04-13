package net.ym.zzy.favoritenews.tool;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;

import net.ym.zzy.favoritenews.R;


/**
 * Created by sam on 14-10-30.
 */
public class UIUtils {
    public static void setSystemBarTintColor(Activity activity){
        if(SystemBarTintManager.isKitKat()){
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintDrawable(new ColorDrawable(activity.getResources().getColor(R.color.material_700)));
        }
    }
}
