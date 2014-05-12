package com.hectorgaticapaz.apps.android.santuariodesanjose;

import java.io.File;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.ActividadesFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.ContactoFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.FotosFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.MapaGoogleFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.MenuFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.SinConexionFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.WebFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.helper.NetworkHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LaMeraMama extends BaseActivity {

	private Fragment mContent;
	
	public LaMeraMama() {
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null) {
			if (NetworkHelper.NetworkStatus(getApplicationContext()))
				mContent = new ActividadesFragment(); 
			else {
				mContent = new SinConexionFragment();
				Bundle args = new Bundle();
				args.putInt("viewId", R.id.txtActividades);
				mContent.setArguments(args);
			}
		}
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment())
		.commit();
		
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
		if (fragment instanceof WebFragment || fragment instanceof MapaGoogleFragment)
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		else
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		if (fragment instanceof FotosFragment || fragment instanceof ContactoFragment) {
//			about.setVisible(true);
//			Log.wtf("about", "setVisible true");
//		} else {
//			about.setVisible(false);
//			Log.wtf("about", "setVisible false");
//		}
		this.supportInvalidateOptionsMenu();
	}
	
	private MenuItem about;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mContent != null && (this.mContent instanceof FotosFragment || this.mContent instanceof ContactoFragment)){
			getSupportMenuInflater().inflate(R.menu.actividad_menu, menu);
			
			about = menu.findItem(R.id.menu_about);
			about.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					share();
					return false;
				}
			});
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	private void share() {
		if (this.mContent instanceof ContactoFragment) {
			String email = "informacion@santuariodesanjose.org.gt";
			String asunto = "";
			String cuerpo = "Enviado desde aplicacion android";
			String mailUrl = "mailto:" + email + "?subject=" + asunto + "&body=" + cuerpo;
			Uri uri2 = Uri.parse(mailUrl);
			startActivity(new Intent(Intent.ACTION_VIEW, uri2));
		} else {
			new BitmapTask().execute();
		}
	}
	
    public void shareImage() {
//		View view = findViewById(R.id.layout);// your layout id
//		view = view.getRootView();
    		View view = getWindow().getDecorView();
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File picDir = new File(Environment.getExternalStorageDirectory()
					+ "/myPic");
			if (!picDir.exists()) {
				picDir.mkdir();
			}
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(true);
			Bitmap bitmap = view.getDrawingCache();
			// Date date = new Date();
			String fileName = "mylove" + ".png";
			File picFile = new File(picDir + "/" + fileName);
			try {
				picFile.createNewFile();
				FileOutputStream picOut = new FileOutputStream(picFile);
//				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//						(int) (bitmap.getHeight() / 1.2));
				boolean saved = bitmap.compress(CompressFormat.PNG, 100,
						picOut);
				if (saved) {
					Log.d("CORRECTO", "Image saved to your device Pictures "
							+ "directory!");
				} else {
					Log.e("ERROR", "no se pudo guardar archivo " + picFile.getAbsolutePath());
				}
				picOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			view.destroyDrawingCache();
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
//			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + picFile.getAbsolutePath()));
			shareIntent.setType("image/png");
			startActivity(Intent.createChooser(shareIntent, "Compartir vía"));

			} else {
			Log.e("ERROR", "pa q ptas");
		}

	}
    
	private class BitmapTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog mDialog ;
		
		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(LaMeraMama.this, ProgressDialog.STYLE_SPINNER);
			mDialog.setMessage("Cargando opciones");
			mDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				shareImage();
			} catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mDialog.dismiss();
		}
		
	}

}
