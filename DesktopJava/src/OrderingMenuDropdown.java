import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.forms.widgets.FormToolkit;

public class OrderingMenuDropdown extends CCombo {

	private CCombo dropdown;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());	
	
	public void init(OrderingMenuDropdown dropdown) {
		
				
	}	
	
	public void addItemsFromTree() {
		
	}
	
	protected void checkSubclass() {
	    //  allow subclass
	}
	
	public OrderingMenuDropdown(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

}
