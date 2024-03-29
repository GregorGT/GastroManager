/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class ImageDrawing extends JPanel implements MouseMotionListener{
	private static final long serialVersionUID = 1L;
	
	private static final int recW = 30;
    private static final int MAX = 20000;
    
	private BufferedImage img = null;
	private String pathToImage;
	
    private Tables[] tables = new Tables[MAX];
    private int numOfRecs = 0;
    private int currentSquareIndex = -1;
    private int movableRectangle = -1;
    private boolean rotate = false;
    private boolean resize = false;
    private boolean activate  = false;
    private String floorId;
    private Connection connection;
    private LayoutMenu layoutMenu;
    private ArrayList<Tables> deletedTables;
    
	public ImageDrawing(String pathToImage, String floorId, Connection connection, LayoutMenu layoutMenu) {
		this.floorId = floorId;
		this.pathToImage = pathToImage;
		this.connection = connection;
		this.layoutMenu = layoutMenu;
		this.deletedTables = new ArrayList<>();
		
		 try {
             img = ImageIO.read(new FileInputStream(pathToImage));
         } catch (NullPointerException e) {
        	 System.err.println("NULL POINTER EXCEPTION");
         } catch (IOException ex) 
		 	{ } 
		 
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
				    tables[currentSquareIndex].setLastModifiedDate();
				}
		 
				@Override
				public void mouseClicked(MouseEvent evt) {
					int x = evt.getX();
				    int y = evt.getY();
				    
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
		Graphics2D g2d = (Graphics2D) g.create();
		
		if (rotate || resize || activate) {
			repaint();
			revalidate();
			rotate = resize = activate = false;	
		}
		
		if (img != null) {
            g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
        } else {
        	return;
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
    
    public void addTable(String value, boolean isInDb, boolean isInDbLocationTable) {
		this.addTable(0, 0, recW, recW, 0, value, isInDb, isInDbLocationTable);
	}
	
    public void addTable(int x, int y, int width, int height, int rotate, String value, boolean isInDb, boolean isInDbLocationTable) {
    	if (numOfRecs < MAX) {
    		tables[numOfRecs] = new Tables(x, y, width, height, rotate, value, floorId, isInDb, isInDbLocationTable);
			currentSquareIndex = numOfRecs;
			++numOfRecs;
			repaint();
		}
    }

    public void removeTable(int n) {
		 if (n < 0 || n >= numOfRecs) {
		 	  return;
		 }
		 
		 for (int i = n; i < tables.length - 1; ++i) {
			 Tables temp = tables[i];
			 tables[i] = tables[i + 1];
			 tables[i + 1] = temp;
			 if (tables[i] != null) {
				 tables[i].setValue(String.valueOf(Integer.parseInt(tables[i].getValue()) - 1));
			 }
		 }

		 deletedTables.add(tables[tables.length - 1]);
		 
		 tables[tables.length - 1] = null;
		 numOfRecs--;

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
	 
	 private class PopUpDemo extends JPopupMenu {
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
	        anItem = new JMenuItem("Delete");
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
							JTextField height = new JTextField("");
							JTextField width = new JTextField("");
							JPanel pan = new JPanel(new GridLayout(0, 1));
							
					    	pan.add(new JLabel("Width:"));
					    	pan.add(height);
					    	pan.add(new JLabel("Height:"));
					    	pan.add(width);
					    	int result = JOptionPane.showConfirmDialog(null, pan, "Width & Height",
					                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

					    	if (result == JOptionPane.OK_OPTION) {
					    		try {
					    			width.setText(width.getText().trim());
					    			height.setText(height.getText().trim());
					    			if ((Integer.parseInt(width.getText()) > img.getWidth()) || 
					    					(Integer.parseInt(height.getText()) > img.getHeight())) {
					    				return;
					    			}
//					    			tables[currentSquareIndex].setSize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
					    			tables[currentSquareIndex].width = Integer.parseInt(width.getText());
					    			tables[currentSquareIndex].height = Integer.parseInt(height.getText());
					    			resize = true;
					    		} catch (NumberFormatException e) {
					    			e.printStackTrace();
					    		}
				    		} 
						} else if (m.getText().equals("Activate")) {
							tables[currentSquareIndex].changeColor();
							activate = true;
						} else if (m.getText().equals("Delete")) {
							removeTable(currentSquareIndex);
							layoutMenu.removeNodeFromTree(deletedTables.get(deletedTables.size()-1).getValue(), deletedTables.get(deletedTables.size()-1).getFloorId());
						}
	        		}
	        	});
	        }
	    }
	}



	public String getPathToImage() {
		return pathToImage;
	}
	public void setPathToImage(String pathToImage) {
		this.pathToImage = pathToImage;
		try {
			img = ImageIO.read(new FileInputStream(this.pathToImage));
		} catch (NullPointerException e) {
			System.err.println("NULLPOINTEREXCEPTION");
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
		repaint();
		revalidate();
	}
	public Tables[] getTables() {
		return tables;
	}
	public int getNumOfRecs() {
		return numOfRecs;
	}
	public ArrayList<Tables> getDeletedTables() {
		return deletedTables;
	}
	public void setDeletedTables(ArrayList<Tables> deletedTables) {
		this.deletedTables = deletedTables;
	}
}

