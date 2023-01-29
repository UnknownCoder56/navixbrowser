package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.uniqueapps.navixbrowser.handler.*;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.Main.Theme;

public class BrowserTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = -3055974049370144677L;
    BrowserWindow windowFrame;
    JButton homeButton, forwardNav, backwardNav, reloadButton;
    JTextField browserField;
    public static Map<Component, CefBrowser> browserComponentMap = new HashMap<>();
    private static final ImageIcon closeImage;
    private static final Color activeTabColor = new Color(46, 57, 115);
    private static final Color inactiveTabColorDarkMode = Color.DARK_GRAY.darker();
    private static final Color inactiveTabColorLightMode = Color.GRAY;

    private boolean dragging = false;
    private Point currentMouseLocation = null;
    private int draggedTabIndex = 0;

    static {
        try {
            closeImage = new ImageIcon(ImageIO
                    .read(Objects.requireNonNull(BrowserTabbedPane.class.getResourceAsStream("/images/cross.png")))
                    .getScaledInstance(20, 20, BufferedImage.SCALE_SMOOTH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BrowserTabbedPane(BrowserWindow windowFrame, JButton homeButton, JButton forwardNav,
                             JButton backwardNav, JButton reloadButton, JTextField browserField) {
        super();
        this.windowFrame = windowFrame;
        this.homeButton = homeButton;
        this.forwardNav = forwardNav;
        this.backwardNav = backwardNav;
        this.reloadButton = reloadButton;
        this.browserField = browserField;
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setUI(new BasicTabbedPaneUI() {
            final Insets insets = new Insets(0, 0, 0, 0);

            @Override
            protected Insets getTabInsets(int tabPlacement, int tabIndex) {
                return insets;
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                          boolean isSelected) {
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) {
                    // Gets the tab index based on the mouse position
                    int tabNumber = getUI().tabForCoordinate(BrowserTabbedPane.this, e.getX(), e.getY());
                    if (tabNumber >= 0) {
                        draggedTabIndex = tabNumber;
                        dragging = true;
                        repaint();
                    }
                } else {
                    currentMouseLocation = e.getPoint();
                    repaint();
                }

                super.mouseDragged(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu() {
                        private static final long serialVersionUID = 3325907722226518327L;

                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g;
                            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            super.paintComponent(g2d);
                        }
                    };

                    JMenuItem copyLinkItem = new JMenuItem("Copy URL");
                    copyLinkItem
                            .addActionListener(l -> Toolkit.getDefaultToolkit().getSystemClipboard()
                                    .setContents(new StringSelection(browserComponentMap
                                            .get(BrowserTabbedPane.this.getComponentAt(BrowserTabbedPane.this.getUI()
                                                    .tabForCoordinate(BrowserTabbedPane.this, e.getX(), e.getY())))
                                            .getURL()), null));
                    popupMenu.add(copyLinkItem);

                    JMenuItem closeTabItem = new JMenuItem("Close tab");
                    closeTabItem.addActionListener(l -> {
                        if (BrowserTabbedPane.this.getTabCount() > 1) {
                            BrowserTabbedPane.this.removeBrowserTab(
                                    browserComponentMap.get(BrowserTabbedPane.this.getComponentAt(BrowserTabbedPane.this
                                            .getUI().tabForCoordinate(BrowserTabbedPane.this, e.getX(), e.getY()))));
                        } else {
                            windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
                            windowFrame.cefApp.dispose();
                            windowFrame.dispose();
                        }
                    });
                    popupMenu.add(closeTabItem);

                    popupMenu.show(BrowserTabbedPane.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragging) {
                    int tabNumber = getUI().tabForCoordinate(BrowserTabbedPane.this, e.getX(), 10);
                    if (tabNumber >= 0) {
                        Component comp = getComponentAt(draggedTabIndex);
                        Component tabComp = getTabComponentAt(draggedTabIndex);
                        String title = getTitleAt(draggedTabIndex);
                        removeTabAt(draggedTabIndex);
                        insertTab(title, null, comp, null, tabNumber);
                        setTabComponentAt(tabNumber, tabComp);
                        setSelectedIndex(tabNumber);
                    }
                }
                dragging = false;
            }
        });
        addChangeListener(l -> {
            windowFrame.setTitle(getTitleAt(getSelectedIndex()) + " - Navix");
            for (int i = 0; i < getTabCount(); i++) {
                Component c = getTabComponentAt(i);
                if (c != null) {
                    c.setBackground(getSelectedIndex() == i ? activeTabColor : getInactiveTabColor());
                }
            }
            if (getSelectedBrowser() == null) {
                browserField.setText("");
                browserField.setEnabled(false);
                homeButton.setEnabled(false);
                forwardNav.setEnabled(false);
                backwardNav.setEnabled(false);
                reloadButton.setEnabled(false);
                if (windowFrame.splitPane.getRightComponent() != null) {
                    windowFrame.splitPane.setRightComponent(null);
                }
            } else if (!getSelectedBrowser().getURL().contains("newtab-dark.html") && !getSelectedBrowser().getURL().contains("newtab-light.html")) {
                browserField.setEnabled(true);
                homeButton.setEnabled(true);
                forwardNav.setEnabled(getSelectedBrowser().canGoForward());
                backwardNav.setEnabled(getSelectedBrowser().canGoBack());
                reloadButton.setEnabled(true);
                browserField.setText(getSelectedBrowser().getURL());
                if (windowFrame.splitPane.getRightComponent() != null) {
                    windowFrame.splitPane.setRightComponent(null);
                }
            } else {
                browserField.setEnabled(true);
                homeButton.setEnabled(true);
                forwardNav.setEnabled(getSelectedBrowser().canGoForward());
                backwardNav.setEnabled(getSelectedBrowser().canGoBack());
                reloadButton.setEnabled(true);
                browserField.setText("navix://home");
                if (windowFrame.splitPane.getRightComponent() != null) {
                    windowFrame.splitPane.setRightComponent(null);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Map<RenderingHints.Key, Object> rh = new HashMap<>();
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        if (dragging && currentMouseLocation != null) {
            int destTab = getUI().tabForCoordinate(BrowserTabbedPane.this,
                    Math.round(Math.round(currentMouseLocation.getX())), 10);
            if (destTab != -1) {
                Rectangle bounds = getUI().getTabBounds(BrowserTabbedPane.this, destTab);
                g2d.setColor(Main.settings.theme == Theme.Dark ? Color.WHITE : Color.BLACK);
                g2d.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
            }
        }
        super.paintComponent(g2d);
    }

    public void addBrowserTab(CefApp cefApp, String startURL, boolean useOSR, boolean isTransparent) {
        var cefClient = cefApp.createClient();
        addCefHandlers(cefApp, cefClient);
        var cefBrowser = cefClient.createBrowser(startURL, useOSR, isTransparent);
        browserComponentMap.put(cefBrowser.getUIComponent(), cefBrowser);
        addTab("New Tab", null, cefBrowser.getUIComponent(), cefBrowser.getURL());
        setTabComponentAt(getTabCount() - 1, generateTabPanel(windowFrame, this, cefApp, cefBrowser, "New Tab", true));
        setSelectedIndex(getTabCount() - 1);
    }

    public void addSettingsTab(CefApp cefApp) {
        var settingsDialog = new SettingsPanel(windowFrame);
        addTab("Settings", null, settingsDialog, "Navix settings");
        setTitleAt(getTabCount() - 1, "Settings");
        setTabComponentAt(getTabCount() - 1, generateSettingsTabPanel(windowFrame, this, cefApp, settingsDialog, true));
        setSelectedIndex(getTabCount() - 1);
    }

    public void addDownloadsTab(CefApp cefApp) {
        var downloadsDialog = new DownloadsPanel();
        addTab("Downloads", null, downloadsDialog, "Navix downloads");
        setTitleAt(getTabCount() - 1, "Downloads");
        setTabComponentAt(getTabCount() - 1, generateDownloadsTabPanel(windowFrame, this, cefApp, downloadsDialog, true));
        setSelectedIndex(getTabCount() - 1);
    }

    public void removeBrowserTab(CefBrowser browser) {
        browserComponentMap.remove(browser.getUIComponent());
        removeTabAt(indexOfComponent(browser.getUIComponent()));
        browser.close(true);
    }

    public void removeSettingsTab(Component settingsDialog) {
        removeTabAt(indexOfComponent(settingsDialog));
    }

    public void removeDownloadsTab(Component downloadsDialog) {
        removeTabAt(indexOfComponent(downloadsDialog));
    }

    public CefBrowser getSelectedBrowser() {
        return browserComponentMap.getOrDefault(getSelectedComponent(), null);
    }

    public static JPanel generateTabPanel(BrowserWindow windowFrame, BrowserTabbedPane tabbedPane, CefApp cefApp,
                                          CefBrowser cefBrowser, String newTitle, boolean highlightTab) {
        if (newTitle.length() > 15) {
            newTitle = newTitle.substring(0, 12) + "...";
        }
        JPanel tabPanel = new JPanel(new BorderLayout(4, 4)) {
            private static final long serialVersionUID = 3725666626083864341L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Area panelArea = new Area(g2d.getClip());
                Area roundedArea = new Area(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 8, 20, 20));
                panelArea.subtract(roundedArea);
                g2d.setColor(windowFrame.getBackground());
                g2d.fill(panelArea);
            }
        };
        tabPanel.setBackground(highlightTab ? activeTabColor : getInactiveTabColor());
        JLabel tabInfoLabel = new JLabel(newTitle);
        tabInfoLabel.setForeground(Color.WHITE);
        tabInfoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        SwingUtilities.invokeLater(() -> {
            try {
                tabInfoLabel.setIcon(new ImageIcon(
                        ImageIO.read(new URL("https://www.google.com/s2/favicons?domain=" + cefBrowser.getURL()))));
            } catch (IOException ignored) {
            }
        });
        tabPanel.add(tabInfoLabel, BorderLayout.CENTER);
        JButton closeTabButton = new JButton();
        closeTabButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        closeTabButton.setBackground(new Color(0x0, true));
        closeTabButton.setIcon(closeImage);
        closeTabButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeTabButton.setBackground(new Color(0, 0, 0, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeTabButton.setBackground(new Color(0x0, true));
            }
        });
        closeTabButton.addActionListener(l -> {
            if (tabbedPane.getTabCount() > 1) {
                tabbedPane.removeBrowserTab(cefBrowser);
            } else {
                windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
                cefApp.dispose();
                windowFrame.dispose();
            }
        });
        tabPanel.add(closeTabButton, BorderLayout.EAST);
        return tabPanel;
    }

    public static JPanel generateSettingsTabPanel(BrowserWindow windowFrame, BrowserTabbedPane tabbedPane,
                                                  CefApp cefApp, Component settingsDialog, boolean highlightTab) {
        JPanel tabPanel = new JPanel(new BorderLayout(4, 4)) {
            private static final long serialVersionUID = 3725666626083864341L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Area panelArea = new Area(g2d.getClip());
                Area roundedArea = new Area(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 8, 20, 20));
                panelArea.subtract(roundedArea);
                g2d.setColor(windowFrame.getBackground());
                g2d.fill(panelArea);
            }
        };
        tabPanel.setBackground(highlightTab ? activeTabColor : getInactiveTabColor());
        JLabel tabInfoLabel = new JLabel("Settings");
        tabInfoLabel.setForeground(Color.WHITE);
        tabInfoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabPanel.add(tabInfoLabel, BorderLayout.CENTER);
        JButton closeTabButton = new JButton();
        closeTabButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        closeTabButton.setBackground(new Color(0x0, true));
        closeTabButton.setIcon(closeImage);
        closeTabButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeTabButton.setBackground(new Color(0, 0, 0, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeTabButton.setBackground(new Color(0x0, true));
            }
        });
        closeTabButton.addActionListener(l -> {
            if (tabbedPane.getTabCount() > 1) {
                tabbedPane.removeSettingsTab(settingsDialog);
            } else {
                windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
                cefApp.dispose();
                windowFrame.dispose();
            }
        });
        tabPanel.add(closeTabButton, BorderLayout.EAST);
        return tabPanel;
    }

    public static JPanel generateDownloadsTabPanel(BrowserWindow windowFrame, BrowserTabbedPane tabbedPane,
                                                   CefApp cefApp, Component downloadsDialog, boolean highlightTab) {
        JPanel tabPanel = new JPanel(new BorderLayout(4, 4)) {
            private static final long serialVersionUID = 3725666626083864341L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Area panelArea = new Area(g2d.getClip());
                Area roundedArea = new Area(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 8, 20, 20));
                panelArea.subtract(roundedArea);
                g2d.setColor(windowFrame.getBackground());
                g2d.fill(panelArea);
            }
        };
        tabPanel.setBackground(highlightTab ? activeTabColor : getInactiveTabColor());
        JLabel tabInfoLabel = new JLabel("Downloads");
        tabInfoLabel.setForeground(Color.WHITE);
        tabInfoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabPanel.add(tabInfoLabel, BorderLayout.CENTER);
        JButton closeTabButton = new JButton();
        closeTabButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        closeTabButton.setBackground(new Color(0x0, true));
        closeTabButton.setIcon(closeImage);
        closeTabButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeTabButton.setBackground(new Color(0, 0, 0, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeTabButton.setBackground(new Color(0x0, true));
            }
        });
        closeTabButton.addActionListener(l -> {
            if (tabbedPane.getTabCount() > 1) {
                tabbedPane.removeDownloadsTab(downloadsDialog);
            } else {
                windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
                cefApp.dispose();
                windowFrame.dispose();
            }
        });
        tabPanel.add(closeTabButton, BorderLayout.EAST);
        return tabPanel;
    }

    public void applyThemeChange() {
        setUI(new BasicTabbedPaneUI() {
            final Insets insets = new Insets(0, 0, 0, 0);

            @Override
            protected Insets getTabInsets(int tabPlacement, int tabIndex) {
                return insets;
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                          boolean isSelected) {
            }
        });
        for (int i = 0; i < getTabCount(); i++) {
            var browser = browserComponentMap.get(getComponentAt(i));
            if (browser != null) {
                if (browser.getURL().contains("newtab-dark.html") || browser.getURL().contains("newtab-light.html")) {
                    browser.loadURL("navix://home");
                }
            }
        }
    }

    private static Color getInactiveTabColor() {
        return Main.settings.theme == Theme.Dark ? inactiveTabColorDarkMode : inactiveTabColorLightMode;
    }

    private void addCefHandlers(CefApp cefApp, CefClient cefClient) {
        cefClient.addContextMenuHandler(new NavixContextMenuHandler(cefApp, windowFrame));
        cefClient.addDialogHandler(new NavixDialogHandler(windowFrame));
        cefClient.addDisplayHandler(new NavixDisplayHandler(windowFrame, BrowserTabbedPane.this, browserField, cefApp));
        cefClient.addDownloadHandler(new NavixDownloadHandler(windowFrame));
        cefClient.addFocusHandler(new NavixFocusHandler(windowFrame));
        cefClient.addLoadHandler(new NavixLoadHandler(forwardNav, backwardNav, windowFrame));
        cefClient.addDragHandler(new NavixDragHandler());
    }
}
