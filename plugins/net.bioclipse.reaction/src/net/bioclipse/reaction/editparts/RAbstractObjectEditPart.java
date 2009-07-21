/*******************************************************************************
 * Copyright (c) 2007-2009  Miguel Rojas <miguelrojasch@users.sf.net>, 
 *                          Stefan Kuhn <shk3@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeEvent;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.cdk.domain.CDKMolecule;
import net.bioclipse.cdk.domain.CDKMoleculePropertySource;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.cdk.jchempaint.widgets.JChemPaintEditorWidget;
import net.bioclipse.chemoinformatics.wizards.WizardHelper;
import net.bioclipse.reaction.domain.CDKReactionPropertySource;
import net.bioclipse.reaction.editpolicies.RComponentEditPolicy;
import net.bioclipse.reaction.editpolicies.RDirectEditPolicy;
import net.bioclipse.reaction.editpolicies.RGraphicalNodeEditPolicy;
import net.bioclipse.reaction.model.AbstractModel;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.gef.AccessibleEditPart;
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
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;

/**
 * 
 * @author Miguel Rojas
 */
public class RAbstractObjectEditPart extends EditPartWithListener implements NodeEditPart, IPropertyListener, IAdaptable{
	
	private RDirectEditManager directManager = null;
    private static Map<IEditorPart,IFile> orignalFiles=new HashMap<IEditorPart,IFile>();
    private static Map<IEditorPart,CompoundObjectModel> orignalChildren=new HashMap<IEditorPart,CompoundObjectModel>();
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
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new RComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,new RDirectEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,new RGraphicalNodeEditPolicy());
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals(){
		Rectangle constraint = ((AbstractObjectModel)getModel()).getConstraint();
		// New Reactangle to store in CML
		((GraphicalEditPart)getParent()).setLayoutConstraint(this,getFigure(),constraint);
	}
	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt){
		if(evt.getPropertyName().equals(AbstractObjectModel.P_CONSTRAINT)){
			refreshVisuals();
		}else if(evt.getPropertyName().equals(AbstractObjectModel.P_TEXT)){
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
		
		/* one-click functionality - introduction text - show molecule*/
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			this.performDirectEdit(); //-introduction text
		}
		/* double-click functionality - Opening JCP editor*/
		if(req.getType().equals(RequestConstants.REQ_OPEN)){
			AbstractObjectModel abstractObject = (AbstractObjectModel)this.getModel();
			if(abstractObject instanceof CompoundObjectModel){
				CompoundObjectModel compoundObject = (CompoundObjectModel)abstractObject;
				IAtomContainer atomContainer = DefaultChemObjectBuilder.getInstance().newMolecule( compoundObject.getIMolecule());
				StringWriter writer = new StringWriter();
				CMLWriter cmlWriter = new CMLWriter(writer);
		        if(atomContainer == null){
					atomContainer = new AtomContainer();
					atomContainer.setID("m");
				}
		        try {
					cmlWriter.write(atomContainer);
				} catch (CDKException e1) {
					e1.printStackTrace();
				}
		        String cmlContent = writer.toString();
		        IEditorDescriptor desc = PlatformUI.getWorkbench().
	            getEditorRegistry().getDefaultEditor(atomContainer.getID()+"_.cml",Platform.getContentTypeManager().getContentType( "net.bioclipse.contenttypes.cml.singleMolecule2d" ));
		        try {
	                IFile tmpFile= net.bioclipse.core.Activator.getVirtualProject().getFile(WizardHelper.findUnusedFileName(new StructuredSelection(net.bioclipse.core.Activator.getVirtualProject()), atomContainer.getID(), ".cml") );
	                tmpFile.create( new StringBufferInputStream(cmlContent), IFile.FORCE, null);
	                IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(tmpFile), desc.getId());
	                editor.addPropertyListener( this );
	                orignalFiles.put( editor, tmpFile);
	                orignalChildren.put(editor,compoundObject);
                } catch ( Exception e ) {
	            }
	            
			}
		}
		
		super.performRequest(req);
	}
	/**
	 * rewrite the Cell
	 */
	private void performDirectEdit(){
		if(directManager  == null){
			directManager = new RDirectEditManager(this, TextCellEditor.class, 
													new RCellEditorLocator(getFigure()));
				
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
	protected List<Object> getModelSourceConnections(){
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
		AbstractModel abstractObject = (AbstractObjectModel)this.getModel();
		if(abstractObject instanceof CompoundObjectModel){
			if (IPropertySource.class.equals(adapter)) {
				CDKMolecule cdkMol= new CDKMolecule(((CompoundObjectModel)abstractObject).getIMolecule() );
	            return new CDKMoleculePropertySource(cdkMol);
			}
			else if (AccessibleEditPart.class.equals(adapter)){
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
					JChemPaintEditorWidget jcp = ((CompoundObjectModel)abstractObject).getJCP();
					jcp.setAtomContainer(mol);
				}
			}
		}else if(abstractObject instanceof ReactionObjectModel){
			if (IPropertySource.class.equals(adapter)) {
	            return new CDKReactionPropertySource((ReactionObjectModel)abstractObject);
	        }else if (AccessibleEditPart.class.equals(adapter)){
				IReaction reaction = ((ReactionObjectModel)this.getModel()).getIReaction();
				JChemPaintEditorWidget jcp = ((AbstractObjectModel)abstractObject).getJCP();
				jcp.setReaction( reaction );
			}

		}else if(net.bioclipse.core.domain.IMolecule.class.equals(adapter)){
			CDKMolecule cdkMol= new CDKMolecule(((CompoundObjectModel)abstractObject).getIMolecule() );
            return cdkMol.getAtomContainer();
		}
		return super.getAdapter(adapter);
	}
	
	public void propertyChanged(Object source, int paramInt) {
		ICDKMolecule cdkMol = Activator.getDefault().getJavaCDKManager().loadMolecule(orignalFiles.get( source ) );
		IAtomContainer ac = cdkMol.getAtomContainer();
		CompoundObjectModel compoundObject = orignalChildren.get(source);
		compoundObject.setIMolecule(ac);
		JChemPaintEditorWidget jcp = compoundObject.getJCP();
		jcp.setAtomContainer(ac);
	}
}
