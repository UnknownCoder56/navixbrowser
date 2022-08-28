package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.handler.CefDownloadHandlerAdapter;

import java.io.File;

public class NavixDownloadHandler extends CefDownloadHandlerAdapter {

    @Override
    public void onBeforeDownload(CefBrowser cefBrowser, CefDownloadItem cefDownloadItem, String suggestedFileName, CefBeforeDownloadCallback cefBeforeDownloadCallback) {
        super.onBeforeDownload(cefBrowser, cefDownloadItem, suggestedFileName, cefBeforeDownloadCallback);
        cefBeforeDownloadCallback.Continue(new File(new File(System.getProperty("user.home", "Downloads")), suggestedFileName).getAbsolutePath(), true);
    }

    @Override
    public void onDownloadUpdated(CefBrowser cefBrowser, CefDownloadItem cefDownloadItem, CefDownloadItemCallback cefDownloadItemCallback) {
        super.onDownloadUpdated(cefBrowser, cefDownloadItem, cefDownloadItemCallback);
    }
}
