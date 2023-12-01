package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefDragData;
import org.cef.handler.CefDragHandler;

public class NavixDragHandler implements CefDragHandler {
    @Override
    public boolean onDragEnter(CefBrowser browser, CefDragData dragData, int mask) {
        return false;
    }
}
