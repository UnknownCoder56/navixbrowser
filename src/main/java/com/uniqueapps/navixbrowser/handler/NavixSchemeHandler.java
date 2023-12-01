package com.uniqueapps.navixbrowser.handler;

import com.uniqueapps.navixbrowser.Main;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.network.CefRequest;

import java.awt.*;
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
			browser.loadURL("file://" + resources.getAbsolutePath() + (Main.getTextColorForBackground() == Color.WHITE ? "/newtab-dark.html" : "/newtab-light.html"));
			cefCallback.Continue();
		}
		return false;
	}
}
