package com.uniqueapps.navixbrowser.listener;

import me.friwi.jcefmaven.EnumOS;
import me.friwi.jcefmaven.EnumPlatform;
import me.friwi.jcefmaven.UnsupportedPlatformException;

import javax.swing.*;

import com.uniqueapps.navixbrowser.component.BrowserTabbedPane;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class NavixComponentListener extends ComponentAdapter {

    JFrame windowFrame;
    BrowserTabbedPane tabbedPane;

    public NavixComponentListener(JFrame windowFrame, BrowserTabbedPane tabbedPane) {
        this.windowFrame = windowFrame;
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        try {
            if (EnumPlatform.getCurrentPlatform().getOs() == EnumOS.LINUX) {
                windowFrame.add(tabbedPane, BorderLayout.CENTER);
            }
        } catch (UnsupportedPlatformException e) {
            e.printStackTrace();
        }
    }
}
