package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;

public class NavixRequestHandler extends CefRequestHandlerAdapter {

    NavixResourceRequestHandler navixResourceRequestHandler;

    public NavixRequestHandler() {
        navixResourceRequestHandler = new NavixResourceRequestHandler();
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame, CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator, BoolRef disableDefaultHandling) {
        return navixResourceRequestHandler;
    }
}
