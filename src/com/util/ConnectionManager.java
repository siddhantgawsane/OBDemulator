/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class ConnectionManager extends DatabaseUtility {

	public static void main(String[] args) {
		getDBConnection();
	}

	public static List getOwnerInfoById(String id) {
		String query = "SELECT * from obd.ownertable where uid='"+id+"'";
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		
		
		System.out.println("Size "+lst.size());
		return lst;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static int index = 0;

	public static OBDModel getOBDList() {
		String query = "SELECT * FROM obd.obd  order by `time` LIMIT " + index
				+ ",1";
		index++;
		System.out.println(query);
		List beans = getBeanList(OBDModel.class, query);
		System.out.println("Beans Size " + beans.size());
		OBDModel o = null;
		if (beans.size() > 0) {
			o = (OBDModel) beans.get(0);
		}
		return o;

	}

	public static boolean setOBDList(String load_pct, String iat, String maf,
			String throttlepos, String time, String vss, String rpm, String temp, String ownerid) {
		boolean b = false;
		String query = "INSERT INTO obd.obdserver (load_pct, temp, rpm, vss, iat, maf, throttlepos, time, ownerid)  values(?,?,?,?,?,?,?,?)";

		System.out.println(query);
		int i = executeUpdate(query, new Object[] { load_pct,temp, rpm, vss, iat, maf,
				throttlepos, time,ownerid });

		if (i >= 0) {
			b = true;
		}
		return b;
	}

	public static List getOBDAllList(String vehicleNo) {
		String query = "";
		if (vehicleNo.length() > 0)
			query = "SELECT * FROM obd.obd o,adminvehicles a where o.vehicleNo=a.vehicleNo and a.vehicleno like '%"
					+ vehicleNo + "%' order by `time` Limit 1,1000";
		else
			query = "SELECT * FROM obd.obd o,adminvehicles a where o.vehicleNo =a.vehicleNo order by `time` Limit 1,1000";
		System.out.println(query);
		List beans = getBeanList(OBDModel.class, query);
		return beans;
	}
	
	
	
	public static OwnerInfoModel getOwnerId(String imei) {
		String query = "";
		OwnerInfoModel oi= null;
		query = "SELECT * FROM obd.ownertable o where o.imei='"+imei+"'";
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		if (lst.size() > 0) {
			oi = (OwnerInfoModel) lst.get(0);
		}
		return oi;
	}

	
	
	public static List validateUser(String userid, String pwd) {
		String query = "Select * from obd.useraccounts u where u.userid like '"+userid+"' and u.pass like '"+pwd+"' ";
		return getMapList(query);
	}
	
	
	

	public static boolean dataExists(String query) {

		boolean success = false;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			if (rs.next()) {
				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static String getMaxValue(String query) {

		String success = "";
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			if (rs.next()) {
				success = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}
}
