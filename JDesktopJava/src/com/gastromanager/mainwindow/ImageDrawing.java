package com.gastromanager.mainwindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public class ImageDrawing extends JPanel implements MouseMotionListener{

	private BufferedImage img = null;
	private String pathToImage;
	
	private static final int recW = 20;
    private static final int MAX = 200;
    private Tables[] tables = new Tables[MAX];
    private int numOfRecs = 0;
    private int currentSquareIndex = -1;
    private int movableRectangle = -1;
    private boolean rotate = false;
    private boolean resize = false;
    private boolean activate  = false;
    
    
	public ImageDrawing(String pathToImage) {
		this.pathToImage = pathToImage;
		
		 try {
             img = ImageIO.read(new FileInputStream(pathToImage));
         } catch (IOException ex) {
             ex.printStackTrace();
         }
		 
		 this.addComponentListener(new ComponentAdapter() {
	           public void componentResized(ComponentEvent e) {
	        	   Component c = e.getComponent();	        	   
	           }
	     });
		 
		 
		 addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent evt) {

					int x = evt.getX();
				    int y = evt.getY();
				    currentSquareIndex = getRec(x, y);

				    if (currentSquareIndex < 0) {// not inside a square
				    	return;
				    }

				    if (SwingUtilities.isRightMouseButton(evt)) {
				    	doPop(evt);
				    }
				}
		 
				@Override
				public void mouseClicked(MouseEvent evt) {
					int x = evt.getX();
				    int y = evt.getY();
//				    
				    if (evt.getClickCount() >= 2) {
//				    	remove(currentSquareIndex);
				    }
		 		}
				
				private void doPop(MouseEvent e) {
			        PopUpDemo menu = new PopUpDemo();
			        menu.show(e.getComponent(), e.getX(), e.getY());
			    }
    	});
		 
    	addMouseMotionListener(this);
	}	
	
	@Override
    public Dimension getPreferredSize() {
        return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
    }

	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		if (rotate || resize || activate) {
			repaint();
			revalidate();	
			rotate = resize = activate = false;
		}
		
    	Graphics2D g2d = (Graphics2D) g.create();
        if (img != null) {
//            g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
            g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
        }
   	
        this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

        for (int i=0; i<numOfRecs; ++i) {
        	tables[i].drawTable(g2d, img);
        }

	 	 g2d.dispose();
	}
	
    public int getRec(int x, int y) {
    	for (int i = 0; i < numOfRecs; i++) {
			if (tables[i].contains(x, y)) {
			    return i;
			}
		}
    	return -1;
    }
    
    public void add(int x, int y) {
    	if (numOfRecs < MAX) {
    		tables[numOfRecs] = new Tables(x, y, recW, recW);
			currentSquareIndex = numOfRecs;
			numOfRecs++;
			repaint();
		}
    }
	
    @Override
    public void remove(int n) {
		 if (n < 0 || n >= numOfRecs) {
		 	  return;
		 }
		 
		  numOfRecs--;
		  tables[n] = tables[numOfRecs];
		  if (currentSquareIndex == n) {
			  currentSquareIndex = -1;
		  }
 
		  repaint();
    }

	@Override
	public void mouseDragged(MouseEvent event) {
		  int x = event.getX();
		  int y = event.getY();
		 
		  if (currentSquareIndex >= 0 && movableRectangle == currentSquareIndex) {
			  	tables[currentSquareIndex].x = x;
			  	tables[currentSquareIndex].y = y;
			  	repaint();
		 }
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		 int x = event.getX();
  		 int y = event.getY();
  		 
  		 if (getRec(x, y) >= 0) {
  			  setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
  		 } else {
  			  setCursor(Cursor.getDefaultCursor());
  		 }
	}
	
	
	 
	public void addTable() {
		this.add(0, 0);
	}
	
	 private class PopUpDemo extends JPopupMenu {
  		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JMenuItem anItem;
	    private ArrayList<JMenuItem> allItems;

	    public PopUpDemo() {
	    	allItems = new ArrayList<>();
	        anItem = new JMenuItem("Move");
	        allItems.add(anItem);
	        anItem = new JMenuItem("Rotate");
	        allItems.add(anItem);
	        anItem = new JMenuItem("Resize");
	        allItems.add(anItem);
	        anItem = new JMenuItem("Activate");
	        allItems.add(anItem);
	        
	        for (JMenuItem m: allItems) {
	        	this.add(m);
	        	m.addActionListener(new ActionListener() {
	        		@Override
					public void actionPerformed(ActionEvent arg0) {
						if (m.getText().equals("Move")) {
							movableRectangle = currentSquareIndex;
						} else if (m.getText().equals("Rotate")) {
							tables[currentSquareIndex].setRotate(tables[currentSquareIndex].getRotate()+45);
							rotate = true;
						} else if (m.getText().equals("Resize")) {
							tables[currentSquareIndex].setSize(50, 20);
							resize = true;
						} else if (m.getText().equals("Activate")) {
							tables[currentSquareIndex].setColor(Color.YELLOW);
							activate = true;
						}
	        		}
	        	});
	        }
	    }
	}
}

