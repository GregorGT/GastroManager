import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class OrderingMenuDropdown extends CCombo {
	
	public void init(OrderingMenuDropdown dropdown, GMTreeItem gmtree) {
		
//		GMTreeItem orderRoot = new GMTreeItem(gmtree, SWT.NONE);
//		orderRoot.setText("Orders");
		GMTreeItem newNode = new GMTreeItem(gmtree, SWT.NONE);
		
		
		for (int i = 0; i < gmtree.getItemCount(); i++) {
			dropdown.add(gmtree.getItem(i).getText());
		}
		
		
//		dropdown.add("A");
//		dropdown.add("B");
//		dropdown.add("C");
//		dropdown.add("D");
	}	
	
	public void addItemsFromTree() {
		
	}
	
	public void addItemstoTree() {
		
	}
	
	protected void checkSubclass() {
	    //  allow subclass
	}
	
	public OrderingMenuDropdown(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

}
