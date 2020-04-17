package com.konyvtar.view;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.konyvtar.keszlet.Keszlet;
import com.konyvtar.keszlet.KeszletDbUtil;

public class KolcsonzesViewDbUtil {

	private static KolcsonzesViewDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/konyvtar";

	public static KolcsonzesViewDbUtil getInstance() throws Exception {
		if (instance == null)
			instance = new KolcsonzesViewDbUtil();
		return instance;
	}

	private KolcsonzesViewDbUtil() throws Exception {
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();

		DataSource theDataSource = (DataSource) context.lookup(jndiName);

		return theDataSource;
	}
	
	
	/**
	 * View letrehozasa a lejart kolcsonzesekrol
	 * @throws Exception
	 */
	public void createExpiredView() throws Exception {
		Connection myConn = null;
		Statement myStmt = null;
		
		try {
			myConn = getConnection();
			
			String sql = "CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `lejartkolcsonview` \r\n" + 
					"AS select `kolcsonzes`.`kolcsonID` AS `kolcsonID`,\r\n" + 
					"	`kolcsonzes`.`tagID` AS `tagID`,\r\n" + 
					"	`kolcsonzes`.`keszletID` AS `keszletID`,\r\n" + 
					"	`kolcsonzes`.`kivetel_datum` AS `kivetel_datum`\r\n" + 
					"	,`kolcsonzes`.`hatarido` AS `hatarido`,\r\n" + 
					"	`kolcsonzes`.`vissza_datum` AS `vissza_datum`,\r\n" + 
					"	(to_days(`kolcsonzes`.`vissza_datum`) - to_days(`kolcsonzes`.`hatarido`)) AS `keses` \r\n" + 
					"from \r\n" + 
					"	`kolcsonzes` \r\n" + 
					"where \r\n" + 
					"	(`kolcsonzes`.`vissza_datum` > `kolcsonzes`.`hatarido`);";
			
			
			myStmt = myConn.createStatement();		
			myStmt.executeUpdate(sql);
			System.out.println("View created");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(myConn, myStmt);
		}
	}
	
	/**
	 * View letrehozasa a kolcsonzesekrol, ahol az utolso oszlop kiszamolja a kolcsonzes ota eltelt napok szamat
	 * @throws Exception
	 */
	public void createDaysPassedView() throws Exception {
		Connection myConn = null;
		Statement myStmt = null;
		
		try {
			myConn = getConnection();
			
			String sql = "CREATE OR REPLACE \r\n" + 
					"    ALGORITHM = UNDEFINED \r\n" + 
					"    DEFINER = `root`@`localhost` \r\n" + 
					"    SQL SECURITY DEFINER\r\n" + 
					"VIEW `new_view_jdbc` AS\r\n" + 
					"    SELECT \r\n" + 
					"        `kolcsonzes`.`kolcsonID` AS `kolcsonID`,\r\n" + 
					"        `kolcsonzes`.`tagID` AS `tagID`,\r\n" + 
					"        `kolcsonzes`.`keszletID` AS `keszletID`,\r\n" + 
					"        `kolcsonzes`.`kivetel_datum` AS `kivetel_datum`,\r\n" + 
					"        `kolcsonzes`.`hatarido` AS `hatarido`,\r\n" + 
					"        `kolcsonzes`.`vissza_datum` AS `vissza_datum`,\r\n" + 
					"        (TO_DAYS(CURDATE()) - TO_DAYS(`kolcsonzes`.`kivetel_datum`)) AS `napok_szama`\r\n" + 
					"    FROM\r\n" + 
					"        `kolcsonzes`\r\n";
			
			
			myStmt = myConn.createStatement();		
			myStmt.executeUpdate(sql);
			System.out.println("View created");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(myConn, myStmt);
		}
	}
	
	/**
	 * Lekerdezi a view-bol az x napja kikolcsonzott konyveket, amiket meg nem hoztak vissza
	 * @param napok a felhasznalo altal megadott napok szama, csak az ettol nagyobbakat adja vissza
	 * @return
	 * @throws Exception
	 */
	public List<KolcsonzesView> getKolcsonzesekNapokkal(int napok) throws Exception {
		List<KolcsonzesView> kolcsonzesek = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;


		try {
			myConn = getConnection();

			String sql = "select * from new_view_jdbc WHERE napok_szama >= ? AND vissza_datum IS NULL;";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setInt(1, napok);
			myRs = myStmt.executeQuery();

			while (myRs.next()) {
				int kolcsonzesID = myRs.getInt("kolcsonID");
				int tagID = myRs.getInt("tagID");
				int keszletID = myRs.getInt("keszletID");
				Date kivetel_datum = myRs.getDate("kivetel_datum");
				Date hatarido = myRs.getDate("hatarido");
				Date vissza_datum = myRs.getDate("vissza_datum");
				int napokSzama = myRs.getInt("napok_szama");
				
				KolcsonzesView tempKolcsonzes = new KolcsonzesView(kolcsonzesID, tagID, keszletID, kivetel_datum, hatarido, vissza_datum, napokSzama);

				kolcsonzesek.add(tempKolcsonzes);
			}

			return kolcsonzesek;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	/**
	 * A lejart kolcsonok lekerdezese view-bol
	 * 
	 * @return ArrayList a view összes sorából
	 * @throws Exception
	 */
	public List<KolcsonzesView> getLejartKolcsonzesek() throws Exception {
		List<KolcsonzesView> kolcsonzesek = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from lejartkolcsonview";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {
				int kolcsonzesID = myRs.getInt("kolcsonID");
				int tagID = myRs.getInt("tagID");
				int keszletID = myRs.getInt("keszletID");
				Date kivetel_datum = myRs.getDate("kivetel_datum");
				Date hatarido = myRs.getDate("hatarido");
				Date vissza_datum = myRs.getDate("vissza_datum");
				int keses = myRs.getInt("keses");

				KolcsonzesView tempKolcsonzes = new KolcsonzesView(kolcsonzesID, tagID, keszletID, kivetel_datum, hatarido, vissza_datum, keses);

				kolcsonzesek.add(tempKolcsonzes);
			}

			return kolcsonzesek;
		} finally {
			close(myConn, myStmt, myRs);
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
