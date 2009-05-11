package net.bioclipse.plugins.bc_reaction.perspective;

import net.bioclipse.plugins.Bc_reactionPlugin;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 
 * @author Miguel Rojas
 */
public class ReactionPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	final static Bc_reactionPlugin PLUGIN = Bc_reactionPlugin.getDefault();
	private int palettePrefer;
	private int perspectPrefer;
	

	/**
	 * The constructor of the ReactionPreferencePage object
	 */
	public ReactionPreferencePage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setTitle("Reaction Plugin");
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(data);
		Label label = new Label(composite, SWT.NONE);
		label.setText(Bc_reactionPlugin.OPEN_PERSP_FOR_RES+ "?:");
		
		final Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setBounds(50, 50, 150, 65);
	    combo.setItems(new String[] {"Ask", "Yes", "No"});
	    combo.select(PLUGIN.getOpenPerspectivePreference());
	    combo.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
	    		if(combo.getSelectionIndex() == Bc_reactionPlugin.TRUE)
	    			perspectPrefer = Bc_reactionPlugin.TRUE;
	    		else if(combo.getSelectionIndex() == Bc_reactionPlugin.FALSE)
	    			perspectPrefer = Bc_reactionPlugin.FALSE;
	    		else if(combo.getSelectionIndex() == Bc_reactionPlugin.UNSET)
	    			perspectPrefer = Bc_reactionPlugin.UNSET;
	          }

	          public void widgetDefaultSelected(SelectionEvent e) {
	          }
	    });
	    
	    
	    
		final Button check = new Button(composite, SWT.CHECK);
	    check.setText("Show the palette");
	    
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 3;
	    check.setLayoutData(gridData);
	    
	    
	    if(PLUGIN.getPalettePreference() == Bc_reactionPlugin.TRUE)
	    	check.setSelection(true);
	    else
	    	check.setSelection(false);
	    
	    check.addSelectionListener(new SelectionListener(){

			public void widgetSelected(SelectionEvent e) {
	    		if(check.getSelection())
	    			palettePrefer = Bc_reactionPlugin.TRUE;
	    		else
	    			palettePrefer = Bc_reactionPlugin.FALSE;
	          }

	          public void widgetDefaultSelected(SelectionEvent e) {
	          }
	    });
	    
	    final Button check2 = new Button(composite, SWT.CHECK);
	    check2.setText("Show tabular");
    	check2.setSelection(true);
    	check2.setLayoutData(gridData);
	    
		return composite;
	}
	

	/*
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply() {
		PLUGIN.setPalettePreference(palettePrefer);
		PLUGIN.setOpenPerspectivePreference(perspectPrefer);
		super.performApply();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		PLUGIN.setPalettePreference(palettePrefer);
		PLUGIN.setOpenPerspectivePreference(perspectPrefer);
		return super.performOk();
	}

}
