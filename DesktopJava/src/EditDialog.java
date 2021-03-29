import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Attr;

public class EditDialog extends Dialog {

	String m_sValue;
	String m_sNewValue;
	
	public void open(GMTreeItem item, String attribute) {
	    Shell parent = getParent();
	    final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	    shell.setText("Edit value: " + attribute);
	    shell.setLayout(new GridLayout(2, true));
	    
	    Label label = new Label(shell, SWT.NULL);
	    label.setText("Please enter the new value: ");
	    
	    final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);

	    final Button buttonOK = new Button(shell, SWT.PUSH);
	    buttonOK.setText("Ok");
	    buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	    Button buttonCancel = new Button(shell, SWT.PUSH);
	    buttonCancel.setText("Cancel");

	    text.addListener(SWT.Modify, new Listener() {
	      public void handleEvent(Event event) {
	        try {
	          m_sNewValue = new String(text.getText());
	          buttonOK.setEnabled(true);
	        } catch (Exception e) {
	          buttonOK.setEnabled(false);
	        }
	      }
	    });

	    buttonOK.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	    	  item.m_attributes.replace(attribute, m_sNewValue);
	    	  
	    	  if (attribute == "name") 
	    		    item.setText(m_sNewValue);
	    	  
	    	  shell.dispose();
	      }
	    });

	    buttonCancel.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        m_sValue = null;
	        shell.dispose();
	      }
	    });
	    
	    shell.addListener(SWT.Traverse, new Listener() {
	      public void handleEvent(Event event) {
	        if(event.detail == SWT.TRAVERSE_ESCAPE)
	          event.doit = false;
	      }
	    });

	   
	    
	    shell.pack();
	    shell.open();

	    Display display = parent.getDisplay();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }

	    //return newValue;
	  }
	
//	void openEditDialog(GMTreeItem selectedItem, String attribute, Shell shell) {
//		
//		EditDialog d = new EditDialog(shell);
//		d.open(selectedItem, attribute);
//		
//	}
	
	
	public EditDialog(Shell parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Shell parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

}
