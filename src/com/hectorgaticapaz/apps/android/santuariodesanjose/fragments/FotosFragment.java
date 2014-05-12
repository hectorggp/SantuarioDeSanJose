package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hectorgaticapaz.apps.android.santuariodesanjose.LaMeraMama;
import com.hectorgaticapaz.apps.android.santuariodesanjose.R;
import com.hectorgaticapaz.apps.android.santuariodesanjose.helper.ImageDownloader;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FotosFragment extends Fragment {

	public static ImageDownloader imageDownloader = new ImageDownloader();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("HOLA", "onCreate llamado");
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_photos_pager, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((ViewPager) getActivity().findViewById(R.id.pager)).setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					((LaMeraMama) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					((LaMeraMama) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			((ViewPager) getActivity().findViewById(R.id.pager)).setPageTransformer(true, new ScaleFadePageTransformer());
		
		final ProgressDialog mDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("Cargando Fotos");
		mDialog.show();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("FOTOS");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					((ViewPager) getActivity().findViewById(R.id.pager)).
						setAdapter(new CustomPageAdapter(getChildFragmentManager(), arg0));
					mDialog.dismiss();
				} else {
		        	mDialog.dismiss();
		        	arg1.printStackTrace();
		        	Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_LONG).show();
				}
					
			}
		});
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//	    inflater.inflate(R.menu.actividad_menu, menu);
//	    super.onCreateOptionsMenu(menu,inflater);
//	}

//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		inflater.inflate(R.menu.actividad_menu, menu);
//		final MenuItem about = menu.findItem(R.id.menu_about);
//		about.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				Toast.makeText(getActivity(), " le dio click " , Toast.LENGTH_LONG).show();
//				return false;
//			}
//		});
//		super.onCreateOptionsMenu(menu, inflater);
//	}
	
	private class CustomPageAdapter extends FragmentStatePagerAdapter {

		private List<ParseObject> list;
		
		public CustomPageAdapter(FragmentManager fm, List<ParseObject> list) {
			super(fm);
			this.list = list;
		}
		@Override
		public Fragment getItem(int arg0) {
			FotoFragment f = new FotoFragment();
			Bundle args = new Bundle();
			args.putString("objectId", list.get(arg0).getParseFile("foto").getUrl());
			f.setArguments(args);
			return f;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		
	}

	private class CustomPageAdapter1 extends FragmentStatePagerAdapter {

		private List<ParseObject> list;
		public CustomPageAdapter1(FragmentManager fm, List<ParseObject> list) {
			super(fm);
			this.list = list;
			
			for (ParseObject object : list) {
				String fileName = object.getObjectId();
				try {
					FileOutputStream fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
					fos.write(object.getParseFile("foto").getData());
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public Fragment getItem(int arg0) {
			FotoFragment f = new FotoFragment();
			Bundle args = new Bundle();
			args.putString("objectId", list.get(arg0).getObjectId());
			try {
				args.putInt("bytesSize", list.get(arg0).getParseFile("foto").getData().length);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			f.setArguments(args);
			return f;
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	public class ScaleFadePageTransformer implements PageTransformer {
	    private static final float MIN_SCALE = 0.85f;
	    private static final float MIN_ALPHA = 0.5f;

	    @SuppressLint("NewApi")
		public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 1) { // [-1,1]
	            // Modify the default slide transition to shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }

	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA +
	                    (scaleFactor - MIN_SCALE) /
	                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
}
}
