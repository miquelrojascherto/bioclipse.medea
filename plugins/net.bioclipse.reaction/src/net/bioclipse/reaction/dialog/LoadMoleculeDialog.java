package net.bioclipse.reaction.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
 
/**
 * @author Miguel Rojas
 */
public class LoadMoleculeDialog extends TitleAreaDialog {

	protected Button[] radios;
	private int actionTODO = 0;
	public static int ACTION_OPEN_WIZARD = 0;
	public static int ACTION_OPEN_JCP = 1;
	/**
	 * Constructor of the LoadMoleculeDialog object
	 * 
	 * @param activeShell The Shell object
	 */
	public LoadMoleculeDialog(Shell activeShell) {
		super(activeShell);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		setTitle("Load molecule");
		setMessage("Please select how you would like to load a molecule");
		
		Listener radioGroup = new Listener () {
		     public void handleEvent (Event event) {
		    	 System.out.println("changing");
		    	 if(radios[0].getSelection())
		    		 actionTODO = ACTION_OPEN_WIZARD;
		    	 else
		    		 actionTODO = ACTION_OPEN_JCP;
		     }
		};		
		radios = new Button[2];
		 
        radios[0] = new Button(parent, SWT.RADIO);
	    radios[0].setSelection(true);
	    radios[0].setText("Choice to load a molecule with the wizard");
	    radios[0].addListener (SWT.Selection, radioGroup);
	 
	    radios[1] = new Button(parent, SWT.RADIO);
	    radios[1].setText("Choice to create a molecule with JChemPaint editor");
	    radios[1].addListener (SWT.Selection, radioGroup);

		return area;
	}
	/**
	 * get the action to do
	 * 
	 * @return the action
	 */
	public int getAction(){
		return actionTODO; 
	}


}
