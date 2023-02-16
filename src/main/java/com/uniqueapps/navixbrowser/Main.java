package com.uniqueapps.navixbrowser;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.safebrowsing.v4.Safebrowsing;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.component.DownloadObjectPanel;
import com.uniqueapps.navixbrowser.component.RuntimeDownloadHandler;
import com.uniqueapps.navixbrowser.handler.NavixAppHandler;
import com.uniqueapps.navixbrowser.object.DownloadObject;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadAction;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadState;
import com.uniqueapps.navixbrowser.object.UserSettings;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.cef.CefApp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static String VERSION = "2.0";
    public static int DEBUG_PORT = 8090;

    public static File userAppData = new File(".", "AppData");
    public static File cache = new File(".", "cache");
    public static File settingsFile = new File(userAppData, "settings");
    public static UserSettings settings = new UserSettings();

    public static File downloadsFile = new File(userAppData, "downloads");
    public static List<DownloadObject> downloads = new ArrayList<>();
    public static List<DownloadObjectPanel> downloadPanels = new ArrayList<>();
    public static Map<DownloadObject, DownloadAction> downloadsActionBuffer = new HashMap<>();

    public static File themes = new File(".", "themes");

    public static RuntimeDownloadHandler downloadWindow;

    public static Safebrowsing safebrowsing;

    public enum Theme {
        Modern, System
    }

    public static void main(String[] args) {
        try {
            safebrowsing = new Safebrowsing.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("Navix")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        userAppData.mkdir();
        cache.mkdir();
        loadSettings();
        prepareThemes();
        try {
            switch (Main.settings.theme) {
                case Modern:
                    UIManager.setLookAndFeel(getModernLookAndFeelForBackground());
                    break;
                case System:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        downloadWindow = new RuntimeDownloadHandler();
		CefApp cefApp = createCefApp();
		SwingUtilities.invokeLater(() -> start(cefApp));
    }

    public static void start(CefApp cefApp) {
        try {
            var window = new BrowserWindow("navix://home", settings.OSR, false, cefApp);
            loadData(window);
            window.setSize(600, 400);
            window.setMinimumSize(new Dimension(400, 300));
            window.setTitle("Navix");
            window.setLocationRelativeTo(null);
            window.setExtendedState(Main.settings.launchMaximized ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
            window.setVisible(true);
        } catch (IOException e) {
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

    private static CefApp createCefApp() {
        if (!new File(".", "jcef-bundle").exists()) {
            downloadWindow.setVisible(true);
        }

        CefAppBuilder builder = new CefAppBuilder();
        builder.getCefSettings().windowless_rendering_enabled = settings.OSR;
        builder.getCefSettings().user_agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.2704.106 Safari/537.36 Navix/"
                + VERSION;
        builder.getCefSettings().user_agent_product = "Navix " + VERSION;
        builder.getCefSettings().cache_path = cache.getAbsolutePath();
        builder.getCefSettings().remote_debugging_port = DEBUG_PORT;
        builder.setProgressHandler(downloadWindow);
        if (!Main.settings.HAL)
            builder.addJcefArgs("--disable-gpu");
        builder.addJcefArgs("--enable-media-stream");
        builder.setAppHandler(new NavixAppHandler());
        try {
            return builder.build();
        } catch (IOException | InterruptedException | UnsupportedPlatformException | CefInitializationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepareThemes() {
        if (themes.mkdir()) {
            try {
                Files.copy(Main.class.getResourceAsStream("/themes/FlatLaf.properties"),
                        new File(themes, "FlatLaf.properties").toPath());
                Files.copy(Main.class.getResourceAsStream("/themes/FlatLightLaf.properties"),
                        new File(themes, "FlatLightLaf.properties").toPath());
                Files.copy(Main.class.getResourceAsStream("/themes/FlatDarkLaf.properties"),
                        new File(themes, "FlatDarkLaf.properties").toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FlatLaf.registerCustomDefaultsSource(themes);
    }

    public static LookAndFeel getModernLookAndFeelForBackground() {
        Color bgColor = getBgColor();

        // Calculate the perceptive luminance (aka luma) - human eye favors green color...
        double luma = ((0.299 * bgColor.getRed()) + (0.587 * bgColor.getGreen()) + (0.114 * bgColor.getBlue())) / 255;

        // Return light theme for bright colors, dark theme for dark colors
        return luma > 0.5 ? new FlatLightLaf() : new FlatDarkLaf();
    }

    public static LookAndFeel getModernLookAndFeelForBackground(Color bgColor) {
        // Calculate the perceptive luminance (aka luma) - human eye favors green color...
        double luma = ((0.299 * bgColor.getRed()) + (0.587 * bgColor.getGreen()) + (0.114 * bgColor.getBlue())) / 255;

        // Return light theme for bright colors, dark theme for dark colors
        return luma > 0.5 ? new FlatLightLaf() : new FlatDarkLaf();
    }

    public static Color getTextColorForBackground() {
        Color bgColor = getBgColor();

        // Calculate the perceptive luminance (aka luma) - human eye favors green color...
        double luma = ((0.299 * bgColor.getRed()) + (0.587 * bgColor.getGreen()) + (0.114 * bgColor.getBlue())) / 255;

        // Return black for bright colors, white for dark colors
        return luma > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public static Color getTextColorForBackground(Color bgColor) {
        // Calculate the perceptive luminance (aka luma) - human eye favors green color...
        double luma = ((0.299 * bgColor.getRed()) + (0.587 * bgColor.getGreen()) + (0.114 * bgColor.getBlue())) / 255;

        // Return black for bright colors, white for dark colors
        return luma > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private static Color getBgColor() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./themes/FlatLaf.properties"))) {
            String hexColor = reader.readLine().substring(6);
            return Color.decode(hexColor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
