import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DrillDownGroup extends Group {
	
	public DrillDownGroup m_group;
	public boolean m_bActivationTracker;
	
	public void drag(DrillDownGroup a) {
		Point originalPoint = new Point(a.getBounds().x, a.getBounds().y);
		System.out.println("ORIGINAL POINT: " + originalPoint.x + " - " + originalPoint.y);
	}
	
	public void drop(DrillDownGroup a, Point p) {
		a.setBounds(p.x, p.y, a.getBounds().width, a.getBounds().height);
	}
	
	
	
	public void addNewDrillDownGroup(int height, int width, String name, Composite comp) {
		
		m_group = new DrillDownGroup(comp, SWT.NONE);
		m_group.setText(name);
		m_group.setLayout(new GridLayout(1,  true));
		m_group.setBounds(comp.getBounds().x, comp.getBounds().y + 80, width, height);
		RClickMenu drillDownMenu = new RClickMenu(m_group);
		m_group.setMenu(drillDownMenu);
		
		
		m_group.addListener(SWT.Selection, new Listener() {
			@Override 
			public void handleEvent(Event arg0) {
				System.out.println("Group selected: " + m_group.getText());	
			}
		});
		
		m_group.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event e) {
				
				Color enterMouseColor = new Color(100,100,100);
				m_group.setBackground(enterMouseColor);
				m_bActivationTracker = true;
				
			}			
		});
		
		if (m_bActivationTracker == true) {
			m_group.addListener(SWT.MouseDown, new Listener() {
				@Override
				public void handleEvent(Event e) {
					
					m_group.drag(m_group);
					
				}
				
				
			});
			
			m_group.addListener(SWT.MouseUp, new Listener() {
				@Override
				public void handleEvent(Event e) {
					
					Point cursorLocation = Display.getCurrent().getCursorLocation();
					Point relativeCursorLocation = 
					Display.getCurrent().getFocusControl().toControl(cursorLocation);
					m_group.drop(m_group, relativeCursorLocation);
					
				}
				
				
			}); 
			
		}
		
		m_group.addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event e) {
				
				Color exitMouseColor = new Color(200,200,200);
				m_group.setBackground(exitMouseColor);
				m_bActivationTracker = false;
				
			}			
		});
		
		
//		m_group.addListener(SWT.MenuDetect, new Listener() {
//			@Override
//			public void handleEvent(Event arg0) {
//				
//				drillDownMenu.openDrillDownMenu(drillDownMenu, m_group);
//				m_bActivationTracker = true;
//				
//				if (m_bActivationTracker == true) {
//					m_group.addMouseListener(new MouseListener() {
//						
//						@Override
//						public void mouseUp(MouseEvent arg0) {
//							// TODO Auto-generated method stub
//							Point cursorLocation = Display.getCurrent().getCursorLocation();
//							Point relativeCursorLocation = 
//							Display.getCurrent().getFocusControl().toControl(cursorLocation);
//							m_group.drop(m_group, relativeCursorLocation);
//						}
//						
//						@Override
//						public void mouseDown(MouseEvent arg0) {
//							// TODO Auto-generated method stub
//							m_group.drag(m_group);
//						}
//						
//						@Override
//						public void mouseDoubleClick(MouseEvent arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//				}
//				
//			}
//		});
//		
	}
	
	public void addButtonToDrillDown(int height, int width, String name) {
		DrillDownButton btn = new DrillDownButton(m_group, SWT.PUSH); //add 
		btn.init(height, width, name, btn);
		m_group.redraw();
	}
	
	public void activatedDrillDown(DrillDownGroup a) {

		Color aktiv = new Color(0,150,0);
		a.setBackground(aktiv);		
	}
	
	
	
	public DrillDownGroup(Composite parent, int style) {
		super(parent, style);
		
		// TODO Auto-generated constructor stub
	}

	
	protected void checkSubclass() {
	    //  allow subclass
	}
}
