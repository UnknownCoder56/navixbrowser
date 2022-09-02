package com.uniqueapps.navixbrowser.handler;

import java.util.Vector;

import javax.swing.JFrame;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefFileDialogCallback;
import org.cef.handler.CefDialogHandler;

public class NavixDialogHandler implements CefDialogHandler {

	JFrame windowFrame;

	public NavixDialogHandler(JFrame windowFrame) {
		this.windowFrame = windowFrame;
	}

	@Override
	public boolean onFileDialog(CefBrowser cefBrowser, FileDialogMode fileDialogMode, String title,
			String defaultFilePath, Vector<String> acceptFilters, CefFileDialogCallback cefFileDialogCallback) {
		return false;
	}
}
