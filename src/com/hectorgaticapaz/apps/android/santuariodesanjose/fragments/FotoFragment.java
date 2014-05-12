package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hectorgaticapaz.apps.android.santuariodesanjose.R;

public class FotoFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_photo_view, null);
		String objectId = getArguments().getString("objectId");
		ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		FotosFragment.imageDownloader.download(objectId, imageView,  
				width, height);
		return mView;
	}

 }
