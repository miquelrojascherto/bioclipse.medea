package net.bioclipse.plugins.extensions;

import org.xmlcml.cml.base.CMLElement;

import net.bioclipse.seneca.judge.IJudge;
import net.bioclipse.seneca.judge.IJudgeFactory;
import net.bioclipse.seneca.judge.MissingInformationException;

public class WCCMedeaJudgeFactory implements IJudgeFactory {

	private boolean enabled;
	
	public IJudge createJudge(CMLElement data)
			throws MissingInformationException {
		IJudge judge = new net.bioclipse.plugins.extensions.WCCMedeaJudge();
		judge.configure(data);
		judge.setEnabled(enabled);
		return judge;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

}
