package com.uniqueapps.navixbrowser.listener;

import com.uniqueapps.navixbrowser.component.BrowserWindow;
import org.cef.CefApp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NavixWindowListener extends WindowAdapter {

    BrowserWindow browserWindow;
    CefApp cefApp;

    public NavixWindowListener(BrowserWindow browserWindow, CefApp cefApp) {
        this.browserWindow = browserWindow;
        this.cefApp = cefApp;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        cefApp.dispose();
        browserWindow.dispose();
    }
}
