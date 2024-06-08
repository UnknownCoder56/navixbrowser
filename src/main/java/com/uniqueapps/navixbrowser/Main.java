package com.uniqueapps.navixbrowser;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.cef.CefApp;
import org.cef.CefSettings;
import org.cef.OS;

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

public class Main {

    public static final String VERSION = "2.1";
    public static int DEBUG_PORT;

    public static File userAppData = new File(".", "data");
    public static File cache = new File(".", "cache");
    public static UserSettings settings = new UserSettings();
    public static File downloadsFile = new File(userAppData, "downloads");
    public static List<DownloadObject> downloads = new ArrayList<>();
    public static List<DownloadObjectPanel> downloadPanels = new ArrayList<>();
    public static Map<DownloadObject, DownloadAction> downloadsActionBuffer = new HashMap<>();

    public static File themes = new File(".", "themes");

    public static RuntimeDownloadHandler downloadWindow;

    public static Safebrowsing safebrowsing;

    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public enum Theme {
        Modern, System, CrossPlatform
    }

    public static void main(String[] args) {
        try {
            FileHandler fileHandler = new FileHandler("./navix.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            safebrowsing = new Safebrowsing.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null)
                .setApplicationName("Navix")
                .build();
        } catch (GeneralSecurityException | IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize Safebrowsing API: {0}", e.getMessage());
        }
        userAppData.mkdir();
        cache.mkdir();
        loadSettings();
        DEBUG_PORT = settings.debugPort;
        prepareThemes();
        try {
            switch (Main.settings.theme) {
                case Modern:
                    UIManager.setLookAndFeel(getModernLookAndFeelForBackground());
                    break;
                case System:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case CrossPlatform:
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    break;
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            logger.log(Level.SEVERE, "Failed to set LookAndFeel: {0}", e.getMessage());
        }

        downloadWindow = new RuntimeDownloadHandler();
        CefApp cefApp = createCefApp();
        SwingUtilities.invokeLater(() -> start(cefApp));
    }

    public static void start(CefApp cefApp) {
        try {
            var window = new BrowserWindow(settings.newTabURL, settings.OSR, false, cefApp);
            loadData(window);
            window.setSize(600, 400);
            window.setMinimumSize(new Dimension(400, 300));
            window.setTitle("Navix");
            window.setLocationRelativeTo(null);
            window.setExtendedState(settings.launchMaximized ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
            window.setVisible(true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create BrowserWindow: {0}", e.getMessage());
        }
    }

    public static void loadSettings() {
        settings.load();
    }

    @SuppressWarnings("unchecked")
    public static void loadData(BrowserWindow browserWindow) {
        try {
            downloadsFile.createNewFile();
        } catch (IOException e) {
            if (!downloadsFile.exists()) {
                logger.log(Level.SEVERE, "Failed to create downloads file: {0}", e.getMessage());
            }
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
            refreshDownloads();
        }
    }

    public static void refreshSettings() {
        settings.save();
    }

    public static void refreshDownloads() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(downloadsFile))) {
            oos.writeObject(downloads);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save downloads: {0}", e.getMessage());
        }
    }

    private static CefApp createCefApp() {
        if (!new File(".", "jcef-bundle").exists()) {
            downloadWindow.setVisible(true);
        }

        CefAppBuilder builder = new CefAppBuilder();
        builder.getCefSettings().windowless_rendering_enabled = settings.OSR;
        builder.getCefSettings().user_agent_product = "Navix " + VERSION;
        builder.getCefSettings().cache_path = cache.getAbsolutePath();
        builder.getCefSettings().remote_debugging_port = DEBUG_PORT;
        builder.setProgressHandler(downloadWindow);
        if (!Main.settings.HAL)
            builder.addJcefArgs("--disable-gpu");
        builder.addJcefArgs("--enable-media-stream");
        builder.addJcefArgs(Main.settings.args);
        builder.setAppHandler(new NavixAppHandler());
        try {
            CefApp cefApp = builder.build();
            CefSettings cefSettings = builder.getCefSettings();
            if (OS.isLinux()) {
                cefSettings.user_agent = "Mozilla/5.0 (Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + cefApp.getVersion().getChromeVersion() + " Safari/537.36 Navix/"
                        + VERSION;
            } else if (OS.isWindows()) {
                cefSettings.user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; WOW64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + cefApp.getVersion().getChromeVersion() + " Safari/537.36 Navix/"
                        + VERSION;
            } else if (OS.isMacintosh()) {
                cefSettings.user_agent = "Mozilla/5.0 (Macintosh) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + cefApp.getVersion().getChromeVersion() + " Safari/537.36 Navix/"
                        + VERSION;
            } else {
                cefSettings.user_agent = "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + cefApp.getVersion().getChromeVersion() + " Safari/537.36 Navix/"
                        + VERSION;
            }
            cefApp.setSettings(cefSettings);
            return cefApp;
        } catch (IOException | InterruptedException | UnsupportedPlatformException | CefInitializationException e) {
            logger.log(Level.SEVERE, "Failed to create CefApp: {0}", e.getMessage());
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
                logger.log(Level.SEVERE, "Failed to prepare theme files: {0}", e.getMessage());
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
            logger.log(Level.SEVERE, "Failed to read theme file: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
