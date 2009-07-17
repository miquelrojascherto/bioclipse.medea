package net.bioclipse.plugins.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * A perspective that consists of views and editors relevant to sequence analysis and visualization
 * 
 * @author Miguel Rojas
 *
 */
public class MedeaPerspective implements IPerspectiveFactory {

	
	public static final String ID_PERSPECTIVE =  "net.bioclipse.plugins.medea.perspective";

	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
	    defineLayout(layout); 



	}

	private void defineLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		
	}

	private void defineActions(IPageLayout layout) {

		String editorArea = layout.getEditorArea();
        
        IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, 0.40f, editorArea);
        top.addView("net.bioclipse.plugins.bc_spectrum.views.PeakSpectrumView");
        IFolderLayout topLeft = layout.createFolder("t_left", IPageLayout.LEFT, 0.20f, "top");
        topLeft.addView("net.bioclipse.views.BioResourceView");
        IFolderLayout topRight = layout.createFolder("t_right", IPageLayout.RIGHT, 0.65f, "top");
        topRight.addView("net.bioclipse.plugins.bc_spectrum.views.peakTable.PeakView");
        
//        IFolderLayout middLeft = layout.createFolder("m_left", IPageLayout.RIGHT, 0.20f, editorArea);
        
        IFolderLayout middRight = layout.createFolder("m_left",IPageLayout.RIGHT, 0.70f, editorArea);
        middRight.addView(IPageLayout.ID_OUTLINE);
        middRight.addView(IPageLayout.ID_PROP_SHEET);
        
	}
	

}

