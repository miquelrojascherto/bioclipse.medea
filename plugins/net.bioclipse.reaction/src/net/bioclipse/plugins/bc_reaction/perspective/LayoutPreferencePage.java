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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Miguel Rojas
 */
public class LayoutPreferencePage extends PreferencePage implements	IWorkbenchPreferencePage {

	final static Bc_reactionPlugin PLUGIN = Bc_reactionPlugin.getDefault();
	private Button[] radios;
	private Listener radioGroup;
    private int layoutType;
	private Group group1;
	private int layoutPref;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setTitle("Layout");
		

		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Current Layout:");
		
		final Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setBounds(50, 50, 150, 65);
	    String items[] = {"Hierarchic VERTICAL Layout","Hierarchic HORIZONTAL Layout"};
	    combo.setItems(items);
	    combo.addSelectionListener(new SelectionListener(){
	    	public void widgetSelected(SelectionEvent e) {
	    		if(combo.getSelectionIndex() == 0)
	    			createHV();
	    		else if (combo.getSelectionIndex() == 1)
	    			createHH();
	          }

			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
	    });
	    
	    
	    if(PLUGIN.getLayoutPreference() < 10)
	    	layoutPref = 0;
	    else if(PLUGIN.getLayoutPreference() < 20)
	    	layoutPref = 1;
    	combo.select(layoutPref);
	    
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gd);
		
		group1 = new Group(composite, SWT.NONE);
		group1.setText("Layout position");    
	    layout = new GridLayout();
	    layout.numColumns = 3;
	    group1.setLayout(layout);
	    
	    
	    radioGroup = new Listener () {

			@SuppressWarnings("static-access")
			public void handleEvent (Event event) {
				if(layoutPref == 0){
					if(radios[0].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_VERT_LEFT;
			    	 else if(radios[1].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_VERT_CENTER;
			    	 else if(radios[2].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_VERT_RIGHT;
				}else if(layoutPref == 1){
			    	 if(radios[0].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_HOR_UP;
			    	 else if(radios[1].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_HOR_CENTER;
			    	 else if(radios[2].getSelection())
			    		 layoutType = PLUGIN.HIERARCHIC_HOR_DOWN;
				}
		    		 
		     }
		};	
		
	    if(layoutPref == 0)
	    	createHV();
	    else if(layoutPref == 1)
	    	createHH();
	    
	    Group group2 = new Group(composite, SWT.NONE);
	    group2.setText("Size Combo");    
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    group2.setLayout(layout);
		
	    Button[] radios = new Button[2];
	    radios[0] = new Button(group2, SWT.RADIO);
		radios[0].addListener (SWT.Selection, radioGroup);
	    radios[0].setText("A                           ");
    	radios[0].setSelection(true);	 
	    radios[1] = new Button(group2, SWT.RADIO);
		radios[1].addListener (SWT.Selection, radioGroup);
	    radios[1].setText("B                           ");
	    
	    
		return composite;
	}

	/**
	 * create the radios for Layout Vertical
	 */
	private void createHV() {
		layoutPref = 0;
		if(group1.getChildren().length == 0){
			radios = new Button[3];
			for(int i = 0 ; i < 3 ; i++){
				radios[i] = new Button(group1, SWT.RADIO);
				radios[i].addListener (SWT.Selection, radioGroup);
			}
		}else {
			for(int i = 0 ; i < 3 ; i++)
				radios[i] = (Button)group1.getChildren()[i];
		}
		
	    radios[0].setText("Left          ");
	    radios[1].setText("Center        ");
	    radios[2].setText("Right         ");

	    if(PLUGIN.getLayoutPreference() == 0){
	    	radios[0].setSelection(true);	 
	    	radios[1].setSelection(false);	 
	    	radios[2].setSelection(false);	 
	    }
	    else if(PLUGIN.getLayoutPreference() == 1){
	    	radios[0].setSelection(false);	 
	    	radios[1].setSelection(true);	
	    	radios[2].setSelection(false);	  
	    }
	    else if(PLUGIN.getLayoutPreference() == 2){
	    	radios[0].setSelection(false);	
	    	radios[1].setSelection(false);	 
	    	radios[2].setSelection(true);
	    }
	    
	    group1.redraw();
	}

	/**
	 * create the radios for Layout Horizontal
	 */
	private void createHH() {
		layoutPref = 1;
		if(group1.getChildren().length == 0){
			radios = new Button[3];
			for(int i = 0 ; i < 3 ; i++){
				radios[i] = new Button(group1, SWT.RADIO);
				radios[i].addListener (SWT.Selection, radioGroup);
			}
		}else {
			for(int i = 0 ; i < 3 ; i++)
				radios[i] = (Button)group1.getChildren()[i];
		}
		
	    radios[0].setText("Up            ");
	    radios[1].setText("Center        ");
	    radios[2].setText("Down          ");

	    if(PLUGIN.getLayoutPreference() == 10){
	    	radios[0].setSelection(true);	 
	    	radios[1].setSelection(false);	 
	    	radios[2].setSelection(false);	 
	    }
	    else if(PLUGIN.getLayoutPreference() == 11){
	    	radios[0].setSelection(false);	 
	    	radios[1].setSelection(true);	
	    	radios[2].setSelection(false);	  
	    }
	    else if(PLUGIN.getLayoutPreference() == 12){
	    	radios[0].setSelection(false);	
	    	radios[1].setSelection(false);	 
	    	radios[2].setSelection(true);
	    }

	    
	    group1.redraw();
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply() {
		PLUGIN.setLayoutPreference(layoutType);
		super.performApply();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		
		PLUGIN.setLayoutPreference(layoutType);
		return super.performOk();
	}
	
}
