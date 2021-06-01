package com.gastromanager.mainwindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Tables extends Rectangle {
	
	private int rotate;
	private Color color;
	
	public Tables(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.rotate = 0;
		this.color = Color.BLACK;
	}
	

    public void drawTable(Graphics g, BufferedImage img) {
    	Graphics2D g2d = (Graphics2D) g.create();
    	g2d.setBackground(this.getColor());
	 	g2d.setColor(this.getColor());
	 	
	 	
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
 		
	 	g2d.rotate(Math.toRadians(this.getRotate()), this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2);
 		
 		g2d.fillRect(this.x, this.y, (int)this.getWidth(), (int)this.getHeight());
 		g2d.draw(this);
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
}
