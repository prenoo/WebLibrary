package com.konyvtar.keszlet;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Keszlet {
	
	private Integer keszlet_ID;
	private String szerzo;
	private String cim;
	private String kategoria;
	private int darabszam;
	
	public Keszlet() {
		
	}
	
	
	public Keszlet(Integer keszlet_ID, String szerzo, String cim, String kategoria, int darabszam) {
		super();
		this.keszlet_ID = keszlet_ID;
		this.szerzo = szerzo;
		this.cim = cim;
		this.kategoria = kategoria;
		this.darabszam = darabszam;
	}


	public Integer getKeszlet_ID() {
		return keszlet_ID;
	}

	public void setKeszlet_ID(Integer keszlet_ID) {
		this.keszlet_ID = keszlet_ID;
	}

	public String getSzerzo() {
		return szerzo;
	}

	public void setSzerzo(String szerzo) {
		this.szerzo = szerzo;
	}

	public String getCim() {
		return cim;
	}

	public void setCim(String cim) {
		this.cim = cim;
	}

	public String getKategoria() {
		return kategoria;
	}

	public void setKategoria(String kategoria) {
		this.kategoria = kategoria;
	}

	public int getDarabszam() {
		return darabszam;
	}

	public void setDarabszam(int darabszam) {
		this.darabszam = darabszam;
	}
}
