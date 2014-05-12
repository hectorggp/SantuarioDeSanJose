package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

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

public class SinConexionFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sin_conexion, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().findViewById(R.id.btnReintentar).setOnClickListener(this);
	}

	@Override
	public void onClick(View vf) {
		int id = getArguments().getInt("viewId");
		String titulo = null;
		String url = null;
		switch (id) {
		case R.id.txtActividades:
			if (revisarConexion(id)) {
				((LaMeraMama) getActivity())
						.switchContent(new ActividadesFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Actividades");
			}
			break;
		case R.id.txtFotos:
			if (revisarConexion(id)){
				((LaMeraMama) getActivity()).switchContent(new FotosFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Fotos");
			}
			break;
		case R.id.txtVideos:
			url = "http://m.youtube.com/user/santsanjose";
			titulo = "Videos";
		case R.id.txtWeb:
			if (url == null) {
				titulo = "Web";
				url = "http://santuariodesanjose.org.gt/sj/";
			}
			if (revisarConexion(id)) {
				WebFragment wf = new WebFragment();
				Bundle args = new Bundle();
				args.putString("Url", url);
				wf.setArguments(args);
				((LaMeraMama) getActivity()).switchContent(wf);
				((BaseActivity) getActivity()).getSupportActionBar().setTitle(titulo);
			}
			break;
		case R.id.txtUbicacion:
			if (revisarConexion(id)){
				((LaMeraMama) getActivity()).switchContent(new MapaGoogleFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Ubicación");
			}
			break;
		case R.id.txtContactenos:
			if (revisarConexion(id)) {
				((LaMeraMama) getActivity()).switchContent(new AudioPlayerFragment());
				((BaseActivity) getActivity()).getSupportActionBar().setTitle("Audios");
			}
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
