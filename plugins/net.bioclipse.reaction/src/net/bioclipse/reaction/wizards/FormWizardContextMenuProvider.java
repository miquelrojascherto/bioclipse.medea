package net.bioclipse.reaction.wizards;

import org.eclipse.gef.ContextMenuProvider;  
import org.eclipse.gef.EditPartViewer;  
import org.eclipse.gef.ui.actions.ActionRegistry;  
import org.eclipse.gef.ui.actions.GEFActionConstants;  
import org.eclipse.jface.action.IAction;  
import org.eclipse.jface.action.IMenuManager;  
import org.eclipse.ui.actions.ActionFactory;  
  
public class FormWizardContextMenuProvider extends ContextMenuProvider {  
  
    private ActionRegistry actionRegistry;  
      
    public FormWizardContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {  
        super(viewer);  
        setActionRegistry(registry);  
    }  
  
    @Override  
    public void buildContextMenu(IMenuManager menu) {  
        GEFActionConstants.addStandardActionGroups(menu);  
          
        IAction action = getActionRegistry().getAction(ActionFactory.UNDO.getId());  
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);  
          
        action = getActionRegistry().getAction(ActionFactory.REDO.getId());  
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);  
          
        action = getActionRegistry().getAction(ActionFactory.DELETE.getId());  
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);  
    }  
  
    public ActionRegistry getActionRegistry() {  
        return actionRegistry;  
    }  
  
    public void setActionRegistry(ActionRegistry registry) {  
        this.actionRegistry = registry;  
    }  
  
}  
