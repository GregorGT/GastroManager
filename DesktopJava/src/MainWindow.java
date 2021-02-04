import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
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

public class MainWindow {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private SashForm sashForm;
	private TabFolder tabFolder;
	private TabItem tbtmView;
	private TabItem tbtmNewItem;

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
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		sashForm = new SashForm(shell, SWT.NONE);
		
		TreeViewer treeViewer = new TreeViewer(sashForm, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		tabFolder = new TabFolder(sashForm, SWT.NONE);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		sashForm.setWeights(new int[] {1, 1});
		m_bindingContext = initDataBindings();

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
