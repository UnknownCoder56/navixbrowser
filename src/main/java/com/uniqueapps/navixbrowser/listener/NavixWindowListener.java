package com.uniqueapps.navixbrowser.listener;

import org.cef.CefApp;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NavixWindowListener extends WindowAdapter {

    JFrame windowFrame;
    CefApp cefApp;

    public NavixWindowListener(JFrame windowFrame, CefApp cefApp) {
        this.windowFrame = windowFrame;
        this.cefApp = cefApp;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        cefApp.dispose();
        windowFrame.dispose();
    }
}
