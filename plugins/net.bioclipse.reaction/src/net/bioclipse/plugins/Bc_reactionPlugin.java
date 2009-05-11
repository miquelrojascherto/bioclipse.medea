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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.tools.logging.PluginLogManager;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Miguel Rojas
 */
public class Bc_reactionPlugin extends BioclipsePlugin implements IBioclipsePlugin {

	public static final String OPEN_PERSP_FOR_RES = "Open Reaction Perspective for ReactionResources";
	
	public static final String SHOW_PALETTE_PREF = "Show Palette";
	
	public static final String LAYOUT_PREFER = "Layout preference";
	public static final int HIERARCHIC_VERT_LEFT = 0;
	public static final int HIERARCHIC_VERT_CENTER = 1;
	public static final int HIERARCHIC_VERT_RIGHT = 2;
	public static final int HIERARCHIC_HOR_UP = 10;
	public static final int HIERARCHIC_HOR_CENTER = 11;
	public static final int HIERARCHIC_HOR_DOWN = 12;
	
	private final String PLUGIN_ID = "net.bioclipse.reaction";
	private final String LOG_PROPERTIES_FILE = "logger.properties";
	private PluginLogManager logManager;

	
	//The shared instance.
	private static Bc_reactionPlugin plugin;
	
	/**
	 * The constructor of the Bc_reactionPlugin object.
	 */
	public Bc_reactionPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Reaction plugin...");

		super.start(context);

		//New logger with com.tools.logging
		configureLogger();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Bc_reactionPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("net.bioclipse.plugins.bc_reaction", path);
	}
	

	public static PluginLogManager getLogManager() {
		return getDefault().logManager; 
	}
	
	private void configureLogger() {
		try {
			@SuppressWarnings("unused")
			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL url = Platform.getBundle(PLUGIN_ID).getEntry("/" + LOG_PROPERTIES_FILE);
			
			System.out.println("Spectrum plugin logging using properties file: " + url.toExternalForm());
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
			String message = "Error while initializing Reaction plugin log properties." + e.getMessage();
			System.out.println(message);
			throw new RuntimeException(message,e);
		}         
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(OPEN_PERSP_FOR_RES, Bc_reactionPlugin.UNSET);
		store.setDefault(SHOW_PALETTE_PREF, 0);
		store.setDefault(LAYOUT_PREFER, 0);
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

	public int getLayoutPreference() {
		return getPreferenceStore().getInt(LAYOUT_PREFER);
	}
	
	public void setLayoutPreference(int value) {
		getPreferenceStore().setValue(LAYOUT_PREFER, value);
	}

	public String getLAYOUT_PREFER() {
		return LAYOUT_PREFER;
	}
	
	public int getPalettePreference() {
		return getPreferenceStore().getInt(SHOW_PALETTE_PREF);
	}
	
	public void setPalettePreference(int value) {
		getPreferenceStore().setValue(SHOW_PALETTE_PREF, value);
	}

	public String getSHOW_PALETTE_PREF() {
		return SHOW_PALETTE_PREF;
	}
}
