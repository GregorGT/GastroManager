package com.gastromanager.mainwindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gastromanager.db.DbConnection;

public class DeleteOld extends Thread{
	private static final String DELETE_OLD_RESERVATIONS = "DELETE FROM reservations WHERE end_date < ?";
	private static final Connection connection = DbConnection.getDbConnection().gastroDbConnection;
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public void run() {
		while (true) {
			deleteOldReservations();
			try {
				Thread.sleep(60*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteOldReservations() {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(DELETE_OLD_RESERVATIONS);
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			preparedStatement.setString(1, sdf.format(new Date()));
			int rows = preparedStatement.executeUpdate();
			System.out.println(rows + " rows deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
