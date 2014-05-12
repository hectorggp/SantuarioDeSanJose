package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MapaGoogleFragment extends SupportMapFragment {
	private LatLng mPosFija = new LatLng(14.640977,-90.510043);

	public MapaGoogleFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);
		initMap();
		return v;
	}

	private void initMap() {
		UiSettings settings = getMap().getUiSettings();
		settings.setAllGesturesEnabled(true);
		getMap().setMyLocationEnabled(true);
		settings.setCompassEnabled(true);

		getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mPosFija, 15));
//		getMap().addMarker(new MarkerOptions().position(mPosFija));
		MarkerOptions marker = new MarkerOptions().position(mPosFija);
        getMap().addPolyline(new PolylineOptions().width(5).color(0xFFFF0000)
        			.add(new LatLng(14.642777,-90.505643))  
                .add(new LatLng(14.642174,-90.504632))  
                .add(new LatLng(14.643445,-90.503962))  
                .add(new LatLng(14.644403,-90.503392))  
                .add(new LatLng(14.647299,-90.501770))  
                .add(new LatLng(14.647255,-90.501050))  
                .add(new LatLng(14.644127,-90.502665))  
                .add(new LatLng(14.639966,-90.505058))  
                .add(new LatLng(14.640255,-90.507130))  
                .add(new LatLng(14.633819,-90.507963))  
                .add(new LatLng(14.634638,-90.513488))  
                .add(new LatLng(14.637155,-90.513162))  
                .add(new LatLng(14.636446,-90.508880))  
                .add(new LatLng(14.640475,-90.508299))  
                .add(new LatLng(14.640984,-90.511674))  
                .add(new LatLng(14.640097,-90.511835))  
                .add(new LatLng(14.640227,-90.512704))  
                .add(new LatLng(14.640097,-90.511841))  
                .add(new LatLng(14.640237,-90.512688))  
                .add(new LatLng(14.641123,-90.512637))  
                .add(new LatLng(14.641541,-90.512709))  
                .add(new LatLng(14.642376,-90.512538))  
                .add(new LatLng(14.642686,-90.514749))  
                .add(new LatLng(14.648436,-90.513965))  
                .add(new LatLng(14.648562,-90.514854))  
                .add(new LatLng(14.643638,-90.515521))  
                .add(new LatLng(14.643879,-90.516764))  
                .add(new LatLng(14.632864,-90.518318))  
                .add(new LatLng(14.633265,-90.520951))  
                .add(new LatLng(14.638182,-90.520172))  
                .add(new LatLng(14.643423,-90.519397))  
                .add(new LatLng(14.649106,-90.518553))  
                .add(new LatLng(14.648905,-90.517288))  
                .add(new LatLng(14.646526,-90.517688))  
                .add(new LatLng(14.646331,-90.516390))  
                .add(new LatLng(14.647381,-90.516283))  
                .add(new LatLng(14.646412,-90.510078))  
                .add(new LatLng(14.647416,-90.509947))  
                .add(new LatLng(14.647110,-90.507430))  
                .add(new LatLng(14.642484,-90.508048))  
                .add(new LatLng(14.642320,-90.506774))  
                .add(new LatLng(14.644410,-90.506520))  
                .add(new LatLng(14.644105,-90.505009))  
                .add(new LatLng(14.643853,-90.504468))  
                .add(new LatLng(14.642546,-90.505996)));
        
		mDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("Cargando Ubicacion");
		mDialog.show();

        hilo = new ActualizarUbicacion();
        hilo.execute();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (hilo != null && !hilo.isCancelled())
			hilo.cancel(true);
	};
	
	ActualizarUbicacion hilo;
	ProgressDialog mDialog;
	
	private class ActualizarUbicacion extends AsyncTask<Void, ParseGeoPoint, Void> {

		private Marker mMarker;
		
		@Override
		protected Void doInBackground(Void... params) {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("turno");
			while (!isCancelled()) {
				try {
					ParseObject turno = query.get("4LZIGhTinc");
//					Log.e("MapaGoogleFragment", turno.getParseGeoPoint("posicion").getLatitude() + " - " + turno.getParseGeoPoint("posicion").getLongitude());
					publishProgress(turno.getParseGeoPoint("posicion"));
					Thread.sleep(5000);
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.e("MapaGoogleFragment", "cancelado");
			return null;
		}
		
		@Override
		protected void onProgressUpdate(ParseGeoPoint... values) {
			if (MapaGoogleFragment.this.mDialog.isShowing())
				MapaGoogleFragment.this.mDialog.dismiss();
			ParseGeoPoint point = values[0];
			if (mMarker != null)
				mMarker.remove();
			mMarker = getMap().addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())));
		}
		
	}

}
