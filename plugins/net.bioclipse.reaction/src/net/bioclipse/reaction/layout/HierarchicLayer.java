package net.bioclipse.reaction.layout;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.reaction.model.AbstractConnectionModel;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ContentsModel;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Layer tree
 * 
 * @author Miguel Rojas
 */
public class HierarchicLayer {
	private int[][] board = new int[300][300];
	private int distanceWidth = 100;
	private int distanceHeight = 50;
	private List list;
	private int maximalHeigh = 150;
	private int layoutPosition;
	
	/**
	 * Constructor of HierarchicLayer object
	 * 
	 * @param contentsModel 
	 */
	public HierarchicLayer(ContentsModel contentsModel){
//		System.out.println("position: "+layoutPosition);
		
		list = new ArrayList();
		List children = contentsModel.getChildren();
		
		/* put in order the the model into the board*/
		for(int i = 0 ; i < children.size(); i++){

//			System.out.println("i: "+i);
			/* value centrum into the board*/
			int start = 150;
			
			AbstractObjectModel object = (AbstractObjectModel)children.get(i);
			
			
			/* set products */
			List source = object.getModelSourceConnections();
			for(int j = 0 ; j < source.size(); j++){
//				System.out.println("j_prod: "+j);
				AbstractConnectionModel c = (AbstractConnectionModel)source.get(j);
				AbstractObjectModel oo = c.getTarget();

				if(existObjectintoList(oo) == -1){
					List target = oo.getModelTargetConnections();
					for(int k = 0 ; k < target.size(); k++){
						AbstractConnectionModel c1 = (AbstractConnectionModel)target.get(k);
						AbstractObjectModel oo1 = c1.getSource();
						int row = existObjectintoList(oo1);
						if(row != -1)
							start = row;
					}
					getPosition(oo,start-1);
				}
			}
			/* set reactants*/
			List target = object.getModelTargetConnections();
			for(int j = 0 ; j < target.size(); j++){
//				System.out.println("j_react: "+j);
				AbstractConnectionModel c = (AbstractConnectionModel)target.get(j);
				AbstractObjectModel oo = c.getSource();
				
				if(existObjectintoList(oo) == -1){
					getPosition(oo,start+1);
					if((start +1) > maximalHeigh )
						maximalHeigh  = start+1;
				}
			}
		}
		
		/* set the objects of the model starting the tree in point 0,0 */
//		System.out.println("00: ");
		for(int i = 0 ; i < children.size() ; i++){
//			System.out.println("i: "+i);
			AbstractObjectModel object = (AbstractObjectModel)children.get(i);
			for( int j = 0; j < list.size() ; j++){
//				System.out.println("j: "+j);
				ID id = (ID)list.get(j);
				if(id.getObject().getEditableValue().equals(object.getEditableValue())){
//					System.out.println("id: "+id.column+","+id.row);
					object.setConstraint(new Rectangle((id.column+1)*distanceWidth,(maximalHeigh-id.row+1)*distanceHeight,-1,-1));
					break;
				}
			}
		}
		
	}
	/**
	 * search if the object exists into the List
	 * 
	 * @param object  The AbstractObjectModel
	 * @return The position
	 */
	private int existObjectintoList(AbstractObjectModel object){
		for( int j = 0; j < list.size() ; j++){
			ID id = (ID)list.get(j);
			if(id.getObject().getEditableValue().equals(object.getEditableValue())){
				return id.getRow();
			}
		}
		return -1;
	}

	/**
	 * get the position which is free into the board in a definited row
	 * 
	 * @param object The AbstractObjectModel
	 * @param row  The Row
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int[] getPosition(AbstractObjectModel object, int row) {
		int columnFree = searchColumnFree(row);
		board[row][columnFree] = 1;
		
		list.add(new ID(row,columnFree,object));
		int[] position = new int[2];
		position[0] = row;
		position[1] = columnFree+1;
		return position;
	}
	/**
	 * search column free
	 * 
	 * @param row
	 * @return The position the column which is free
	 */
	private int searchColumnFree(int row) {
		for(int i = 0 ; i < board[row].length ; i++)
			if(board[row][i] == 0)
				return i;
		return 0;
	}
	/**
	 * A class which set a ID
	 * 
	 * @author Miguel Rojas
	 */
	class ID{
		private int row;
		private AbstractObjectModel object;
		private int column;
		
		/**
		 * Constructor of the ID object
		 * 
		 * @param row    The row position
		 * @param column The column position
		 * @param object The AbstractObjectModel
		 */
		ID(int row, int column, AbstractObjectModel object){
			this.row = row;
			this.column = column;
			this.object = object;
		}
		/**
		 * Get the AbstractObjectModel
		 * 
		 * @return The AbstractObjectModel
		 */
		public AbstractObjectModel getObject(){
			return object;
		}
		/**
		 * Get the position row
		 * @return The position
		 */
		public int getRow(){
			return row;
		}
		/**
		 * 
		 * Get the position column
		 * @return The position
		 */
		public int getColumn(){
			return column;
		}
	}
}
