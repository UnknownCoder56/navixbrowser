package com.uniqueapps.navixbrowser.handler;

import com.uniqueapps.navixbrowser.component.BrowserWindow;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;

import javax.swing.*;

public class NavixRequestHandler extends CefRequestHandlerAdapter {

    NavixResourceRequestHandler navixResourceRequestHandler;
    BrowserWindow browserWindow;

    public NavixRequestHandler(BrowserWindow browserWindow) {
        navixResourceRequestHandler = new NavixResourceRequestHandler();
        this.browserWindow = browserWindow;
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame, CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator, BoolRef disableDefaultHandling) {
        return navixResourceRequestHandler;
    }

    @Override
    public void onRenderProcessTerminated(CefBrowser browser, TerminationStatus status, int error_code, String error_string) {
        super.onRenderProcessTerminated(browser, status, error_code, error_string);
        if (browserWindow.tabbedPane.getSelectedBrowser() == browser) {
            browserWindow.loadBar.setVisible(false);
                SwingUtilities.invokeLater(() -> JOptionPane.showOptionDialog(
                        browserWindow,
                        "Failed to render page with error \"" + error_string + "\"!",
                        "Failed to render page!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null));
        }
    }

    @Override
    public boolean onCertificateError(CefBrowser browser, CefLoadHandler.ErrorCode cert_error, String request_url, CefCallback callback) {
        if (browserWindow.tabbedPane.getSelectedBrowser() == browser) {
            SwingUtilities.invokeLater(() -> JOptionPane.showOptionDialog(
                    browserWindow,
                    "Certificate error \"" + cert_error.name() + "\" for URL \"" + request_url + "\"!",
                    "Certificate error!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    null,
                    null));
            callback.Continue();
        }
        return true;
    }
}
