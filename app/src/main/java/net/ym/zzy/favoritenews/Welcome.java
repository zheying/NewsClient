package net.ym.zzy.favoritenews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;

public class Welcome extends Activity {

	/**
	 * 三个切换的动画
	 */
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;

	ImageView mImageView;

	View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		view = LayoutInflater.from(this).inflate(R.layout.welcome, null);
		setContentView(view);
		mImageView = (ImageView) findViewById(R.id.image);
		int index = new Random().nextInt(2);
		if (index == 1) {
			mImageView.setImageResource(R.drawable.entrance3);
		} else {
			mImageView.setImageResource(R.drawable.entrance2);
		}

		initAnim();
		setListener();
	}


	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
		mFadeIn.setDuration(500);
		mFadeInScale = AnimationUtils.loadAnimation(this,
				R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(2000);
		mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
		mFadeOut.setDuration(500);
		view.startAnimation(mFadeIn);
	}


	/**
	 * 监听事件
	 */
	public void setListener() {
		/**
		 * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
		 * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
		 */
		mFadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				mImageView.startAnimation(mFadeInScale);
			}
		});
		mFadeInScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				redirectTo();
				// mImageView.startAnimation(mFadeOut);
			}
		});
		mFadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				// startActivity(MainActivity.class);
			}
		});
	}

//	private AlphaAnimation start_anima;
//	View view;
//	@Override
//	protected void onCreate(Bundle savedInstanceState){
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		view = View.inflate(this, R.layout.welcome, null);
//		setContentView(view);
//		initView();
//		initData();
//	}
//	private void initData() {
//		start_anima = new AlphaAnimation(0.3f, 1.0f);
//		start_anima.setDuration(2000);
//		view.startAnimation(start_anima);
//		start_anima.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				// TODO Auto-generated method stub
//				redirectTo();
//			}
//		});
//	}
//
//	private void initView() {
//
//	}

	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}
}
