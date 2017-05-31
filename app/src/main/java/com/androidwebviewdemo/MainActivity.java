package com.androidwebviewdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * 使用WebView播放视频时需要注意的地方：
 * 1、加网络访问权限（及其他所需要的权限）；
 * 2、WebViewClient中方法shouldOverrideUrlLoading可用来实现点击webView页面的链接；
 * 3、WebView中播放视频需要添加webView.setWebChromeClient(new WebChromeClient())；
 * 4、视频竖屏时，点击全屏，想要切换到横屏全屏的状态，那么必须在Manifest.xml配置文件该Activity的
 *  配置文件中添加android:configChanges="orientation|screenSize"语句。
 * 5、如果视频不能播放，或者播放比较卡，可以采用硬件加速，即在Application，或所在的Activity的配置文件中添加
 *  android:hardwareAccelerated="true"即可。
 *
 */
public class MainActivity extends Activity {
	private WebView webView;
	private FrameLayout video_fullView;// 全屏时视频加载view
	private View xCustomView;
	private CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.webView);
		video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
		Button goVideoView= (Button) findViewById(R.id.go_video_view);
		goVideoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(MainActivity.this,VideoViewActivity.class);
				startActivity(intent);
			}
		});


		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
//		 ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);//webview使用本地存储
		ws.setSupportMultipleWindows(true);// 新加
		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		webView.setWebViewClient(new myWebViewClient());
//		webView.loadUrl("http://look.appjx.cn/mobile_api.php?mod=news&id=12604");
		webView.loadUrl("http://i0.hqyxjy.com/files/1.%E6%95%99%E5%B8%88%E5%A6%82%E4%BD%95%E4%B8%8B%E8%BD%BD%E3%80%81%E6%B3%A8%E5%86%8C%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B9%B6%E5%AE%8C%E6%88%90%E5%BC%80%E8%AF%BE%E8%AE%BE%E7%BD%AE.mp4");
	}

	public class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

	public class myWebChromeClient extends WebChromeClient {
		private Bitmap xdefaltvideo;

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			webView.setVisibility(View.INVISIBLE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			video_fullView.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.setVisibility(View.VISIBLE);
		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			webView.setVisibility(View.VISIBLE);
		}

//		//视频加载添加默认图标
//
//		@Override
//
//		public Bitmap getDefaultVideoPoster() {
//
//			//Log.i(LOGTAG, "here in on getDefaultVideoPoster");
//
//			if (xdefaltvideo == null) {
//
//				xdefaltvideo = BitmapFactory.decodeResource(
//
//						getResources(), R.drawable.ic_launcher);
//
//			}
//
//			return xdefaltvideo;
//
//		}
	}

	/**
	 * 判断是否是全屏
	 *
	 * @return
	 */
	public boolean inCustomView() {
		return (xCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView() {
		xwebchromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		super.onResume();
		webView.onResume();
		webView.resumeTimers();

		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
		webView.pauseTimers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		super.onDestroy();
		video_fullView.removeAllViews();
		webView.loadUrl("about:blank");
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.destroy();
		webView = null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (inCustomView()) {
				// webViewDetails.loadUrl("about:blank");
				hideCustomView();
				return true;
			} else {
				webView.loadUrl("about:blank");
				MainActivity.this.finish();
			}
		}
		return false;
	}
}
