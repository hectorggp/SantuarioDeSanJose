package com.hectorgaticapaz.apps.android.santuariodesanjose.objects;

import java.util.Date;

import android.graphics.Bitmap;

public class Actividad {

	public String banner;
	public String detalle;
	public Date date;
	public String nombre;
	public int prioridad;
	public String contenedor;
	public String objectId;

	public Actividad(String banner, String detalle, Date date, String nombre,
			int prioridad, String contenedor, String objectId) {

		this.banner = banner;
		this.detalle = detalle;
		this.date = date;
		this.nombre = nombre;
		this.prioridad = prioridad;
		this.contenedor = contenedor;
		this.objectId = objectId;
	}
}
