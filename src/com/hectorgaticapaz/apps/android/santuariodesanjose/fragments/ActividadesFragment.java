package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hectorgaticapaz.apps.android.santuariodesanjose.ActividadActivity;
import com.hectorgaticapaz.apps.android.santuariodesanjose.R;
import com.hectorgaticapaz.apps.android.santuariodesanjose.objects.Actividad;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ActividadesFragment extends ListFragment {

//	private static final String TAG = "ListFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.lista_actividades, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ActividadesListAdapter adapter = new ActividadesListAdapter(getActivity());
		
		final ProgressDialog mDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("Cargando Actividades");
		mDialog.show();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Actividad");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> actividadList, ParseException e) {
		        if (e == null) {
			        	for (ParseObject actividad : actividadList) {
			        		ParseFile banner = actividad.getParseFile("BannerActividad");
			        		String detalle = actividad.getString("Detalle");
			        		Date date = actividad.getDate("InicioDate");
			        		String nombre = actividad.getString("NombreActividad");
			        		int prioridad = actividad.getInt("Prioridad");
			        		String contenedor = actividad.getString("contenedor");
			        		String objectId = actividad.getObjectId();
		        			String bitmap = banner.getUrl();
			        		Actividad aux = new Actividad(bitmap,
			        				detalle, date, nombre, prioridad, contenedor, objectId);
			        		adapter.add(aux);
			        	} 
			        	mDialog.dismiss();
		        } else {
		        	mDialog.dismiss();
		        	e.printStackTrace();
		        	Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_LONG).show();
		        }
		    }
		});
		
		setListAdapter(adapter);
	}

	private class ActividadesListAdapter extends ArrayAdapter<Actividad> {
		
		public ActividadesListAdapter(Context context) {
			super(context, 0);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) 
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_tem_actividad, null);
			float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
			float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
			FotosFragment.imageDownloader.download(getItem(position).banner, 
					(ImageView) convertView.findViewById(R.id.banner), (int) w, (int) h);
//			((ImageView) convertView.findViewById(R.id.banner)).setImageBitmap(getItem(position).banner);
			((TextView) convertView.findViewById(R.id.txtTitulo)).setText(getItem(position).nombre);
			((TextView) convertView.findViewById(R.id.txtFecha)).setText(getItem(position).contenedor);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent actividad = new Intent(getActivity(), ActividadActivity.class);
					Log.d("putamadre", getItem(position).objectId);
					actividad.putExtra("objectId", getItem(position).objectId);
					startActivity(actividad);
				}
			});
			return convertView;
		}
	}
}
