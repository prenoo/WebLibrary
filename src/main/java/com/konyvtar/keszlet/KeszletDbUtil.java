package com.konyvtar.keszlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class KeszletDbUtil {

	private static KeszletDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/konyvtar";

	public static KeszletDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new KeszletDbUtil();
		}

		return instance;
	}

	private KeszletDbUtil() throws Exception {
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();

		DataSource theDataSource = (DataSource) context.lookup(jndiName);

		return theDataSource;
	}

	public List<Keszlet> getKeszletek() throws Exception {

		List<Keszlet> keszletek = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from keszlet";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int id = myRs.getInt("keszlet_ID");
				String szerzo = myRs.getString("szerzo");
				String cim = myRs.getString("cim");
				String kategoria = myRs.getString("kategoria");
				int darabszam = myRs.getInt("darabszam");

				// create new student object
				Keszlet tempKeszlet = new Keszlet(id, szerzo, cim, kategoria, darabszam);

				// add it to the list of students
				keszletek.add(tempKeszlet);
			}

			return keszletek;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void addKeszlet(Keszlet theKeszlet) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into keszlet (szerzo, cim, kategoria, darabszam) values (?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theKeszlet.getSzerzo());
			myStmt.setString(2, theKeszlet.getCim());
			myStmt.setString(3, theKeszlet.getKategoria());
			myStmt.setInt(4, theKeszlet.getDarabszam());

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}

	}

	/*
	 * Készlet darabszámának csökkentése egyel ha kikölcsönöznek egy tételt
	 */
	public void keszletCsokkentes(Keszlet theKeszlet) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update keszlet " + " set darabszam=?" + " where keszlet_ID=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			int darab = theKeszlet.getDarabszam() - 1;
			myStmt.setInt(1, darab);
			myStmt.setInt(2, theKeszlet.getKeszlet_ID());
			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}

	/*
	 * Készlet darabszámának növelése 1-el ha visszahoznak egy tételt
	 */
	public void keszletNoveles(Keszlet theKeszlet) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update keszlet " + " set darabszam=?" + " where keszlet_ID=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			int darab = theKeszlet.getDarabszam() + 1;
			myStmt.setInt(1, darab);
			myStmt.setInt(2, theKeszlet.getKeszlet_ID());
			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}

	public Keszlet getKeszlet(int keszletID) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from keszlet where keszlet_ID=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, keszletID);

			myRs = myStmt.executeQuery();

			Keszlet theKeszlet = null;

			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("keszlet_ID");
				String szerzo = myRs.getString("szerzo");
				String cim = myRs.getString("cim");
				String kategoria = myRs.getString("kategoria");
				int darabszam = myRs.getInt("darabszam");

				theKeszlet = new Keszlet(id, szerzo, cim, kategoria, darabszam);
			} else {
				throw new Exception("Could not find tag id: " + keszletID);
			}

			return theKeszlet;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void updateKeszlet(Keszlet theKeszlet) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update keszlet " + " set szerzo=?, cim=?, kategoria=?, darabszam=?" + " where keszlet_ID=?";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setString(1, theKeszlet.getSzerzo());
			myStmt.setString(2, theKeszlet.getCim());
			myStmt.setString(3, theKeszlet.getKategoria());
			myStmt.setInt(4, theKeszlet.getDarabszam());
			myStmt.setInt(5, theKeszlet.getKeszlet_ID());
			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}

	}

	public void deleteKeszlet(int keszletID) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from keszlet where keszlet_ID=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, keszletID);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}

	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();

		return theConn;
	}

	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}

	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
