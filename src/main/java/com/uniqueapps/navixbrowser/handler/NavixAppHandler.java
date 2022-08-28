package com.uniqueapps.navixbrowser.handler;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.callback.CefSchemeRegistrar;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;

import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

public class NavixAppHandler extends MavenCefAppHandlerAdapter {

    public NavixAppHandler() {
        super();
    }

    @Override
    public void stateHasChanged(CefApp.CefAppState cefAppState) {
        super.stateHasChanged(cefAppState);
        if (cefAppState == CefApp.CefAppState.TERMINATED)
            System.exit(0);
    }

    @Override
    public void onRegisterCustomSchemes(CefSchemeRegistrar cefSchemeRegistrar) {
        super.onRegisterCustomSchemes(cefSchemeRegistrar);
        cefSchemeRegistrar.addCustomScheme(NavixSchemeHandler.scheme, true, false, false, false, true, false, false);
    }

    @Override
    public void onContextInitialized() {
        super.onContextInitialized();
        CefApp cefApp = CefApp.getInstance();
        cefApp.registerSchemeHandlerFactory(NavixSchemeHandler.scheme, "", new SchemeHandlerFactory());
    }

    private static class SchemeHandlerFactory implements CefSchemeHandlerFactory {

        @Override
        public CefResourceHandler create(CefBrowser cefBrowser, CefFrame cefFrame, String schemeName,
                CefRequest cefRequest) {
            if (schemeName.equals(NavixSchemeHandler.scheme)) {
                return new NavixSchemeHandler(cefBrowser);
            }
            return null;
        }
    }
}
