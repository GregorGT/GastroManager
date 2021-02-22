import java.util.HashMap;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class GMTreeItem extends TreeItem {
	
	
	public String m_name = new String();
	public String m_value = new String();
	public String m_xmlname = new String(); 
	public HashMap < String, String > m_attributes = new HashMap<String, String>();
	
	public GMTreeItem(Tree parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(TreeItem parentItem, int style) {
		super(parentItem, style);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(Tree parent, int style, int index) {
		super(parent, style, index);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(TreeItem parentItem, int style, int index) {
		super(parentItem, style, index);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void checkSubclass() {
	    //  allow subclass
	}
	
	String getDisplayString() {
		
		String result = new String();
		
		if ( m_name.length() > 0 )  
			result = m_name;
		else if (m_attributes.containsKey("name") == true)
			result = m_attributes.getOrDefault("name", result);
		
		else if ( m_xmlname.length() > 0) 
			result = m_xmlname;
		
		else if ( m_value.length() > 0 ) {
			result += "=";
			result += m_value;
		}
		
		//System.out.println(m_attributes.containsKey("name"));
		
		return result;
		
	}

	public void addListener(int menudetect, Listener listener) {
		// TODO Auto-generated method stub
		
	}

	public int getSelectionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMenu(Menu menu_3) {
		// TODO Auto-generated method stub
		
	}

}
