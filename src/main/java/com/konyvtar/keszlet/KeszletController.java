package com.konyvtar.keszlet;

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

import com.konyvtar.keszlet.Keszlet;
import com.konyvtar.keszlet.KeszletDbUtil;

@ManagedBean(name = "keszletcontroller")
@SessionScoped
public class KeszletController implements Serializable {

	private List<Keszlet> keszletek;
	private KeszletDbUtil keszletDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public KeszletController() throws Exception {
		keszletek = new ArrayList<>();
		keszletDbUtil = KeszletDbUtil.getInstance();
	}

	public List<Keszlet> getKeszletek() {
		return keszletek;
	}

	public void loadKeszletek() {
		keszletek.clear();

		try {
			// get all keszlet from database
			keszletek = keszletDbUtil.getKeszletek();
		} catch (Exception e) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading keszlet table", e);

			// add error message for JSF page
			addErrorMessage(e);
		}

	}

	public String addKeszlet(Keszlet theKeszlet) {

		logger.info("Adding keszlet: " + theKeszlet);

		try {

			// add keszlet to the database
			keszletDbUtil.addKeszlet(theKeszlet);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding tags", exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "list-keszlet?faces-redirect=true";
	}

	public String loadKeszlet(int tagId) {

		logger.info("loading keszlet: " + tagId);

		try {
			// get keszlet from database
			Keszlet theKeszlet = keszletDbUtil.getKeszlet(tagId);

			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("keszlet", theKeszlet);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading keszlet id:" + tagId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "update-keszlet-form.xhtml";
	}

	public String updateKeszlet(Keszlet theKeszlet) {

		logger.info("updating keszlet: " + theKeszlet);

		try {

			// update keszlet in the database
			keszletDbUtil.updateKeszlet(theKeszlet);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating keszlet: " + theKeszlet, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "list-keszlet?faces-redirect=true";
	}

	public String deleteKeszlet(int tagId) {

		logger.info("Deleting keszlet id: " + tagId);

		try {
			// keszlet torlese adatbazisbol
			keszletDbUtil.deleteKeszlet(tagId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting keszlet id: " + tagId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "list-keszlet";
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
