package com.uniqueapps.navixbrowser.handler;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandlerAdapter;

import com.uniqueapps.navixbrowser.component.BrowserTabbedPane;
import com.uniqueapps.navixbrowser.component.BrowserWindow;

import static com.uniqueapps.navixbrowser.component.BrowserTabbedPane.generateTabPanel;

import javax.swing.*;

public class NavixDisplayHandler extends CefDisplayHandlerAdapter {

	BrowserWindow windowFrame;
	BrowserTabbedPane tabbedPane;
	JTextField browserField;
	CefApp cefApp;

	public NavixDisplayHandler(BrowserWindow windowFrame, BrowserTabbedPane tabbedPane, JTextField browserField,
			CefApp cefApp) {
		this.windowFrame = windowFrame;
		this.tabbedPane = tabbedPane;
		this.browserField = browserField;
		this.cefApp = cefApp;
	}

	@Override
	public void onTitleChange(CefBrowser cefBrowser, String newTitle) {
		super.onTitleChange(cefBrowser, newTitle);
		if (BrowserTabbedPane.browserComponentMap.containsValue(cefBrowser)) {
			try {
				if (tabbedPane.getSelectedBrowser() == cefBrowser) {
					tabbedPane.setTitleAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()), newTitle);
					tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()),
							generateTabPanel(windowFrame, tabbedPane, cefApp, cefBrowser, newTitle, true));
				} else {
					tabbedPane.setTitleAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()), newTitle);
					tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()),
							generateTabPanel(windowFrame, tabbedPane, cefApp, cefBrowser, newTitle, false));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (cefBrowser == tabbedPane.getSelectedBrowser()) {
				windowFrame.setTitle(newTitle + " - Navix");
			}
		}
	}

	@Override
	public void onAddressChange(CefBrowser cefBrowser, CefFrame cefFrame, String newUrl) {
		super.onAddressChange(cefBrowser, cefFrame, newUrl);
		if (cefBrowser == tabbedPane.getSelectedBrowser()) {
			if (!newUrl.contains("newtab-dark.html") && !newUrl.contains("newtab-light.html")) {
				browserField.setText(newUrl);
			} else {
				browserField.setText("navix://home");
			}
		}
	}
}
