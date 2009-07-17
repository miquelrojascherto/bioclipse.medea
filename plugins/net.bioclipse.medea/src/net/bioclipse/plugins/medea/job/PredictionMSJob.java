/*******************************************************************************
 * Copyright (c) 2006 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Rob Schellhorn
 ******************************************************************************/

package net.bioclipse.plugins.medea.job;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.model.BioResourceType;
import net.bioclipse.model.IBioResource;
import net.bioclipse.model.SpectrumResource;
import net.bioclipse.model.resources.StringResource;
import net.bioclipse.util.BioclipseConsole;
import net.bioclipse.util.folderUtils;
import net.bioclipse.views.BioResourceView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.HueckelAromaticityDetector;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.structgen.RandomGenerator;
import org.openscience.cdk.structgen.SingleStructureRandomGenerator;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLSpectrum;

/**
 * @author Rob Schellhorn
 */
public class PredictionMSJob extends Job {

	/**
	 * The selection to run this computation on, never <code>null</code>.
	 */
	private IStructuredSelection selection = StructuredSelection.EMPTY;

	
	public PredictionMSJob() {
		super("Prediction Mass Spectrum");

		assert invariant() : "POST: The invariant holds";
	}

	/**
	 * @return <code>true</code> if this instance satisfies the class
	 *         invariant, <code>false</code> otherwise.
	 */
	private boolean invariant() {
		return selection != null;
	}

	/*
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(IProgressMonitor monitor) {
		assert invariant() : "PRE: The invariant holds";

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

			
			try {
				int stepsDone = 0;
				monitor.beginTask("Computing", 10000);


					monitor.worked(1);
					for(int i = 0; i< 100; i++)
						System.out.println("hola");
					stepsDone++;
					Thread.sleep(50);
				
			} catch (Exception exception) {
				System.out.println("An exception occured: " + exception.getMessage());
				exception.printStackTrace();
			}

		monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * Sets the selection of this job.
	 * 
	 * @param selection
	 *            The new selection for this job.
	 * @throws IllegalArgumentException
	 *             If the given selection is <code>null</code>.
	 */
	public void setSelection(IStructuredSelection selection) {
		assert invariant() : "PRE: The invariant holds";

		if (selection == null) {
			throw new IllegalArgumentException();
		}

		this.selection = selection;

		assert invariant() : "POST: The invariant holds";
	}
	
	public SpectrumResource[] getSpectrumResources() {
		assert invariant() : "PRE: The invariant holds";

		List<SpectrumResource> resources = new ArrayList<SpectrumResource>();
		for (Object o : selection.toArray()) {

			if (o instanceof SpectrumResource) {
				resources.add((SpectrumResource) o);
			}
		}

		return resources.toArray(new SpectrumResource[resources.size()]);
	}
	
	class ChildResourceCreator implements Runnable {
		
		private IBioResource virtualFolder;
		private int counter;
		private Convertor cmlConvertor;
		private IMolecule result;

		public ChildResourceCreator(IBioResource virtualFolder, int counter, Convertor cmlConvertor, IMolecule result) {
			this.virtualFolder = virtualFolder;
			this.counter = counter;
			this.cmlConvertor = cmlConvertor;
			this.result = result;
		}
		
		public void run() {
			System.out.println("running");
		}
	}
	
}