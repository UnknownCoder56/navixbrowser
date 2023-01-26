package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.network.CefRequest;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.Main.Theme;

import java.io.File;

public class NavixSchemeHandler extends CefResourceHandlerAdapter {

	public static final String scheme = "navix";
	CefBrowser browser;

	public NavixSchemeHandler(CefBrowser browser) {
		this.browser = browser;
	}

	@Override
	public boolean processRequest(CefRequest cefRequest, CefCallback cefCallback) {
		String action = cefRequest.getURL();
		if (action.contains("home")) {
			File resources = new File(".", "resources");
			if (Main.settings.theme == Theme.Dark) {
				browser.loadURL("file://" + resources.getAbsolutePath() + "/newtab-dark.html");
			} else {
				browser.loadURL("file://" + resources.getAbsolutePath() + "/newtab-light.html");
			}
			cefCallback.Continue();
		}
		return false;
	}
}
