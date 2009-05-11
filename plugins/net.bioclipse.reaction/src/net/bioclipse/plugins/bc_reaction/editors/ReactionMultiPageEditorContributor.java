package net.bioclipse.plugins.bc_reaction.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 * 
 * @author Miguel Rojas
 */
public class ReactionMultiPageEditorContributor extends ActionBarContributor {
	/**
	 * Creates a multi-page contributor.
	 */
	public ReactionMultiPageEditorContributor() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		
		addRetargetAction(new DeleteRetargetAction());
		
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {

	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@SuppressWarnings("deprecation")
	public void contributeToToolBar(IToolBarManager toolBarManager) {
	   toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.DELETE));
	   
	   toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
	   toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

	   toolBarManager.add(new ZoomComboContributionItem(getPage()));
	   
	   /* double toolBarManager*/
//	   toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.UNDO));
//	   toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.REDO));
	}
	
}
