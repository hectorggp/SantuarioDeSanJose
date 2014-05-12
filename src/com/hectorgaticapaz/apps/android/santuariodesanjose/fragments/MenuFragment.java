package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import com.google.android.gms.maps.model.LatLng;
import com.hectorgaticapaz.apps.android.santuariodesanjose.BaseActivity;
import com.hectorgaticapaz.apps.android.santuariodesanjose.LaMeraMama;
import com.hectorgaticapaz.apps.android.santuariodesanjose.R;
import com.hectorgaticapaz.apps.android.santuariodesanjose.helper.NetworkHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MenuFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().findViewById(R.id.txtActividades).setOnClickListener(this);
		getActivity().findViewById(R.id.txtFotos).setOnClickListener(this);
		getActivity().findViewById(R.id.txtWeb).setOnClickListener(this);
		getActivity().findViewById(R.id.txtUbicacion).setOnClickListener(this);
		getActivity().findViewById(R.id.txtVideos).setOnClickListener(this);
		getActivity().findViewById(R.id.txtAudios).setOnClickListener(this);
		getActivity().findViewById(R.id.txtContactenos).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String titulo = null;
		String url = null;
		switch (v.getId()) {
		case R.id.txtActividades:
			if (revisarConexion(v.getId())) {
				((LaMeraMama) getActivity()).switchContent(new ActividadesFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Actividades");
			}
			break;
		case R.id.txtFotos:
			if (revisarConexion(v.getId())) {
				((LaMeraMama) getActivity()).switchContent(new FotosFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Fotos");
			}
			break;
		case R.id.txtVideos:
			 url = "http://m.youtube.com/user/santsanjose";
			 titulo = "Videos";
		case R.id.txtWeb:
			if (url == null) {
				url = "http://santuariodesanjose.org.gt/sj/";
				titulo = "Web";
			}
			if (revisarConexion(v.getId())) {
				WebFragment wf = new WebFragment();
				Bundle args = new Bundle();
				args.putString("Url", url);
				wf.setArguments(args);
				((LaMeraMama) getActivity()).switchContent(wf);
				((BaseActivity) getActivity()).getSupportActionBar().setTitle(titulo);
			}
			break;
		case R.id.txtUbicacion:
			if (revisarConexion(v.getId())) {
				((LaMeraMama) getActivity()).switchContent(new MapaGoogleFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Ubicación");
			}
			break; 
		case R.id.txtAudios:
			if (revisarConexion(v.getId())) {
				((LaMeraMama) getActivity()).switchContent(new AudioPlayerFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Audios");
			}
			break;
		case R.id.txtContactenos:
			((LaMeraMama) getActivity()).switchContent(new ContactoFragment());
			((BaseActivity) getActivity()).getSupportActionBar().setTitle("Contáctenos");
			break;
		}
	}

	private boolean revisarConexion(int id) {
		if (NetworkHelper.NetworkStatus(getActivity()))
			return true;
		else {
			SinConexionFragment f = new SinConexionFragment();
			Bundle args = new Bundle();
			args.putInt("viewId", id);
			f.setArguments(args);
			((LaMeraMama) getActivity()).switchContent(f);
			return false;
		}
	}
}
