package net.bioclipse.plugins.wizards;

import java.util.HashMap;

import net.bioclipse.model.BioResource;
import net.bioclipse.views.BioResourceLabelProvider;
import net.bioclipse.views.BioResourceView;
import net.bioclipse.views.FolderResourceContentProvider;

import org.eclipse.jface.dialogs.IDialogPage;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
/**
 * 
 * @author Miguel Rojas
 */
@SuppressWarnings("unchecked")
public class SaveSpectrumWizardPage extends WizardPage {
	
	public static final String CML = "Chemical Markup Language";
	public static final String JCAMP = "Jcamp-dx";

	private Text dirText;
	protected BioResource selectedFolder;
	private Text fileText;
	private Text txtExtension;
	private List list;

	private boolean saveSpectrum = true;
	private Button buttonSavS;
	
	protected String selectedFormat;
	private HashMap spectrumFormat2ExtensionMap = new HashMap();

	{
		spectrumFormat2ExtensionMap.put(CML, ".cml");
		spectrumFormat2ExtensionMap.put(JCAMP, ".jdx");
	}

	/**
	 * Constructor for SpectrumWizardPage object.
	 * 
	 */
	public SaveSpectrumWizardPage() {
		super("SpectrumWizardPage");
		setTitle("Predicted Mass Spectrum");
		setDescription("This wizard saves the predicte mass spectrum obtained from " +
				"MEDEA System given a compound.");
	}
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
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

		txtExtension = new Text(container, SWT.BORDER);
		txtExtension.setBounds(260, 25, 50, 25);
		txtExtension.setEnabled(false);

		label = new Label(container, SWT.NONE);
		label.setText("Supported formats:");

		list = new List(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		list.setLayoutData(gd);
		list.add(CML);
		list.add(JCAMP);
		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] plist = list.getSelection();
				if (plist != null) {
					selectedFormat = plist[0];
					txtExtension.setText((String) spectrumFormat2ExtensionMap.get(selectedFormat));
				}
			}
		});
		list.select(0);
		String[] plist = list.getSelection();
		if (plist != null) {
			selectedFormat = plist[0];
			txtExtension.setText((String) spectrumFormat2ExtensionMap.get(selectedFormat));
		}
		dialogChanged();
		setControl(container);
		
		Label textQuestion = new Label(container, SWT.LEFT);
		textQuestion.setText("Would you like to save the spectrum?");
	    Composite yesNo1 = new Composite(container, SWT.NONE);
	    yesNo1.setLayout(new FillLayout(SWT.VERTICAL));

	    buttonSavS = new Button(yesNo1, SWT.CHECK);
	    buttonSavS.setSelection(true);
	    buttonSavS.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
	    		if(buttonSavS.getSelection()){
	    			saveSpectrum = true;
	    			fileText.setEditable(true);
	    			fileText.setEnabled(true);
	    			dirText.setEditable(true);
	    			dirText.setEnabled(true);
	    			if(fileText.getText().length() > 0)
	    				setPageComplete(true);
	    			else
	    				setPageComplete(false);
	    			dialogChanged();
	    		}else{
	    			saveSpectrum = false;
	    			fileText.setEditable(false);
	    			fileText.setEnabled(false);
	    			dirText.setEditable(false);
	    			dirText.setEnabled(false);
	    		    setPageComplete(true);
	    			dialogChanged();
	    		}
	          }
	          public void widgetDefaultSelected(SelectionEvent e) {
	          }
	    });
	}

//	/**
//	 * Uses the standard container selection dialog to choose the new value for
//	 * the container field.
//	 */
//
//	private void handleBrowse() {
//		DirectoryDialog dirDial = new DirectoryDialog(this.getShell());
//		dirText.setText(dirDial.open());
//	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {
		String fileName = getFileName();
		String dirStr = getPathStr();

		if(!saveSpectrum){
			updateStatus(null);
			return;
		}
		
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

	public String getExtension() {
		return txtExtension.getText();
	}

	public String getCompleteFileName() {
		String path = this.getPathStr();
		String fileName = this.getFileName();
		String extension = this.getExtension();
		String completePath = path + System.getProperty("file.separator")
				+ fileName + extension;
		return completePath;
	}

	public String getSelectedFormat() {
		return selectedFormat;
	}

	public BioResource getSelectedFolder() {
		return selectedFolder;
	}
	
	/**
	 * 
	 * @return True, if the spectrum must be save
	 */
	public boolean saveSpectrum() {
		return saveSpectrum;
	}
}