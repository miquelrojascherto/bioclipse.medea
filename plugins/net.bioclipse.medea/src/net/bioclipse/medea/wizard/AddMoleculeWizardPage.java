package net.bioclipse.medea.wizard;

import net.bioclipse.chemoinformatics.contentlabelproviders.MoleculeFileContentProvider;
import net.bioclipse.ui.contentlabelproviders.FolderLabelProvider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

public class AddMoleculeWizardPage extends WizardPage {

	private IResource selectedFile = null;
	private Button emptyMolButton;

	protected AddMoleculeWizardPage() {
		super("Add Molecule");
		setTitle("New Medea - AddMolecule Wizard");
		setDescription("This wizard lets you add a Molecule to acquire new reaction predictions");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		TreeViewer treeViewer = new TreeViewer(container);
		treeViewer.setContentProvider(new MoleculeFileContentProvider());
		treeViewer.setLabelProvider(new DecoratingLabelProvider(
				new FolderLabelProvider(), PlatformUI.getWorkbench()
				.getDecoratorManager().getLabelDecorator()));
		treeViewer.setUseHashlookup(true);
		
		//Layout the tree viewer below the text field
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		layoutData.horizontalSpan = 3;
		treeViewer.getControl().setLayoutData(layoutData);
		treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().findMember("."));
		treeViewer.expandToLevel(2);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection sel = event.getSelection();
				if (sel instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) sel)
					.getFirstElement();
					if (element instanceof IFile) {
						selectedFile = (IFile) element;
						setErrorMessage(null);
						setPageComplete(true);
					} else if(element instanceof IProject){
						setErrorMessage("Please select a Molecule containing Resource!");
						setPageComplete(false);
					}
				}
			}
			
		});
		treeViewer.setSelection(new StructuredSelection(ResourcesPlugin.getWorkspace().getRoot().findMember(".")));
		
		// add possibility to add an empty molecule
		emptyMolButton = new Button(container, SWT.CHECK);
		emptyMolButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				if (button.getSelection()) {
					setErrorMessage(null);
					setPageComplete(true);
				}
				else {
					setErrorMessage("Please select a Molecule containing Resource!");
					setPageComplete(false);
				}
				
			}
			
		});
		Label emptyMolLabel = new Label(container, SWT.NULL);
		emptyMolLabel.setText("Add empty Molecule");

		setControl(container);
	}

	public IResource getSelectedRes() {
		//if add empty molecule is checked return null, else return the selected moleculeRes
		if (!emptyMolButton.getSelection()) {
			return this.selectedFile;
		}
		else {
			return null;
		}
	}

}
