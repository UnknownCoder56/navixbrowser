package com.uniqueapps.navixbrowser.object;

import java.io.Serializable;

import com.uniqueapps.navixbrowser.Main.Theme;

public class UserSettings implements Serializable {

	private static final long serialVersionUID = -3073111196488214954L;
	public boolean HAL = true;
	public boolean OSR = false;
	public boolean launchMaximized = true;
	public String searchEngine = "https://google.com/search?q=";
	public Theme theme = Theme.Modern;
	public boolean enableAdBlock = true;
	public boolean enableTrackerBlock = true;
	public boolean enableSafeBrowsing = true;
}
