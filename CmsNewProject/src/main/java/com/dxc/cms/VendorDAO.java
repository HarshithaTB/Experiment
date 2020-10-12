package com.dxc.cms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendorDAO {

	public Vendor[] showVendors() throws SQLException {
		Connection connection = ConnectionHelper.getConnection();
		PreparedStatement pst = connection.prepareStatement("select count(*) cnt from vendor");
		ResultSet rs = pst.executeQuery();
		rs.next();
		int cnt = rs.getInt("cnt");
		pst = connection.prepareStatement("select * from Vendor");
		rs = pst.executeQuery();
		Vendor[] vendorlist = new Vendor[cnt];
		Vendor vendor = null;
		int i=0;
		FoodStatus fs=FoodStatus.AVAILABLE;
		while(rs.next()) {
			vendor = new Vendor();
			vendor.setVenId(rs.getInt("VEN_ID"));
			vendor.setVenName(rs.getString("VEN_NAME"));
			vendor.setVenPassword(rs.getString("VEN_PASSWORD"));
			vendor.setVenEmail(rs.getString("VEN_EMAIL"));
			vendor.setVenMobile(rs.getString("VEN_MOBILE"));
			vendor.setVenAddress(rs.getString("VEN_ADDRESS"));
			
			vendorlist[i]=vendor;
			i++;
		}
		return vendorlist;

	}
	public Vendor searchByVendorName(String vendorName) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select * from Vendor where VEN_NAME=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setString(1, vendorName);
		ResultSet rs = pst.executeQuery();
		Vendor vendor = null;
		if (rs.next()) {
			vendor = new Vendor();
			vendor.setVenId(rs.getInt("VEN_ID"));
			vendor.setVenName(rs.getString("VEN_NAME"));
			vendor.setVenPassword(rs.getString("VEN_PASSWORD"));
			vendor.setVenEmail(rs.getString("VEN_EMAIL"));
			vendor.setVenMobile(rs.getString("VEN_MOBILE"));
			vendor.setVenAddress(rs.getString("VEN_ADDRESS"));
		}
		return vendor;
	}
	public Vendor searchByVendorId(int venId) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select * from Vendor where VEN_ID=?";
		PreparedStatement pst = con.prepareStatement(cmd);
		pst.setInt(1, venId);
		ResultSet rs = pst.executeQuery();
		Vendor vendor = null;
		if (rs.next()) {
			vendor = new Vendor();
			vendor.setVenId(rs.getInt("VEN_ID"));
			vendor.setVenName(rs.getString("VEN_NAME"));
			vendor.setVenPassword(rs.getString("VEN_PASSWORD"));
			vendor.setVenEmail(rs.getString("VEN_EMAIL"));
			vendor.setVenMobile(rs.getString("VEN_MOBILE"));
			vendor.setVenAddress(rs.getString("VEN_ADDRESS"));
		}
		return vendor;
	}
	public int authenticate(String user, String password) throws SQLException {
		Connection con = ConnectionHelper.getConnection();
		String cmd = "select count(*) cnt from Vendor where VEN_NAME=? "
				+ " AND VEN_PASSWORD=?";
		PreparedStatement  pst = con.prepareStatement(cmd);
		pst.setString(1, user);
		pst.setString(2, password);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int cnt = rs.getInt("cnt");
		return cnt;
	}

	public String acceptOrReject(int ordId,int venId,String Status) throws SQLException {
		String result="";
		double Price;
		OrderDetail order = new OrderDAO().getOrderbyOrderId(ordId);
		Wallet wallet = new WalletDAO().showByWalltType(order.getCusId(), order.getWalType());
		if(Status.equalsIgnoreCase("ACCEPTED")) {
		String cmd ="UPDATE ORDER_DETAIL SET ORD_STATUS=? WHERE ORDER_ID=?";
		Connection con = ConnectionHelper.getConnection();
	    PreparedStatement pst = con.prepareStatement(cmd);
	    pst.setString(1, Status);
	    pst.setInt(2, ordId);
	    pst.executeUpdate();
	    result="Order Accepted!";
		}
		if(Status.equalsIgnoreCase("REJECTED")) {
			String cmd ="UPDATE ORDER_DETAIL SET ORD_STATUS=? WHERE ORDER_ID=?";
			Connection con = ConnectionHelper.getConnection();
		    PreparedStatement pst = con.prepareStatement(cmd);
		    pst.setString(1, Status);
		    pst.setInt(2, ordId);
		    pst.executeUpdate();
		    Price = (wallet.getWalAmount() + order.getOrdAmount());
		    cmd = "Update Wallet SET WAL_AMOUNT=? WHERE WAL_TYPE=? AND CUS_ID=?";
		    pst = con.prepareStatement(cmd);
		    pst.setDouble(1, Price);
		    pst.setString(2, order.getWalType());
		    pst.setInt(3, order.getCusId());
		    result = "Order Rejected!";
		    
		}
		return result;
	}
}
