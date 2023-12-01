package com.uniqueapps.navixbrowser.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefPrintDialogCallback;
import org.cef.callback.CefPrintJobCallback;
import org.cef.handler.CefPrintHandlerAdapter;
import org.cef.misc.CefPrintSettings;

import java.awt.*;

public class NavixPrintHandler extends CefPrintHandlerAdapter {

    @Override
    public boolean onPrintDialog(CefBrowser browser, boolean hasSelection, CefPrintDialogCallback callback) {
        return super.onPrintDialog(browser, hasSelection, callback);
    }

    @Override
    public void onPrintSettings(CefBrowser browser, CefPrintSettings settings, boolean getDefaults) {
        super.onPrintSettings(browser, settings, getDefaults);
    }

    @Override
    public boolean onPrintJob(CefBrowser browser, String documentName, String pdfFilePath, CefPrintJobCallback callback) {
        return super.onPrintJob(browser, documentName, pdfFilePath, callback);
    }

    @Override
    public void onPrintStart(CefBrowser browser) {
        super.onPrintStart(browser);
    }

    @Override
    public void onPrintReset(CefBrowser browser) {
        super.onPrintReset(browser);
    }

    @Override
    public Dimension getPdfPaperSize(CefBrowser browser, int deviceUnitsPerInch) {
        return super.getPdfPaperSize(browser, deviceUnitsPerInch);
    }
}
