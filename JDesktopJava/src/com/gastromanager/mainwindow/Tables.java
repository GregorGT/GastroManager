package com.gastromanager.mainwindow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tables extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private static final String XML_TAG = "table";
	private static final Color TEXT_COLOR = Color.RED;
	private static final String INSERT_TO_TABLEDETAILS = "INSERT INTO tabledetails VALUES (?, ?, ?, ?, ?, ?)";
	private static final String DELETE_TO_TABLEDETAILS = "delete from tabledetails where table_id = ? and floor_id = ?";
	private static final String UPDATE_TO_TABLEDETAILS = "update tabledetails set table_id = ? where table_id = ? and floor_id = ?";
	private static final String SELECT_MAX_ID = "SELECT max(id)+1 FROM tabledetails";
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private int rotate;
	private Color color;
	private String value;
	private String floorId;
	private boolean isInDb;
	private boolean isInDbLocationTable;
	private String createdDate;
	private String lastModifiedDate;
	private String previousValue;

	public Tables(int x, int y, int width, int height, int rotate, String value, String floorId, boolean isInDb, boolean isInDbLocationTable) {
		super(x, y, width, height);
		this.rotate = rotate;
		this.color = Color.BLACK;
		this.value = this.previousValue = value;
		this.floorId = floorId;
		this.isInDb = isInDb;
		this.isInDbLocationTable = isInDbLocationTable;
		this.setCreatedDate();
		this.lastModifiedDate = this.createdDate;
	}

    public void drawTable(Graphics g, BufferedImage img) {
    	Graphics2D g2d = (Graphics2D) g.create();
    	
    	if (this.x < 0) {
	 		this.x = 0;
	 	}
	 	if (this.y < 0) {
	 		this.y = 0;
	 	}
	 	if (this.x + this.getWidth() > img.getWidth()) {
	 		this.x = (int) (img.getWidth() - this.getWidth());
	 	}
	 	if (this.y + this.getWidth() > img.getWidth()) {
	 		this.y = (int) (img.getHeight() -  this.getHeight());
	 	}
	 	
	 	g2d.setBackground(color);
    	g2d.setColor(color);
	 	g2d.rotate(Math.toRadians(this.getRotate()), this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
 		g2d.fillRect(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
	 	g2d.setColor(TEXT_COLOR);
 		g2d.drawString(String.valueOf(this.value), (int) (this.getX()+this.getWidth()/2), (int) (this.getY()+this.getHeight()/2));
 		g2d.draw(this);
    }
	
    public void save(Connection connection) {
    	if (!isInDb) {
    		try {
	    		PreparedStatement statement = connection.prepareStatement(SELECT_MAX_ID);
				ResultSet resultSet = statement.executeQuery();
	    		
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TO_TABLEDETAILS);
				
				preparedStatement.setInt(1, resultSet.getInt(1));
				preparedStatement.setString(2, "table");
				preparedStatement.setInt(3, Integer.parseInt(value));
				preparedStatement.setString(4, "");
				preparedStatement.setString(5, createdDate);
				preparedStatement.setInt(6, Integer.parseInt(floorId));
				
				
				int rows = preparedStatement.executeUpdate();
//				System.out.println(rows + " rows inserted into TABLEDETAILS table");
				
				isInDb = true;
				
				preparedStatement.close();
			} catch (SQLException e) {
				
				System.err.println("SQLException in class: Tables.java at method: save()");
				e.printStackTrace();
			}
    	} else {
//    		System.out.println("Table with value: " + value + " and floorid: " + floorId + " is in DB.");
    	}
    }
    
    public void delete(Connection connection) {
    	try (PreparedStatement pr = connection.prepareStatement(DELETE_TO_TABLEDETAILS)) {
    		pr.setInt(1, Integer.parseInt(value));
    		pr.setInt(2, Integer.parseInt(floorId));
    		pr.executeUpdate();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void update(Connection connection) {
    	try (PreparedStatement pr = connection.prepareStatement(UPDATE_TO_TABLEDETAILS)) {
    		pr.setInt(1, Integer.parseInt(value));
    		pr.setInt(2, Integer.parseInt(previousValue));
    		pr.setInt(3, Integer.parseInt(floorId));
    		pr.executeUpdate();
    		this.previousValue = value;
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void changeColor() {
    	if (this.color == Color.BLACK) {
    		this.color = Color.YELLOW;
    	} else {
    		this.color = Color.BLACK;
    	}
    }
    
    public void fixValues() {
    	this.previousValue = value;
    }
    
	public String getFloorId() {
		return floorId;
	}
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}
	public int getRotate() {
		return rotate;
	}
	public void setRotate(int rotate) {
		this.rotate = rotate;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.previousValue = this.value;
		this.value = value;	
	}
	public boolean isInDbLocationTable() {
		return isInDbLocationTable;
	}
	public void setInDbLocationTable(boolean isInDbLocationTable) {
		this.isInDbLocationTable = isInDbLocationTable;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		this.createdDate = sdf.format(date);
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		this.lastModifiedDate = sdf.format(date);
	}
	public String getPreviousValue() {
		return previousValue;
	}
	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}
}
