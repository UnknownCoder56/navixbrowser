package com.uniqueapps.navixbrowser.object;

import java.util.prefs.Preferences;

import com.uniqueapps.navixbrowser.Main.Theme;

public class UserSettings {

	public boolean HAL = true;
	public boolean OSR = false;
	public boolean launchMaximized = true;
	public String searchEngine = "https://google.com/search?q=";
	public Theme theme = Theme.Modern;
	public boolean enableAdBlock = true;
	public boolean enableTrackerBlock = true;
	public boolean enableSafeBrowsing = true;
	public String[] args = {};
	public boolean forceDarkMode = false;
	public boolean enableSearchSuggestions = true;
	public String newTabURL = "navix://home";
	public int debugPort = 8090;
	private final Preferences prefs = Preferences.userNodeForPackage(UserSettings.class);

	public void save() {
		prefs.putBoolean("HAL", HAL);
		prefs.putBoolean("OSR", OSR);
		prefs.putBoolean("launchMaximized", launchMaximized);
		prefs.put("searchEngine", searchEngine);
		prefs.put("theme", theme.name());
		prefs.putBoolean("enableAdBlock", enableAdBlock);
		prefs.putBoolean("enableTrackerBlock", enableTrackerBlock);
		prefs.putBoolean("enableSafeBrowsing", enableSafeBrowsing);
		prefs.putBoolean("forceDarkMode", forceDarkMode);
		prefs.putBoolean("enableSearchSuggestions", enableSearchSuggestions);
		prefs.put("newTabURL", newTabURL);
		prefs.putInt("debugPort", debugPort);
	}

	public void load() {
		HAL = prefs.getBoolean("HAL", HAL);
		OSR = prefs.getBoolean("OSR", OSR);
		launchMaximized = prefs.getBoolean("launchMaximized", launchMaximized);
		searchEngine = prefs.get("searchEngine", searchEngine);
		theme = Theme.valueOf(prefs.get("theme", theme.name()));
		enableAdBlock = prefs.getBoolean("enableAdBlock", enableAdBlock);
		enableTrackerBlock = prefs.getBoolean("enableTrackerBlock", enableTrackerBlock);
		enableSafeBrowsing = prefs.getBoolean("enableSafeBrowsing", enableSafeBrowsing);
		forceDarkMode = prefs.getBoolean("forceDarkMode", forceDarkMode);
		enableSearchSuggestions = prefs.getBoolean("enableSearchSuggestions", enableSearchSuggestions);
		newTabURL = prefs.get("newTabURL", newTabURL);
		debugPort = prefs.getInt("debugPort", debugPort);
	}
}
