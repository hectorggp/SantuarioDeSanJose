package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hectorgaticapaz.apps.android.santuariodesanjose.R;

public class WebFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_web, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		WebView mWebView = (WebView) getActivity().findViewById(R.id.webView1);
		// mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

		final Activity activity = getActivity();
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});

		mWebView.setWebViewClient(new MyOwnWebViewClient());

		mWebView.loadUrl(getArguments().getString("Url"));
		mWebView.setWebViewClient(new MyOwnWebViewClient());

	}

	public class MyOwnWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
}
