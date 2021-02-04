import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.*;
import org.xml.sax.*;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class MainWindow {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private SashForm sashForm;
	private TabFolder tabFolder;
	private TabItem tbtmView;
	private TabItem tbtmNewItem;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(475, 350);
		shell.setText("GastroManager");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		sashForm = new SashForm(shell, SWT.NONE);
		
		Tree root = new Tree(sashForm, SWT.BORDER);
		formToolkit.adapt(root);
		formToolkit.paintBordersFor(root);
		
		TreeItem trtmRoot = new TreeItem(root, SWT.NONE);
		trtmRoot.setText("root");
		
		tabFolder = new TabFolder(sashForm, SWT.NONE);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		sashForm.setWeights(new int[] {1, 1});
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmLoadFile = new MenuItem(menu, SWT.CASCADE);
		mntmLoadFile.setText("File");
		
		Menu menu_1 = new Menu(mntmLoadFile);
		mntmLoadFile.setMenu(menu_1);
		
		MenuItem mntmOpenFile = new MenuItem(menu_1, SWT.NONE);
		mntmOpenFile.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				 	FileDialog fd = new FileDialog(shell, SWT.OPEN);
			        fd.setText("Open XML file");
			        fd.setFilterPath("C:/");
			        String[] filterExt = { "*.XML", "*.xml"};
			        fd.setFilterExtensions(filterExt);
			        String selected = fd.open();
			        System.out.println(selected);
			}
		});
		mntmOpenFile.setText("Open File");
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.CASCADE);
		mntmAbout.setText("About");
		
		Menu menu_2 = new Menu(mntmAbout);
		mntmAbout.setMenu(menu_2);
		
		m_bindingContext = initDataBindings();

	}
	
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
