/**
 * 
 */
package net.bioclipse.reaction.wizards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
/**
 * 
 * @author Miguel Rojas
 */
public class CDKResourceContentProvider implements ITreeContentProvider, IDeltaListener {
		private  Object[] EMPTY_ARRAY = new Object[0];
		protected TreeViewer viewer;
		
		/*
		 * @see IContentProvider#dispose()
		 */
		public void dispose() {}

		/*
		 * @see IContentProvider#inputChanged(Viewer, Object, Object)
		 */
		/**
		* Notifies this content provider that the given viewer's input
		* has been switched to a different element.
		* <p>
		* A typical use for this method is registering the content provider as a listener
		* to changes on the new input (using model-specific means), and deregistering the viewer 
		* from the old input. In response to these change notifications, the content provider
		* propagates the changes to the viewer.
		* </p>
		*
		* @param viewer the viewer
		* @param oldInput the old input element, or <code>null</code> if the viewer
		*   did not previously have an input
		* @param newInput the new input element, or <code>null</code> if the viewer
		*   does not have an input
		*/
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.viewer = (TreeViewer)viewer;
			if(oldInput != null) {
				removeListenerFrom((BioResource)oldInput);
			}
			if(newInput != null) {
				addListenerTo((BioResource)newInput);
			}
		}
		
		/** Because the domain model does not have a richer
		 * listener model, recursively remove this listener
		 * from each child BioResource of the given BioResource. */
		protected void removeListenerFrom(BioResource bioResource) {
			bioResource.removeListener(this);
			java.util.List childList = bioResource.getChildrenWithoutLoading();
			if (childList==null) return;
			for (Iterator iterator = childList.iterator(); iterator.hasNext();) {
				BioResource aResource = (BioResource) iterator.next();
				removeListenerFrom(aResource);
			}
		}
		
		/** Because the domain model does not have a richer
		 * listener model, recursively add this listener
		 * to each child BioResource of the given BioResource. */
		protected void addListenerTo(BioResource BioResource) {
			BioResource.addListener(this);
		}
		

		/*
		 * @see ITreeContentProvider#getChildren(Object)
		 */
		public Object[] getChildren(Object parentElement) {
			if(parentElement instanceof BioResource) {
				ArrayList<BioResource> returnList = new ArrayList<BioResource>();
				BioResource bioResource = (BioResource)parentElement;
				if (bioResource.getChildren() == null) {
					return EMPTY_ARRAY;
				}
				Iterator it= bioResource.getChildren().iterator();
				while (it.hasNext()){
					BioResource child=(BioResource)it.next();
					if (child.getDefaultResourceType().getId().equals(CMLResource.ID)) {
						child.getPersistedResource().load();
						byte[] barray = child.getPersistedResource().getInMemoryResource();
						boolean hasMol = false;
						boolean hasSpect = false;
						BufferedReader breader = new BufferedReader(new StringReader(new String(barray)));
						String line = null;
						try {
							while ((line = breader.readLine()) != null) {
								if (!hasMol && line.contains("<molecule")) {
									hasMol = true;
									break;
								}
								if (line.contains("<spectrum")) {
									hasSpect = true;
									
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (hasMol && !hasSpect) {
							addListenerTo(child);
							returnList.add(child);
						}
					}
					if (child.getDefaultResourceType().getId().equals(CDKResource.ID)) {
						addListenerTo(child);
						returnList.add(child);
					}
					if (child.isFolder()) {
						addListenerTo(child);
						returnList.add(child);
					}
				}
				
				return returnList.toArray();
			}
			return EMPTY_ARRAY;
		}
		

		/*
		 * @see ITreeContentProvider#getParent(Object)
		 */
		public Object getParent(Object element) {
			if(element instanceof IBioResource) {
				return ((IBioResource)element).getParent();
			}
			return null;
		}

		/*
		 * @see ITreeContentProvider#hasChildren(Object)
		 */
		public boolean hasChildren(Object element) {

			if(element instanceof IBioResource) {
				IBioResource bioResource = (IBioResource)element;
				return bioResource.hasChildren();
			}
			return false;
			
			
			//		return getChildren(element).length > 0;
		}

		/*
		 * @see IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		/*
		 * @see IDeltaListener#add(DeltaEvent)
		 */
		public void add(DeltaEvent event) {
			Object BioResource = ((BioResource)event.receiver()).getParent();
			viewer.refresh(BioResource, false);
		}

		/*
		 * @see IDeltaListener#remove(DeltaEvent)
		 */
		public void remove(DeltaEvent event) {
			add(event);
		}

	}