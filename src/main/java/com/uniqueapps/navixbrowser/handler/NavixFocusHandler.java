package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefFocusHandlerAdapter;

import com.uniqueapps.navixbrowser.component.BrowserWindow;

import java.awt.*;

public class NavixFocusHandler extends CefFocusHandlerAdapter {

    BrowserWindow window;

    public NavixFocusHandler(BrowserWindow window) {
        this.window = window;
    }

    @Override
    public void onGotFocus(CefBrowser cefBrowser) {
        if (window.browserIsInFocus) return;
        window.browserIsInFocus = true;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        cefBrowser.setFocus(true);
    }

    @Override
    public void onTakeFocus(CefBrowser cefBrowser, boolean b) {
        window.browserIsInFocus = false;
    }
}
