package com.uniqueapps.navixbrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.object.UserSettings;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;

public class Main {

	public static File userAppData = new File(".", "AppData");
	public static File settingsFile = new File(userAppData, "settings"); 
	public static UserSettings settings = new UserSettings();
	
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
			UIManager.setLookAndFeel(new FlatDarkLaf());
			var window = new BrowserWindow("navix://home", settings.OSR, false);
			window.setSize(600, 400);
			window.setTitle("Navix");
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		} catch (UnsupportedLookAndFeelException | IOException | UnsupportedPlatformException | InterruptedException | CefInitializationException e) {
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
