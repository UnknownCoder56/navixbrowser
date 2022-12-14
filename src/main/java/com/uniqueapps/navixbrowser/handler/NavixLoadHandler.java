package com.uniqueapps.navixbrowser.handler;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.network.CefRequest.TransitionType;

import com.formdev.flatlaf.FlatDarkLaf;
import com.uniqueapps.navixbrowser.component.BrowserWindow;

public class NavixLoadHandler extends CefLoadHandlerAdapter {

	JButton forwardNav, backwardNav;
	BrowserWindow browserWindow;

	public NavixLoadHandler(JButton forwardNav, JButton backwardNav, BrowserWindow browserWindow) {
		this.forwardNav = forwardNav;
		this.backwardNav = backwardNav;
		this.browserWindow = browserWindow;
	}

	@Override
	public void onLoadStart(CefBrowser browser, CefFrame frame, TransitionType transitionType) {
		super.onLoadStart(browser, frame, transitionType);
		browserWindow.loadBar.setIndeterminate(false);
		browserWindow.loadBar.setValue(0);
		browserWindow.loadBar.setIndeterminate(true);
		browserWindow.loadBar.setVisible(true);
	}

	@Override
	public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
		super.onLoadEnd(browser, frame, httpStatusCode);
		browserWindow.loadBar.setVisible(false);
	}

	@Override
	public void onLoadingStateChange(CefBrowser cefBrowser, boolean b, boolean b1, boolean b2) {
		super.onLoadingStateChange(cefBrowser, b, b1, b2);
		forwardNav.setEnabled(cefBrowser.canGoForward());
		backwardNav.setEnabled(cefBrowser.canGoBack());
	}

	@Override
	public void onLoadError(CefBrowser cefBrowser, CefFrame cefFrame, ErrorCode errorCode, String s, String s1) {
		super.onLoadError(cefBrowser, cefFrame, errorCode, s, s1);
		browserWindow.loadBar.setVisible(false);
		new Thread(() -> {
			if (errorCode.getCode() != -3) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JOptionPane.showMessageDialog(cefBrowser.getUIComponent(),
							"Failed to load " + cefBrowser.getURL() + " with error code " + errorCode.getCode() + "!",
							JOptionPane.MESSAGE_PROPERTY, JOptionPane.ERROR_MESSAGE);
					UIManager.setLookAndFeel(new FlatDarkLaf());
					if (cefBrowser.canGoBack()) {
						cefBrowser.goBack();
					} else {
						cefBrowser.loadURL("navix://home");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
