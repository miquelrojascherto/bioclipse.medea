package net.bioclipse.plugins.bc_reaction.editparts;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point2d;

import net.bioclipse.plugins.bc_reaction.editors.ReactMolDrawingComposite;
import net.bioclipse.plugins.bc_reaction.editpolicies.MyComponentEditPolicy;
import net.bioclipse.plugins.bc_reaction.editpolicies.MyDirectEditPolicy;
import net.bioclipse.plugins.bc_reaction.editpolicies.MyGraphicalNodeEditPolicy;
import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;
import net.bioclipse.plugins.bc_reaction.model.CompoundObjectModel;
import net.bioclipse.plugins.bc_reaction.model.ReactionObjectModel;
import net.bioclipse.plugins.bc_reaction.wizards.NewAquisitionWizard;

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
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

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

			
			IChemModel chemModel = DefaultChemObjectBuilder.getInstance().newChemModel();
			
			/*create IChemModel*/
			AbstractObjectModel abstractObject = (AbstractObjectModel)this.getModel();
			HashMap coordinates = new HashMap();
			if(abstractObject instanceof CompoundObjectModel){
				IMolecule mol = ((CompoundObjectModel)abstractObject).getIMolecule();
				if(mol != null && mol.getAtomCount() > 0){

					IMoleculeSet moleculeSet = mol.getBuilder().newMoleculeSet();
					if(!GeometryTools.has2DCoordinates(mol)){

						StructureDiagramGenerator sdg = new StructureDiagramGenerator();
						sdg.setMolecule(mol);
						try {
							sdg.generateCoordinates();
							mol = sdg.getMolecule();
							GeometryTools.translateAllPositive(mol,coordinates);
							Iterator it2 = mol.atoms();
							while(it2.hasNext()){
								IAtom atom=(IAtom)it2.next();
								atom.setPoint2d((Point2d)coordinates.get(atom));
							}
							((CompoundObjectModel)abstractObject).setIMolecule(mol);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					moleculeSet.addMolecule(mol);
					chemModel.setMoleculeSet(moleculeSet);
					ReactMolDrawingComposite jcp = abstractObject.getJCP();
					jcp.loadNewChemModel(chemModel,null);
					
				}else
					chemModel = null;
			}else if(abstractObject instanceof ReactionObjectModel){
				IReaction reaction = ((ReactionObjectModel)this.getModel()).getIReaction();
				IReactionSet reactionSet = reaction.getBuilder().newReactionSet();
				IMoleculeSet moleculeSet = ReactionManipulator.getAllReactants(reaction);
				int numbReactants = moleculeSet.getAtomContainerCount();
				moleculeSet.add(ReactionManipulator.getAllProducts(reaction));
				
				int countM = 0;
				int width = 1;
				for(Iterator iter = moleculeSet.molecules(); iter.hasNext();){
					IMolecule mol = (IMolecule)iter.next();
					if(mol.getAtomCount() == 0)
						continue;
					
					
					
					GeometryTools.translateAllPositive(mol,coordinates);
//					GeometryTools.scaleMolecule(mol, new Dimension(xsize,ysize), 0.8,coordinates);			
//					GeometryTools.center(mol, new Dimension(xsize,ysize),coordinates);
					
					Iterator it2 = mol.atoms();

					while(it2.hasNext()){
						IAtom atom=(IAtom)it2.next();
						((Point2d)coordinates.get(atom)).x += width;
					}
					
					if(countM == numbReactants - 1)
						width += GeometryTools.get2DDimension(mol, coordinates).width+100;
					else
						width += GeometryTools.get2DDimension(mol, coordinates).width+30;
					
					countM++;
				}
				reactionSet.addReaction(reaction);
				chemModel.setReactionSet(reactionSet);
			}
			if(chemModel != null){
				ReactMolDrawingComposite jcp = abstractObject.getJCP();
				jcp.loadNewChemModel(chemModel,coordinates);
			}
			this.performDirectEdit(); //-introduction text
			
			
			return;
		}
		/* double-click funcionality - Opening JCP editor*/
		if(req.getType().equals(RequestConstants.REQ_OPEN)){
//			System.out.println("doubles click - opening wizard");
//			LoadMoleculeDialog lmd = new LoadMoleculeDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
//			lmd.open();
//			
//			if(lmd.getAction() == LoadMoleculeDialog.ACTION_OPEN_WIZARD){
			/*the idea is able to choose how to load a molecule*/
			
				IChemModel chemModel = DefaultChemObjectBuilder.getInstance().newChemModel();
				AbstractObjectModel abstractObject = (AbstractObjectModel)this.getModel();
				HashMap coordinates = new HashMap();
				
				if(abstractObject instanceof CompoundObjectModel){
					CompoundObjectModel compoundOM = (CompoundObjectModel)abstractObject;
					IMolecule molecule = showWizard();
					if(molecule == null)
						return;
					compoundOM.setIMolecule(molecule);
					compoundOM.setText(molecule.getID());
					IMoleculeSet moleculeSet = molecule.getBuilder().newMoleculeSet();
					StructureDiagramGenerator sdg = new StructureDiagramGenerator();
					sdg.setMolecule(molecule);
					try {
						sdg.generateCoordinates();
						GeometryTools.translateAllPositive(molecule,coordinates);
					} catch (Exception e) {
						e.printStackTrace();
					}
					moleculeSet.addMolecule(sdg.getMolecule());
					chemModel.setMoleculeSet(moleculeSet);
//					if(sdg.getMolecule() != null)
//						if(sdg.getMolecule().getAtomCount() > 0){
//							System.out.println("getAtomCount:"+sdg.getMolecule().getAtomCount());
//							ReactMolDrawingComposite jcp = abstractObject.getJCP();
//							System.out.println("jcp:"+jcp);
//							jcp.getPage().setDirty(true);
//						}
				}
//			}else if(lmd.getAction() == LoadMoleculeDialog.ACTION_OPEN_JCP)){
//				
//			}
		}
		
		super.performRequest(req);
	}
	/**
	 * show a Wizard with the predicted mass spectrum.
	 * 
	 * @return The IMolecule object
	 */
	public IMolecule showWizard(){
		NewAquisitionWizard nAW = new NewAquisitionWizard();
		WizardDialog wd = new WizardDialog( new Shell(), nAW );
		wd.open();
		return nAW.getIMolecule();
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

}