import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class DrillDownButton extends Button {
	
	public void init(int h, int w, String name, DrillDownButton btn) {
		btn.setText(name);
		btn.setEnabled(false);
		btn.setBounds(5, 25, w, h);
		btn.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				
					btn.addMouseListener(new MouseListener() {
						
						@Override
						public void mouseUp(MouseEvent arg0) {
							// TODO Auto-generated method stub
							Point cursorLocation = Display.getCurrent().getCursorLocation();
							Point relativeCursorLocation = 
								Display.getCurrent().getFocusControl().toControl(cursorLocation);
							btn.drop(btn, relativeCursorLocation);
						}
						
						@Override
						public void mouseDown(MouseEvent arg0) {
							// TODO Auto-generated method stub
							btn.drag(btn);
						}
						
						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
					});
				
				System.out.print(btn.getEnabled());
				
			}
		});
	}
	
	public void drag(DrillDownButton b) {
		Point originalPoint = new Point(b.getBounds().x, b.getBounds().y);
		System.out.println("ORIGINAL POINT: " + originalPoint.x + " - " + originalPoint.y);
	}
	
	public void drop(DrillDownButton b, Point p) {
		
		b.setBounds(p.x, p.y, b.getBounds().height, b.getBounds().width);
		
	}
	public void activatedDrillDown(DrillDownButton b) {
		Color aktiv = new Color(0,255,0);
		b.setBackground(aktiv);		
	}
	
	

	public DrillDownButton(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	protected void checkSubclass() {
	    //  allow subclass
	}
}
