package net.bioclipse.plugins.bc_reaction.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * A perspective that consists of views and editors relevant to sequence
 * analysis and visualization
 * 
 * @author Miguel Rojas
 * 
 */
public class ReactionPerspective implements IPerspectiveFactory {

	IPageLayout storedLayout;

	public static final String ID_PERSPECTIVE = "net.bioclipse.plugins.ReactionPerspective";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
	    defineLayout(layout);
	}
	
	public void defineActions(IPageLayout layout) {
		//		 Add "show views".
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
//		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);	
	}

	public void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		IFolderLayout left = layout.createFolder("left",IPageLayout.LEFT, 0.225f, editorArea);
		left.addView("net.bioclipse.views.BioResourceView");

        IFolderLayout button_left = layout.createFolder("bottom",IPageLayout.BOTTOM,0.50f,"left");
        button_left.addView(IPageLayout.ID_OUTLINE);
        
//        IFolderLayout button = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.70f, editorArea);
//        button.addView(IConsoleConstants.ID_CONSOLE_VIEW);
}
	
	public IPageLayout getStoredLayout() {
		return storedLayout;
	}

}
