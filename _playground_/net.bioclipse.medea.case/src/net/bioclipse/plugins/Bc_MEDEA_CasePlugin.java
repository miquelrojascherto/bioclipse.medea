package net.bioclipse.plugins;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.bioclipse.BioclipsePlugin;
import net.bioclipse.interfaces.IBioclipsePlugin;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.tools.logging.PluginLogManager;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Miguel Rojas
 */
public class Bc_MEDEA_CasePlugin extends BioclipsePlugin implements IBioclipsePlugin {

	public static final String OPEN_PERSP_FOR_RES = "Open Medea Perspective";
	/**The plug-in ID*/
	public final static String PLUGIN_ID="net.bioclipse.plugins.medea";
	
	/**The shared instance.*/
	private static Bc_MEDEA_CasePlugin plugin;

	private final String LOG_PROPERTIES_FILE="logger.properties";
	private PluginLogManager logManager;
	
	/**
	 * The constructor of the Bc_MEDEAPlugin object.
	 */
	public Bc_MEDEA_CasePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 * 
	 * @see net.bioclipse.BioclipsePlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
//		New logger with com.tools.logging
		configureLogger();
	}

	/**
	 * This method is called when the plug-in is stopped
	 * 
	 * @param context The BundleContext
	 * @see net.bioclipse.BioclipsePlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Bc_MEDEA_CasePlugin getDefault() {
		return plugin;
	}
	
	/**
	 * 
	 * @return The LogMananger default
	 */
	public static PluginLogManager getLogManager() {
		return getDefault().logManager; 
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path An String with contain the path
	 * @return the image descriptor from Plugin MEDEA
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("MEDEA", path);
	}
	
	private void configureLogger() {

		try {
			URL url = Platform.getBundle(PLUGIN_ID).getEntry("/" + LOG_PROPERTIES_FILE);
			InputStream propertiesInputStream = url.openStream();
			if (propertiesInputStream != null) {
				Properties props = new Properties();
				props.load(propertiesInputStream);
				propertiesInputStream.close();
				this.logManager = new PluginLogManager(this, props);
//				this.logManager.hookPlugin(
//				TestPlugin.getDefault().getBundle().getSymbolicName(),
//				TestPlugin.getDefault().getLog()); 
			}	
		} 
		catch (Exception e) {
			String message = "Error while initializing CDK log properties: " + e.getMessage();
			System.err.println(message);
			throw new RuntimeException(message,e);
		}         
	}	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(OPEN_PERSP_FOR_RES, Bc_MEDEA_CasePlugin.UNSET);
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.interfaces.IBioclipsePlugin#getOpenPerspectivePreference()
	 */
	public int getOpenPerspectivePreference() {
		return getPreferenceStore().getInt(OPEN_PERSP_FOR_RES);
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.interfaces.IBioclipsePlugin#setOpenPerspectivePreference(int)
	 */
	public void setOpenPerspectivePreference(int value) {
		getPreferenceStore().setValue(OPEN_PERSP_FOR_RES, value);
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.interfaces.IBioclipsePlugin#getOPEN_PERSP_FOR_RES()
	 */
	public String getOPEN_PERSP_FOR_RES() {
		return OPEN_PERSP_FOR_RES;
	}
}
