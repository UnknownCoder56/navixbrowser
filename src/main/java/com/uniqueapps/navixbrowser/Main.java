package com.uniqueapps.navixbrowser;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.cef.CefApp;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.component.DownloadObjectPanel;
import com.uniqueapps.navixbrowser.object.DownloadObject;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadAction;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadState;
import com.uniqueapps.navixbrowser.object.UserSettings;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;

public class Main {

	public static File userAppData = new File(".", "AppData");
	public static File settingsFile = new File(userAppData, "settings");
	public static UserSettings settings = new UserSettings();
	
	public static File downloadsFile = new File(userAppData, "downloads");
	public static List<DownloadObject> downloads = new ArrayList<>();
	public static List<DownloadObjectPanel> downloadPanels = new ArrayList<>();
	public static Map<DownloadObject, DownloadAction> downloadsActionBuffer = new HashMap<>();

	public static enum Theme {
		Dark, Light
	}

	public static void main(String[] args) {
		userAppData.mkdir();
		start(null);
	}

	public static void start(CefApp cefApp) {
		try {
			loadSettings();
			if (Main.settings.theme == Theme.Dark) {
				UIManager.setLookAndFeel(new FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
			}
			var window = new BrowserWindow("navix://home", settings.OSR, false, cefApp);
			loadData(window);
			window.setSize(600, 400);
			window.setMinimumSize(new Dimension(400, 300));
			window.setTitle("Navix");
			window.setLocationRelativeTo(null);
			window.setExtendedState(Main.settings.launchMaximized ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
			window.setVisible(true);
		} catch (UnsupportedLookAndFeelException | IOException | UnsupportedPlatformException | InterruptedException
				| CefInitializationException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSettings() {
		try {
			settingsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile))) {
			settings = (UserSettings) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadData(BrowserWindow browserWindow) {
		try {
			downloadsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			refreshDownloads();
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(downloadsFile))) {
			downloads = (List<DownloadObject>) ois.readObject();
			downloads.forEach(downloadObject -> {
				if (downloadObject.downloadState == DownloadState.DOWNLOADING) {
					downloadObject.downloadState = DownloadState.FINISHED;
				}
				downloadPanels.add(new DownloadObjectPanel(browserWindow, downloadObject));
			});
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			refreshDownloads();
		}
	}

	public static void refreshSettings() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFile))) {
			oos.writeObject(settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void refreshDownloads() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(downloadsFile))) {
			oos.writeObject(downloads);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
