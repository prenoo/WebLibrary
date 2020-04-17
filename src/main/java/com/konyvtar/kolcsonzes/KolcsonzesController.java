package com.konyvtar.kolcsonzes;

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

@ManagedBean(name = "kolcsonzescontroller")
@SessionScoped
public class KolcsonzesController implements Serializable {

	private List<Kolcsonzes> kolcsonzesek;
	private KolcsonzesDbUtil kolcsonzesDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public KolcsonzesController() throws Exception {
		kolcsonzesek = new ArrayList<>();
		kolcsonzesDbUtil = KolcsonzesDbUtil.getInstance();
	}

	public List<Kolcsonzes> getKolcsonzesek() {
		return kolcsonzesek;
	}

	public List<Kolcsonzes> getKolcsonzesekView() {
		return kolcsonzesek;
	}

	public void loadKolcsonzesek() {
		try {
			kolcsonzesek = kolcsonzesDbUtil.getKolcsonzesek();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading table", e);
			addErrorMessage(e);
		}
	}

	public String addKolcsonzes(Kolcsonzes theKolcsonzes) {
		logger.info("Adding kolcsoznes: " + theKolcsonzes);

		try {
			// ha nem aktív a tag vagy 6-nal tobb aktiv kolcsonzese van nem enged tobb keszletet kivenni
			boolean status = kolcsonzesDbUtil.isActive(theKolcsonzes.getTagID());
			if (status) {
				int count = kolcsonzesDbUtil.getCount(theKolcsonzes.getTagID());
				if (count > 6) {
					logger.log(Level.SEVERE, "Túl sok kölcsönzött tétel");
				} else {
					kolcsonzesDbUtil.addKolcsonzes(theKolcsonzes);

				}
			} else
				logger.log(Level.SEVERE, "Nem aktív a tag");

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding kolcsonzes", e);
			addErrorMessage(e);

			return null;
		}

		return "list-kolcsonzes?faces-redirect=true";
	}

	public String visszahoz(int kolcsonID) {
		try {
			kolcsonzesDbUtil.visszahoz(kolcsonID);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error", e);
			addErrorMessage(e);

			return null;
		}

		return "list-kolcsonzes?faces-redirect=true";
	}

	public String loadKolcsonzes(int kolcsonzesID) {
		logger.info("loading kolcsonzes: " + kolcsonzesID);

		try {
			Kolcsonzes theKolcsonzes = kolcsonzesDbUtil.getKolcsonzes(kolcsonzesID);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("kolcsonzes", theKolcsonzes);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error laoding kolcsonzes ID: " + kolcsonzesID, e);
			addErrorMessage(e);
			return null;
		}

		return "update-kolcsonzes-form.xhtml";
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
