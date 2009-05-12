package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractConnectionModel;
import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class ReconnectConnectionCommand extends Command {
	private AbstractConnectionModel connection;
	private AbstractObjectModel newSource = null;
	private AbstractObjectModel newTarget = null;
	private AbstractObjectModel oldSource = null;
	private AbstractObjectModel oldTarget = null;

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (newSource != null) {
			oldSource = connection.getSource();
			reconnectSource(newSource);
		}

		if (newTarget != null) {
			oldTarget = connection.getTarget();
			reconnectTarget(newTarget);
		}
	}

	private void reconnectSource(AbstractObjectModel source) {
		connection.detachSource();
		connection.setSource(source);
		connection.attachSource();
	}

	private void reconnectTarget(AbstractObjectModel target) {
		connection.detachTarget();
		connection.setTarget(target);
		connection.attachTarget();
	}

	public void setConnectionModel(Object model) {
		connection = (AbstractConnectionModel) model;
	}

	public void setNewSource(Object model) {
		newSource = (AbstractObjectModel) model;
	}

	public void setNewTarget(Object model) {
		newTarget = (AbstractObjectModel) model;
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		if (oldSource != null)
			reconnectSource(oldSource);
		if (oldTarget != null)
			reconnectTarget(oldTarget);

		oldSource = null;
		oldTarget = null;
	}
}

