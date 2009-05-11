package net.bioclipse.plugins.bc_reaction.wizards;

import net.bioclipse.model.BioResource;
import net.bioclipse.views.BioResourceLabelProvider;
import net.bioclipse.views.BioResourceView;
import net.bioclipse.views.FolderResourceContentProvider;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
/**
 * 
 * @author Miguel Rojas
 */
public class AddNameFileWizardPage extends WizardPage {

	private BioResource selectedRes;
	private Button emptyMolButton;

	private Text dirText;
	protected BioResource selectedFolder;
	private Text fileText;
	
	
	protected AddNameFileWizardPage() {
		super("Add Name File");
		setTitle("New Reaction Wizard");
		setDescription("This wizard lets you add a Reaction to your new Reaction Resource");
	}

	@SuppressWarnings("static-access")
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&File Directory:");

		dirText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		dirText.setLayoutData(gd);
		gd.horizontalSpan = 3;
		dirText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		BioResourceView resView = (BioResourceView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(
						BioResourceView.ID);
		TreeViewer treeViewer = new TreeViewer(container);
		treeViewer.setContentProvider(new FolderResourceContentProvider());
		treeViewer.setLabelProvider(new DecoratingLabelProvider(
				new BioResourceLabelProvider(), PlatformUI.getWorkbench()
						.getDecoratorManager().getLabelDecorator()));
		treeViewer.setUseHashlookup(true);
		
		// Layout the tree viewer below the text field
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		layoutData.horizontalSpan = 3;
		treeViewer.getControl().setLayoutData(layoutData);

		treeViewer.setInput(resView.getInitalInput());

		treeViewer.expandToLevel(2);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection sel = event.getSelection();
				if (sel instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) sel)
							.getFirstElement();
					if (element instanceof BioResource) {
						selectedFolder = (BioResource) element;
						String path = ((BioResource) element).getPath();
						dirText.setText(path);
					}
				}
			}

		});
		treeViewer.setSelection(new StructuredSelection(resView.getRootFolder()
				.getChildren().get(0)));

		label = new Label(container, SWT.NULL);
		label.setText("&File name:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dialogChanged();
		setControl(container);
	}


	public BioResource getSelectedRes() {
		if (!emptyMolButton.getSelection()) {
			return this.selectedRes;
		}
		else {
			return null;
		}
	}
	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {
		String fileName = getFileName();
		String dirStr = getPathStr();

		if (dirStr.length() == 0) {
			updateStatus("Directory must be specified");
			return;
		}

		if (fileName == null || fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getFileName() {
		if (fileText != null) {
			return fileText.getText();
		} else {
			return null;
		}
	}

	public String getPathStr() {
		return dirText.getText();
	}


	public String getCompleteFileName() {
		String path = this.getPathStr();
		String fileName = this.getFileName();
		String completePath = path + System.getProperty("file.separator")
				+ fileName + ".rmr";
		return completePath;
	}

	public BioResource getSelectedFolder() {
		return selectedFolder;
	}

}
