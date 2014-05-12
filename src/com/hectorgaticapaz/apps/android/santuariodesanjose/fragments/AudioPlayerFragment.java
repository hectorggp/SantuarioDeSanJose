package com.hectorgaticapaz.apps.android.santuariodesanjose.fragments;

import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hectorgaticapaz.apps.android.santuariodesanjose.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AudioPlayerFragment extends ListFragment {

	private static final String TAG = "AudioPlayerFragment";

	private MediaPlayer mediaPlayer;

	private String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_audio_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ArrayAdapter<Audio> adapter = new AudioListAdapter();

		final ProgressDialog mDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage("Cargando Audios");
		mDialog.show();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("audio_marcha");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> audioList, ParseException e) {
				if (e == null) {
					for (ParseObject objeto : audioList) {
						ParseFile audio = objeto.getParseFile("audio");
						String nombre_marcha = objeto.getString("nombre_marcha");
						String url_audio = audio.getUrl();
						adapter.add(new Audio(nombre_marcha, "duracion", url_audio));
					}
					mDialog.dismiss();
				}
				else {
					mDialog.dismiss();
					e.printStackTrace();
					Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_LONG).show();
				}
			}
		});

		setListAdapter(adapter);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer = null;
		}
	}

	public void play(String url) {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) 
			mediaPlayer.stop();
		if (url.equals(this.url)) {
			this.url = null;
			mediaPlayer = null;
			return;
		}
		this.url = url;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
		mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
	}

	private class Audio {
		public Audio(String nombre, String duracion, String url) {
			this.nombre = nombre;
			this.duracion = duracion;
			this.url = url;
		}

		String nombre;

		String duracion;

		String url;
	}

	private class AudioListAdapter extends ArrayAdapter<Audio> {

		public AudioListAdapter() {
			super(getActivity(), R.layout.list_item_song);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song, null);
			((ImageButton) convertView.findViewById(R.id.imageButton1)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					play(getItem(position).url);
				}
			});
			((TextView) convertView.findViewById(R.id.textView1)).setText(getItem(position).nombre);
			return convertView;
		}

	}
}
