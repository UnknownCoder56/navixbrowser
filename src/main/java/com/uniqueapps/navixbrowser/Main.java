package com.uniqueapps.navixbrowser;

import com.formdev.flatlaf.FlatDarkLaf;
import com.uniqueapps.navixbrowser.component.BrowserWindow;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;

import java.io.IOException;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
			var window = new BrowserWindow("navix://home", false, false);
			window.setSize(600, 400);
			window.setTitle("Navix");
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		} catch (UnsupportedLookAndFeelException | IOException | UnsupportedPlatformException | InterruptedException | CefInitializationException e) {
			e.printStackTrace();
		}
	}
}
