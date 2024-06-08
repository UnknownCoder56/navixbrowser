package com.uniqueapps.navixbrowser.handler;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLifeSpanHandlerAdapter;

public class NavixLifeSpanHandler extends CefLifeSpanHandlerAdapter {

    BrowserWindow browserWindow;

    public NavixLifeSpanHandler(BrowserWindow browserWindow) {
        this.browserWindow = browserWindow;
    }

    @Override
    public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url, String target_frame_name) {
        if (target_url != null && !target_url.isEmpty()) {
            browserWindow.tabbedPane.addBrowserTab(browserWindow.cefApp, target_url, Main.settings.OSR, false);
        }
        return true;
    }
}
