package net.bioclipse.plugins.wizards;

import java.util.ArrayList;
import java.util.Iterator;

import net.bioclipse.model.BioResource;
import net.bioclipse.views.BioResourceLabelProvider;
import net.bioclipse.views.BioResourceView;
import net.bioclipse.wizards.SpectrumResourceContentProvider;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class AddSpectrumWizardPage extends WizardPage {

	private Text text;
	private ArrayList spectra = new ArrayList();
	
	protected AddSpectrumWizardPage() {
		super("Add Spectra");
		setTitle("New Medea - AddSpectrum Wizard");
		setDescription("This wizard lets you add spectra to acquire new reaction predictions");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		BioResourceView resView = (BioResourceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(BioResourceView.ID);
		final TreeViewer treeViewer = new TreeViewer(container);
		treeViewer.setContentProvider(new SpectrumResourceContentProvider());
		treeViewer.setLabelProvider(new DecoratingLabelProvider(
                new BioResourceLabelProvider(),  PlatformUI
                        .getWorkbench().getDecoratorManager()
                        .getLabelDecorator()));		
		treeViewer.setUseHashlookup(true);
		
		//Layout the tree viewer below the text field
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
					Object element = ((IStructuredSelection) sel).getFirstElement();
					if (element instanceof BioResource) {
						BioResource bioRes = (BioResource) element;
						if (!bioRes.isFolder()) {
							spectra.add(bioRes);
							setErrorMessage(null);
							setPageComplete(true);
						}
						else {
							setErrorMessage("Please select a Molecule containing Resource!");
							setPageComplete(false);
						}
					}
				}
			}
			
		});
		treeViewer.setSelection(new StructuredSelection(resView.getRootFolder().getChildren().get(0)));
		setControl(container);
	}

	public ArrayList getSpectra() {
		return spectra;
	}



}
