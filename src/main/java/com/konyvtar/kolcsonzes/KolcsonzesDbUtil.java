package com.konyvtar.kolcsonzes;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.konyvtar.keszlet.Keszlet;
import com.konyvtar.keszlet.KeszletDbUtil;

public class KolcsonzesDbUtil {

	private static KolcsonzesDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/konyvtar";

	public static KolcsonzesDbUtil getInstance() throws Exception {
		if (instance == null)
			instance = new KolcsonzesDbUtil();
		return instance;
	}

	private KolcsonzesDbUtil() throws Exception {
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();

		DataSource theDataSource = (DataSource) context.lookup(jndiName);

		return theDataSource;
	}

	/**
	 * Adatbázisból az összes adat beolvasása
	 * 
	 * @return ArrayList az adatbázis összes sorából
	 * @throws Exception
	 */
	public List<Kolcsonzes> getKolcsonzesek() throws Exception {
		List<Kolcsonzes> kolcsonzesek = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from kolcsonzes";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {
				int kolcsonzesID = myRs.getInt("kolcsonID");
				int tagID = myRs.getInt("tagID");
				int keszletID = myRs.getInt("keszletID");
				Date kivetel_datum = myRs.getDate("kivetel_datum");
				Date hatarido = myRs.getDate("hatarido");
				Date vissza_datum = myRs.getDate("vissza_datum");

				Kolcsonzes tempKolcsonzes = new Kolcsonzes(kolcsonzesID, tagID, keszletID, kivetel_datum, hatarido,
						vissza_datum);

				kolcsonzesek.add(tempKolcsonzes);
			}

			return kolcsonzesek;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}
	

	/**
	 * Egy adott kolcsonzes lekerdezese
	 * 
	 * @param kolcsonzesID
	 * @return
	 * @throws Exception
	 */
	public Kolcsonzes getKolcsonzes(int kolcsonzesID) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from kolcsonzes where kolcsonID = ?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, kolcsonzesID);

			myRs = myStmt.executeQuery();

			Kolcsonzes theKolcsonzes = null;

			if (myRs.next()) {
				int id = myRs.getInt("kolcsonID");
				int tagID = myRs.getInt("tagID");
				int keszletID = myRs.getInt("keszletID");
				Date kivetel_datum = myRs.getDate("kivetel_datum");
				Date hatarido = myRs.getDate("hatarido");
				Date vissza_datum = myRs.getDate("vissza_datum");
				theKolcsonzes = new Kolcsonzes(id, tagID, keszletID, kivetel_datum, hatarido, vissza_datum);
			} else {
				throw new Exception("Couldn't find kolcsonzesID: " + kolcsonzesID);
			}

			return theKolcsonzes;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}
	
	

	/**
	 * Megszámolja egy adott tagnak hány aktív kölcsönzése van
	 * 
	 * @param kolcsonzesID
	 * @return kölcsönzött tételek száma
	 * @throws Exception
	 */
	public int getCount(int kolcsonzesID) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select count(tagID) FROM kolcsonzes WHERE tagID=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, kolcsonzesID);

			myRs = myStmt.executeQuery();

			int count = Integer.MAX_VALUE;
			if (myRs.next()) {
				count = myRs.getInt(1);
			} else {
				throw new Exception("Nincs ilyen azonositoval rendelkezo tag");
			}

			return count;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}
	
	public boolean isActive(int kolcsonzesID) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select statusz FROM konyvtar.tag WHERE tag_ID=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, kolcsonzesID);

			myRs = myStmt.executeQuery();

			String status;
			if (myRs.next()) {
				status = myRs.getString(1);
			} else {
				throw new Exception("Nincs ilyen azonositoval rendelkezo tag");
			}
			
			if(status.equals("aktiv"))
				return true;
			else
				return false;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	/**
	 * Új kölcsönzés regisztrálása. A kolcsonzott tetel darabszamat csokkenti egyel.
	 * A kivetel datuma automatikusan a mai nap, a hatarido automatikusan a mai
	 * naphoz kepest +1 honap.
	 * 
	 * @param theKolcsonzes
	 * @throws Exception
	 */
	public void addKolcsonzes(Kolcsonzes theKolcsonzes) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		KeszletDbUtil keszletDbUtil = KeszletDbUtil.getInstance();
		Keszlet theKeszlet = keszletDbUtil.getKeszlet(theKolcsonzes.getKeszletID());

		try {
			myConn = getConnection();

			String sql = "insert into kolcsonzes (tagID, keszletID, kivetel_datum, hatarido) values (?,?,?,?)";

			// mai nap MySQL kompatibilis formatumma alakitasa
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			java.util.Date today = calendar.getTime();
			java.sql.Date sqlToday = new java.sql.Date(today.getTime());

			// hatarido +1 honap a maihoz kepest
			calendar.add(java.util.Calendar.MONTH, 1);
			java.util.Date hatarido = calendar.getTime();
			java.sql.Date sqlHatarido = new java.sql.Date(hatarido.getTime());

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, theKolcsonzes.getTagID());
			myStmt.setInt(2, theKolcsonzes.getKeszletID());
			myStmt.setDate(3, sqlToday);
			myStmt.setDate(4, sqlHatarido);

			myStmt.execute();

			// 1-el csokkenti a darabszamot
			keszletDbUtil.keszletCsokkentes(theKeszlet);
		} finally {
			close(myConn, myStmt);
		}

	}


	/**
	 * Visszahozott tetel regisztralasa. A visszahozas napjat automatikusan rogziti.
	 * Darabszamot megnoveli egyel.
	 * 
	 * @param kolcsonID
	 * @throws Exception
	 */
	public void visszahoz(int kolcsonID) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		Kolcsonzes theKolcsonzes = getKolcsonzes(kolcsonID);
		KeszletDbUtil keszletDbUtil = KeszletDbUtil.getInstance();
		Keszlet theKeszlet = keszletDbUtil.getKeszlet(theKolcsonzes.getKeszletID());

		try {
			myConn = getConnection();

			String sql = "update kolcsonzes " + "set vissza_datum = ? " + "where kolcsonID = ?";

			myStmt = myConn.prepareStatement(sql);

			// datum formazasa MySQL-hez
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			java.util.Date today = calendar.getTime();
			java.sql.Date sqlToday = new java.sql.Date(today.getTime());

			myStmt.setDate(1, sqlToday);
			myStmt.setInt(2, kolcsonID);

			myStmt.execute();

			// darabszam novelese
			keszletDbUtil.keszletNoveles(theKeszlet);

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
