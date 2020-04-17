package com.konyvtar.kolcsonzes;

import java.sql.Date;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Kolcsonzes {
	
	private int kolcsonID;
	private int tagID;
	private int keszletID;
	private Date kivetel_datum;
	private Date hatarido;
	private Date vissza_datum;
	
	
	public Kolcsonzes() {
		
	}
	
	public Kolcsonzes(int kolcsonID, int tagID, int keszletID, Date kivetel_datum, Date hatarido, Date vissza_datum) {
		super();
		this.kolcsonID = kolcsonID;
		this.tagID = tagID;
		this.keszletID = keszletID;
		this.kivetel_datum = kivetel_datum;
		this.hatarido = hatarido;
		this.vissza_datum = vissza_datum;
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
	
	
	
	
	

}
