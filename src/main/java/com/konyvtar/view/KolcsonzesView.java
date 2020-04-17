package com.konyvtar.view;

import java.sql.Date;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name="kolcsonzesview")
public class KolcsonzesView {
	
	private int kolcsonID;
	private int tagID;
	private int keszletID;
	private Date kivetel_datum;
	private Date hatarido;
	private Date vissza_datum;
	private int keses;
	private int napok;
	
	
	public KolcsonzesView() {
		
	}
	
	public KolcsonzesView(int kolcsonID, int tagID, int keszletID, Date kivetel_datum, Date hatarido, Date vissza_datum, int keses) {
		super();
		this.kolcsonID = kolcsonID;
		this.tagID = tagID;
		this.keszletID = keszletID;
		this.kivetel_datum = kivetel_datum;
		this.hatarido = hatarido;
		this.vissza_datum = vissza_datum;
		this.keses = keses;
	}

	
	public int getKolcsonID() {
		return kolcsonID;
	}

	public void setKolcsonID(int kolcsonID) {
		this.kolcsonID = kolcsonID;
	}

	public int getTagID() {
		return tagID;
	}

	public void setTagID(int tagID) {
		this.tagID = tagID;
	}

	public int getKeszletID() {
		return keszletID;
	}

	public void setKeszletID(int keszletID) {
		this.keszletID = keszletID;
	}

	public Date getKivetel_datum() {
		return kivetel_datum;
	}

	public void setKivetel_datum(Date kivetel_datum) {
		this.kivetel_datum = kivetel_datum;
	}

	public Date getHatarido() {
		return hatarido;
	}

	public void setHatarido(Date hatarido) {
		this.hatarido = hatarido;
	}

	public Date getVissza_datum() {
		return vissza_datum;
	}

	public void setVissza_datum(Date vissza_datum) {
		this.vissza_datum = vissza_datum;
	}

	public int getKeses() {
		return keses;
	}

	public void setKeses(int keses) {
		this.keses = keses;
	}

	public int getNapok() {
		return napok;
	}

	public void setNapok(int napok) {
		this.napok = napok;
	}
	
	/*
	public int napok() {
		FacesContext fc = FacesContext.getCurrentInstance();
		this.napok = Integer.parseInt(getNapokParam(fc));
		
		return napok;
	}
	
	public String getNapokParam(FacesContext fc) {
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("napok");
	}
	*/
	
	
	

}
