package com.konyvtar.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "kolcsonzesviewcontroller")
@SessionScoped
public class KolcsonzesViewController implements Serializable {

	private List<KolcsonzesView> kolcsonzesek;
	private KolcsonzesViewDbUtil kolcsonzesDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	private int napok;

	public KolcsonzesViewController() throws Exception {
		kolcsonzesek = new ArrayList<>();
		kolcsonzesDbUtil = KolcsonzesViewDbUtil.getInstance();
	}

	public List<KolcsonzesView> getKolcsonzesek() {
		return kolcsonzesek;
	}
	
	
	/**
	 * Lejart kolcsonzesek listazasa
	 */
	public void loadKolcsonzesek() {
		try {
			kolcsonzesDbUtil.createExpiredView();
			kolcsonzesek = kolcsonzesDbUtil.getLejartKolcsonzesek();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading table", e);
			addErrorMessage(e);
		}
	}
	
	/**
	 * X napja kolcsonzott konyvek listazasa
	 */
	public void loadKolcsonzesekView() {
		try {
			kolcsonzesDbUtil.createDaysPassedView();
			kolcsonzesek = kolcsonzesDbUtil.getKolcsonzesekNapokkal(napok);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading table", e);
			addErrorMessage(e);
		}
	}
	
	/**
	 * Kolcsonzes ota eltelt napok szama, ha meg nem hoztak vissza a konyvet
	 * @return
	 */
	public String kolcsonNapok() {
		setNapok(napok);
		System.out.println("Napok: " + getNapok());
		return "kolcsonzes_view.xhtml";
	}
	
	

	

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public int getNapok() {
		return napok;
	}

	public void setNapok(int napok) {
		this.napok = napok;
	}


}
