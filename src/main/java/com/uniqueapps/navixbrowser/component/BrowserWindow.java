package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.cef.CefApp;
import org.cef.CefClient;

import com.uniqueapps.navixbrowser.handler.NavixAppHandler;
import com.uniqueapps.navixbrowser.handler.NavixDialogHandler;
import com.uniqueapps.navixbrowser.handler.NavixDisplayHandler;
import com.uniqueapps.navixbrowser.handler.NavixDownloadHandler;
import com.uniqueapps.navixbrowser.handler.NavixFocusHandler;
import com.uniqueapps.navixbrowser.handler.NavixLoadHandler;
import com.uniqueapps.navixbrowser.listener.NavixComponentListener;
import com.uniqueapps.navixbrowser.listener.NavixWindowListener;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;

public class BrowserWindow extends JFrame {

    private static final long serialVersionUID = -3658310837225120769L;
    protected final CefApp cefApp;
    private final CefClient cefClient;
    private final JTextField browserAddressField;
    private final JButton homeButton;
    private final JButton forwardNav;
    private final JButton backwardNav;
    private final JButton reloadButton;
    private final JButton addTabButton;
    private final BrowserTabbedPane tabbedPane;
    public boolean browserIsInFocus = false;

    public BrowserWindow(String startURL, boolean useOSR, boolean isTransparent)
            throws IOException, UnsupportedPlatformException, InterruptedException, CefInitializationException {

        RuntimeDownloadWindow downloadWindow = null;
        if (!new File(".", "jcef-bundle").exists()) {
            downloadWindow = new RuntimeDownloadWindow();
            downloadWindow.setVisible(true);
        }
        
        File cache = new File(".", "cache");
        cache.mkdirs();

        File resources = new File(".", "resources");
        if (resources.mkdirs()) {
            Files.copy(getClass().getResourceAsStream("/resources/navix.ico"),
                    new File(new File(".", "resources"), "navix.ico").toPath());
            Files.copy(getClass().getResourceAsStream("/resources/newtab.html"),
                    new File(new File(".", "resources"), "newtab.html").toPath());
            Files.copy(getClass().getResourceAsStream("/resources/style.css"),
                    new File(new File(".", "resources"), "style.css").toPath());
        }

        CefAppBuilder builder = new CefAppBuilder();

        builder.getCefSettings().windowless_rendering_enabled = useOSR;
        builder.getCefSettings().user_agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.2704.106 Safari/537.36 Navix/0.5";
        builder.getCefSettings().user_agent_product = "Navix 0.5";
        builder.getCefSettings().cache_path = cache.getAbsolutePath();
        builder.setAppHandler(new NavixAppHandler());

        cefApp = builder.build();
        cefClient = cefApp.createClient();

        if (downloadWindow != null) {
            downloadWindow.setVisible(false);
        }

        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/navix.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        homeButton = new JButton();
        backwardNav = new JButton();
        forwardNav = new JButton();
        reloadButton = new JButton();
        addTabButton = new JButton();
        browserAddressField = new JTextField(100) {
            @Override
            protected void paintComponent(Graphics g) {
                Map<RenderingHints.Key, Object> rh = new HashMap<>();
                rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHints(rh);
                super.paintComponent(g2d);
            }
        };

        try {
            tabbedPane = new BrowserTabbedPane(this, forwardNav, backwardNav, browserAddressField);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addCefHandlers();
        addListeners();
        prepareNavBar(startURL, useOSR, isTransparent);

        tabbedPane.addBrowserTab(cefApp, startURL, useOSR, isTransparent);
    }

    private void addCefHandlers() {
        cefClient.addDialogHandler(new NavixDialogHandler(this));
        cefClient.addDisplayHandler(new NavixDisplayHandler(this, tabbedPane, browserAddressField, cefApp));
        cefClient.addDownloadHandler(new NavixDownloadHandler());
        cefClient.addFocusHandler(new NavixFocusHandler(this));
        cefClient.addLoadHandler(new NavixLoadHandler(forwardNav, backwardNav, this));
    }

    private void addListeners() {
        // A hack to enable browser resizing in non-OSR mode
        addComponentListener(new NavixComponentListener(this, tabbedPane));
        addWindowListener(new NavixWindowListener(this, cefApp));
    }

    private void prepareNavBar(String startURL, boolean useOSR, boolean isTransparent) {
        browserAddressField.addActionListener(l -> {
            String query = browserAddressField.getText();
            try {
                new URL(query);
                tabbedPane.getSelectedBrowser().loadURL(query);
            } catch (MalformedURLException e) {
                if (query.contains(".") || query.contains("://")) {
                    tabbedPane.getSelectedBrowser().loadURL(query);
                } else {
                    tabbedPane.getSelectedBrowser().loadURL("https://slsearch.cf/search?q=" + query);
                }
            }
        });
        browserAddressField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!browserIsInFocus)
                    return;
                browserIsInFocus = false;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                browserAddressField.requestFocusInWindow();
            }
        });
        browserAddressField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                browserAddressField.selectAll();
            }
        });

        browserAddressField.setBorder(new RoundedBorder(Color.LIGHT_GRAY.darker(), 1, 28, 5));
        browserAddressField.setBackground(this.getBackground());
        browserAddressField.setFont(new JLabel().getFont());

        backwardNav.setEnabled(false);
        forwardNav.setEnabled(false);

        homeButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        backwardNav.setBorder(new EmptyBorder(0, 0, 0, 0));
        forwardNav.setBorder(new EmptyBorder(0, 0, 0, 0));
        reloadButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        addTabButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        homeButton.setBackground(this.getBackground());
        backwardNav.setBackground(this.getBackground());
        forwardNav.setBackground(this.getBackground());
        reloadButton.setBackground(this.getBackground());
        addTabButton.setBackground(this.getBackground());

        try {
            homeButton.setIcon(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/home.png")))
                            .getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH)));
            backwardNav.setIcon(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/left-chevron.png")))
                            .getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH)));
            forwardNav.setIcon(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/right-chevron.png")))
                            .getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH)));
            reloadButton.setIcon(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/reload.png")))
                            .getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH)));
            addTabButton.setIcon(new ImageIcon(
                    ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/add.png")))
                            .getScaledInstance(30, 30, BufferedImage.SCALE_SMOOTH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        homeButton.addActionListener(l -> tabbedPane.getSelectedBrowser().loadURL("navix://home"));
        backwardNav.addActionListener(l -> {
            if (tabbedPane.getSelectedBrowser().canGoBack()) {
                tabbedPane.getSelectedBrowser().goBack();
            }
        });
        forwardNav.addActionListener(l -> {
            if (tabbedPane.getSelectedBrowser().canGoForward()) {
                tabbedPane.getSelectedBrowser().goForward();
            }
        });
        reloadButton.addActionListener(
                l -> tabbedPane.getSelectedBrowser().loadURL(tabbedPane.getSelectedBrowser().getURL()));
        addTabButton.addActionListener(l -> tabbedPane.addBrowserTab(cefApp, startURL, useOSR, isTransparent));

        JPanel separatorPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.DARK_GRAY.brighter());
                g.drawLine(getWidth() / 2, 3, getWidth() / 2, getHeight() - 3);
            }
        };

        JPanel navBar = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 3, 8, 3);

        gbc.gridx = 0;
        gbc.weightx = 0.1;
        navBar.add(addTabButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        navBar.add(separatorPanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        navBar.add(backwardNav, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.1;
        navBar.add(forwardNav, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.1;
        navBar.add(reloadButton, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.1;
        navBar.add(homeButton, gbc);

        gbc.gridx = 6;
        gbc.weightx = 50;
        gbc.fill = GridBagConstraints.BOTH;
        navBar.add(browserAddressField, gbc);

        getContentPane().add(navBar, BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}
