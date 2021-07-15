package com.gastromanager.mainwindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Floor {
	private static final String XML_TAG = "floor";
	private static final String INSERT_TO_FLOOR = "INSERT INTO FLOOR VALUES(?,?,?,?,?)";
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DELETE_FLOOR = "delete from floor where floor_id = ?";
	
	private String title;
	private String value;
	private String createdBy;
	private String createdDate;
	private boolean isInDb;
	private boolean toDelete;
	

	public Floor(String title, String value, String createdBy, boolean isInDb) {
		this.title = title;
		this.value = value;
		this.createdBy = createdBy;
		this.setCreatedDate();
		this.isInDb = isInDb;
		this.toDelete = false;
	}
	
	public void save(Connection connection) {
		if (toDelete) {
			try (PreparedStatement pr = connection.prepareStatement(DELETE_FLOOR)) {
				pr.setInt(1, Integer.parseInt(value));
				pr.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		if (!isInDb) {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TO_FLOOR);
				
				preparedStatement.setInt(1, Integer.parseInt(value));
				preparedStatement.setString(2, title);
				preparedStatement.setInt(3, Integer.parseInt(value));
				preparedStatement.setString(4, "");
				preparedStatement.setString(5, createdDate);
				
				int rows = preparedStatement.executeUpdate();
//				System.out.println(rows + " rows inserted into FLOOR table");
				isInDb = true;
				preparedStatement.close();
			} catch (SQLException e) {
				System.err.println("SQLException in class: Floor.java at method: save()");
				e.printStackTrace();
			}
		} else {
//			System.out.println("Floor: " + title + " already exists in the db.");
		}
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public static String getXmlTag() {
		return XML_TAG;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		this.createdDate = sdf.format(date);
	}
	public boolean isToDelete() {
		return toDelete;
	}
	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
	}
}
