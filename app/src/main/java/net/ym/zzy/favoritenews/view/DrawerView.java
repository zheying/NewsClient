package net.ym.zzy.favoritenews.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

import net.ym.zzy.favoritenews.R;
import net.ym.zzy.favoritenews.SettingsActivity;
import net.ym.zzy.favoritenews.cache.AccessTokenKeeper;
import net.ym.zzy.favoritenews.weibo.auth.AuthorCallback;
import net.ym.zzy.favoritenews.weibo.auth.WBAuthHelper;

/**
 * 自定义SlidingMenu 测拉菜单类
 * */
public class DrawerView implements OnClickListener{
    private final Activity activity;
    SlidingMenu localSlidingMenu;
    private SwitchButton night_mode_btn;
    private TextView night_mode_text;
    private RelativeLayout setting_btn;
    private View login_layout; //登录之后显示的布局
    private View logout_layout; //登录之前显示的布局
    private View weibo_login;  //微博登录按钮

    private ImageView avatar;  //用户头像
    private TextView user_name; //用户昵称

    public DrawerView(Activity activity) {
        this.activity = activity;
    }
    /*
        public SlidingMenu initSlidingMenu() {
            localSlidingMenu = new SlidingMenu(activity);
            localSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);//设置左右滑菜单
            localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
    //		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
            localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
            localSlidingMenu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
            localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
            localSlidingMenu.setFadeDegree(0.35F);//SlidingMenu滑动时的渐变程度
            localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//使SlidingMenu附加在Activity右边
    //		localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
            localSlidingMenu.setMenu(R.layout.left_drawer_fragment);//设置menu的布局文件
    //		localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
            localSlidingMenu.setSecondaryMenu(R.layout.profile_drawer_right);
            localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
            localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                        public void onOpened() {

                        }
                    });
            localSlidingMenu.setOnClosedListener(new OnClosedListener() {

                @Override
                public void onClosed() {
                    // TODO Auto-generated method stub

                }
            });
            initView();
            return localSlidingMenu;
        }
    */
    public SlidingMenu initSlidingMenu() {
        localSlidingMenu = new SlidingMenu(activity);
        localSlidingMenu.setMode(SlidingMenu.LEFT);//设置左右滑菜单
        localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
//		localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
        localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
        localSlidingMenu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
        localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
        localSlidingMenu.setFadeDegree(0.35F);//SlidingMenu滑动时的渐变程度
        localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);//使SlidingMenu附加在Activity右边
//		localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
        localSlidingMenu.setMenu(R.layout.left_drawer_fragment);//设置menu的布局文件
        localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            public void onOpened() {

            }
        });
        localSlidingMenu.setOnClosedListener(new OnClosedListener() {

            @Override
            public void onClosed() {
                // TODO Auto-generated method stub

            }
        });
        initView();
        return localSlidingMenu;
    }

    private void initView() {
//		night_mode_btn = (SwitchButton)localSlidingMenu.findViewById(R.id.night_mode_btn);
//		night_mode_text = (TextView)localSlidingMenu.findViewById(R.id.night_mode_text);
//		night_mode_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
//				}else{
//					night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
//				}
//			}
//		});
//		night_mode_btn.setChecked(false);
//		if(night_mode_btn.isChecked()){
//			night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
//		}else{
//			night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
//		}

        login_layout = localSlidingMenu.findViewById(R.id.login_layout);
        logout_layout = localSlidingMenu.findViewById(R.id.logout_layout);
        weibo_login = localSlidingMenu.findViewById(R.id.weibo_btn);
        avatar = (ImageView)localSlidingMenu.findViewById(R.id.avatar);
        user_name = (TextView)localSlidingMenu.findViewById(R.id.user_name);

        weibo_login.setOnClickListener(this);

        setting_btn =(RelativeLayout)localSlidingMenu.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_btn:
                activity.startActivity(new Intent(activity,SettingsActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.weibo_btn:
                WBAuthHelper.authorizeWBAuthHelper(new AuthorCallback() {
                    @Override
                    public void onComplete() {
                        logout_layout.setVisibility(View.GONE);
                        login_layout.setVisibility(View.VISIBLE);
                        user_name.setText(AccessTokenKeeper.readScreenName(activity));
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onException() {

                    }
                });
                break;
            default:
                break;
        }
    }
}
