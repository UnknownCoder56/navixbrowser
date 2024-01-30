package com.uniqueapps.navixbrowser.handler;

import com.uniqueapps.navixbrowser.component.BrowserTabbedPane;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandlerAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import com.uniqueapps.navixbrowser.Main;

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

			if (cefBrowser == tabbedPane.getSelectedBrowser()) {
				windowFrame.setTitle(newTitle + " - Navix");
			}

			try {
				JPanel tabPanel = (JPanel) tabbedPane.getTabComponentAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()));

				tabbedPane.setTitleAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()), newTitle);

				tabbedPane.setToolTipTextAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()), newTitle);

				if (newTitle.length() > 15) {
					newTitle = newTitle.substring(0, 12) + "...";
				}

				for (Component component : tabPanel.getComponents()) {
					if (component instanceof JLabel) {
						JLabel tabInfoLabel = (JLabel) component;
						tabInfoLabel.setText(newTitle);
						SwingUtilities.invokeLater(() -> {
							try {
								tabInfoLabel.setIcon(new ImageIcon(
										ImageIO.read(new URL("https://www.google.com/s2/favicons?domain=" + cefBrowser.getURL()))));
							} catch (IOException e) {
								tabInfoLabel.setIcon(null);
							}
						});
					}
				}
			} catch (Exception e) {
				Main.logger.log(Level.SEVERE, "Error while updating tab title: {0}", e);
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
		windowFrame.suggestionsPopupMenu.setVisible(false);
	}
	
	@Override
	public boolean onTooltip(CefBrowser browser, String text) {
		windowFrame.tooltip.setText(text);
		windowFrame.tooltip.setVisible(true);
		windowFrame.tooltipTimer.restart();
		return true;
	}
}
