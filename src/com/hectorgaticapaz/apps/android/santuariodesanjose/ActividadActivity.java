package com.hectorgaticapaz.apps.android.santuariodesanjose;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.hectorgaticapaz.apps.android.santuariodesanjose.fragments.FotosFragment;
import com.hectorgaticapaz.apps.android.santuariodesanjose.objects.Actividad;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
 
public class ActividadActivity extends SherlockActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		final ProgressDialog mDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("Cargando Evento");
		mDialog.show();
		
		String objectId = getIntent().getStringExtra("objectId");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Actividad");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject actividad, ParseException arg1) {
				mDialog.dismiss();
				if (arg1 == null) {
					
					String banner = actividad.getParseFile("BannerActividad").getUrl();
					String detalle = actividad.getString("Detalle");
					Date date = actividad.getDate("InicioDate");
					String nombre = actividad.getString("NombreActividad");
					int prioridad = actividad.getInt("Prioridad");
					String contenedor = actividad.getString("contenedor"); 
					String objectId = actividad.getString("objectId"); 
					Actividad a = new Actividad(banner, detalle, date, nombre, prioridad, contenedor, objectId);
					float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
					float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
					FotosFragment.imageDownloader.forceDownload(banner, 
							(ImageView) findViewById(R.id.banner), (int) w, (int) h);
//					((ImageView) findViewById(R.id.banner)).setImageBitmap(a.banner);
					((TextView) findViewById(R.id.textView1)).setText(a.contenedor);
					((TextView) findViewById(R.id.textView2)).setText(a.nombre);
					((TextView) findViewById(R.id.textView3)).setText(a.detalle);
					getSupportActionBar().setTitle(nombre);
				} else {
					Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
					arg1.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.actividad_menu, menu);

        final MenuItem about= menu.findItem(R.id.menu_about);
        about.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
            	new BitmapTask().execute();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
/*			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("image/png");
			sharingIntent.putExtra(Intent.EXTRA_STREAM,
					Uri.parse(picFile.getAbsolutePath()));
			sharingIntent.setData(Uri.fromFile(picFile));
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
*/		
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
			mDialog = new ProgressDialog(ActividadActivity.this, ProgressDialog.STYLE_SPINNER);
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
