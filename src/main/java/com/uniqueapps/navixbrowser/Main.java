package com.uniqueapps.navixbrowser;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.cef.CefApp;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.object.UserSettings;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;

public class Main {

	public static File userAppData = new File(".", "AppData");
	public static File settingsFile = new File(userAppData, "settings");
	public static UserSettings settings = new UserSettings();

	public static enum Theme {
		Dark, Light
	}

	public static void main(String[] args) {
		userAppData.mkdir();

		try {
			settingsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			refreshSettings();
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile))) {
			settings = (UserSettings) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			refreshSettings();
		}

		try {
			if (Main.settings.theme == Theme.Dark) {
				UIManager.setLookAndFeel(new FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
			}
			var window = new BrowserWindow("navix://home", settings.OSR, false);
			window.setSize(600, 400);
			window.setMinimumSize(new Dimension(400, 300));
			window.setTitle("Navix");
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		} catch (UnsupportedLookAndFeelException | IOException | UnsupportedPlatformException | InterruptedException
				| CefInitializationException e) {
			e.printStackTrace();
		}
	}

	public static void restart(CefApp cefApp) {
		userAppData.mkdir();

		try {
			settingsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			refreshSettings();
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile))) {
			settings = (UserSettings) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			refreshSettings();
		}

		try {
			if (Main.settings.theme == Theme.Dark) {
				UIManager.setLookAndFeel(new FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
			}
			var window = new BrowserWindow("navix://home", settings.OSR, false, cefApp);
			window.setSize(600, 400);
			window.setMinimumSize(new Dimension(400, 300));
			window.setTitle("Navix");
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		} catch (UnsupportedLookAndFeelException | IOException | UnsupportedPlatformException | InterruptedException
				| CefInitializationException e) {
			e.printStackTrace();
		}
	}

	public static void refreshSettings() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFile))) {
			oos.writeObject(settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
