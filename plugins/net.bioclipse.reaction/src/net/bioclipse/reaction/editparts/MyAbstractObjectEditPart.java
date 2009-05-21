package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeEvent;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.List;

import net.bioclipse.cdk.domain.CDKMolecule;
import net.bioclipse.cdk.domain.CDKMoleculePropertySource;
import net.bioclipse.cdk.domain.CDKReaction;
import net.bioclipse.cdk.domain.CDKReactionPropertySource;
import net.bioclipse.cdk.jchempaint.widgets.JChemPaintEditorWidget;
import net.bioclipse.chemoinformatics.wizards.WizardHelper;
import net.bioclipse.reaction.editpolicies.MyComponentEditPolicy;
import net.bioclipse.reaction.editpolicies.MyDirectEditPolicy;
import net.bioclipse.reaction.editpolicies.MyGraphicalNodeEditPolicy;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;

/**
 * 
 * @author Miguel Rojas
 */
public class MyAbstractObjectEditPart extends EditPartWithListener implements NodeEditPart{
	
	private MyDirectEditManager directManager = null;
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		AbstractObjectModel model = (AbstractObjectModel)getModel();
		
		Label label = new Label();
		label.setText(model.getText());
		label.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(3)));
		label.setBackgroundColor(ColorConstants.orange);
		label.setOpaque(true);
		
		return label;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new MyComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,new MyDirectEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,new MyGraphicalNodeEditPolicy());
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals(){
		Rectangle constraint = ((AbstractObjectModel)getModel()).getConstraint();
		
		((GraphicalEditPart)getParent()).setLayoutConstraint(this,getFigure(),constraint);
	}
	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt){
		if(evt.getPropertyName().equals(AbstractObjectModel.P_CONSTRAINT))
			refreshVisuals();
		else if(evt.getPropertyName().equals(AbstractObjectModel.P_TEXT)){
			Label label = (Label)getFigure();
			label.setText((String)evt.getNewValue());
		}else if(evt.getPropertyName().equals(AbstractObjectModel.P_SOURCE_CONNECTION))
			refreshSourceConnections();
		else if(evt.getPropertyName().equals(AbstractObjectModel.P_TARGET_CONNECTION))
			refreshTargetConnections();
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request req){
		
		/* one-click funcionality - introduction text - show molecule*/
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			this.performDirectEdit(); //-introduction text
		}
		/* double-click funcionality - Opening JCP editor*/
		if(req.getType().equals(RequestConstants.REQ_OPEN)){
			System.out.println("doubles click - opening wizard");
			AbstractObjectModel abstractObject = (AbstractObjectModel)this.getModel();
			if(abstractObject instanceof CompoundObjectModel){
				IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule( ((CompoundObjectModel)abstractObject).getIMolecule());
				if(mol != null && mol.getAtomCount() > 0){
					StringWriter writer = new StringWriter();
					CMLWriter cmlWriter = new CMLWriter(writer);
			        try {
						cmlWriter.write(mol);
					} catch (CDKException e1) {
						e1.printStackTrace();
					}
			        String cmlContent = writer.toString();
			        IEditorDescriptor desc = PlatformUI.getWorkbench().
		            getEditorRegistry().getDefaultEditor(mol.getID()+"_.cml",Platform.getContentTypeManager().getContentType( "net.bioclipse.contenttypes.cml.singleMolecule2d" ));
			        try {
		                IFile tmpFile= net.bioclipse.core.Activator.getVirtualProject().getFile(WizardHelper.findUnusedFileName(new StructuredSelection(net.bioclipse.core.Activator.getVirtualProject()), mol.getID(), ".cml") );
		                tmpFile.create( new StringBufferInputStream(cmlContent), IFile.FORCE, null);
		                IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(tmpFile), desc.getId());
		            } catch ( Exception e ) {
		            }
				}
			}
		}
		
		super.performRequest(req);
	}
	/**
	 * show a Wizard with the predicted mass spectrum.
	 * 
	 * @return The IMolecule object
	 */
	public IMolecule showWizard(){
		//NewAquisitionWizard nAW = new NewAquisitionWizard();
		//WizardDialog wd = new WizardDialog( new Shell(), nAW );
		//wd.open();
		//return nAW.getIMolecule();
	    return null;
	}
	/**
	 * rewrite the Cell
	 */
	private void performDirectEdit(){
		if(directManager  == null){
			directManager = new MyDirectEditManager(this, TextCellEditor.class, 
													new MyCellEditorLocator(getFigure()));
				
		}
		directManager.show();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection){
		return new ChopboxAnchor(getFigure());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection){
		return new ChopboxAnchor(getFigure());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request){
		return new ChopboxAnchor(getFigure());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request){
		return new ChopboxAnchor(getFigure());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections(){
		return ((AbstractObjectModel)getModel()).getModelSourceConnections();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections(){
		return ((AbstractObjectModel)getModel()).getModelTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter ) {
		
		AbstractObjectModel abstractObject = (AbstractObjectModel)this.getModel();
		if(abstractObject instanceof CompoundObjectModel){
			if (IPropertySource.class.equals(adapter)) {
				CDKMolecule cdkMol= new CDKMolecule(((CompoundObjectModel)abstractObject).getIMolecule() );
	            return new CDKMoleculePropertySource(cdkMol);
			}
			IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule( ((CompoundObjectModel)abstractObject).getIMolecule());
			if(mol != null && mol.getAtomCount() > 0){

				if(!GeometryTools.has2DCoordinates(mol)){

					StructureDiagramGenerator sdg = new StructureDiagramGenerator();
					sdg.setMolecule(mol);
					try {
						sdg.generateCoordinates();
						mol = sdg.getMolecule();
						GeometryTools.translateAllPositive(mol);
						((CompoundObjectModel)abstractObject).setIMolecule(mol);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				JChemPaintEditorWidget jcp = abstractObject.getJCP();
				jcp.setAtomContainer(mol);
			}
		}else if(abstractObject instanceof ReactionObjectModel){
			if (IPropertySource.class.equals(adapter)) {
				CDKReaction cdkReact= new CDKReaction(((ReactionObjectModel)abstractObject).getIReaction() );
	            return new CDKReactionPropertySource(cdkReact);
	        
			}
			IReaction reaction = ((ReactionObjectModel)this.getModel()).getIReaction();
			JChemPaintEditorWidget jcp = abstractObject.getJCP();
			jcp.setReaction( reaction );

		}
		return super.getAdapter(adapter);
	}
}
