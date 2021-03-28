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
	
	public DrillDownGroup group;
	
	
	public void drag(DrillDownGroup a) {
		Point originalPoint = new Point(a.getBounds().x, a.getBounds().y);
		System.out.println("ORIGINAL POINT: " + originalPoint.x + " - " + originalPoint.y);
	}
	
	public void drop(DrillDownGroup a, Point p) {
		a.setBounds(p.x, p.y, a.getBounds().width, a.getBounds().height);
	}
	
	
	
	public void addNewDrillDownGroup(int height, int width, String name, Composite comp) {
		
		group = new DrillDownGroup(comp, SWT.NONE);
		group.setText(name);
		group.setLayout(new GridLayout(1,  true));
//		FormData fd_group = new FormData();
//		group.setLayoutData(fd_group);
		group.setBounds(comp.getBounds().x, comp.getBounds().y + 80, width, height);
		RClickMenu drillDownMenu = new RClickMenu(group);
		group.setMenu(drillDownMenu);
		
		group.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				
				drillDownMenu.openDrillDownMenu(drillDownMenu, group);
				if (group.getEnabled() == true) {
					group.addMouseListener(new MouseListener() {
						
						@Override
						public void mouseUp(MouseEvent arg0) {
							// TODO Auto-generated method stub
							Point cursorLocation = Display.getCurrent().getCursorLocation();
							Point relativeCursorLocation = Display.getCurrent().getFocusControl().toControl(cursorLocation);
							group.drop(group, relativeCursorLocation);
						}
						
						@Override
						public void mouseDown(MouseEvent arg0) {
							// TODO Auto-generated method stub
							group.drag(group);
						}
						
						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
					});
				} else {
					
				}
				
			}
		});
		
	}
	
	public void addButtonToDrillDown(int height, int width, String name) {
		DrillDownButton btn = new DrillDownButton(group, SWT.PUSH); //add 
		btn.init(height, width, name, btn);
		group.redraw();
	}
	
	public void activatedDrillDown(DrillDownGroup a) {

		Color aktiv = new Color(0,255,0);
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
