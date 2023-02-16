package com.uniqueapps.navixbrowser.component;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.formdev.flatlaf.ui.FlatTabbedPaneUI;
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

    private static final Color tabColorDarkMode = Color.DARK_GRAY.darker();
    private static final Color tabColorLightMode = Color.GRAY;

    private boolean dragging = false;
    private Point currentMouseLocation = null;
    private int draggedTabIndex = 0;

    public boolean settingsTabOpen = false;
    public boolean downloadsTabOpen = false;

    private int previousTabIndex = 0;

    private final FlatTabbedPaneUI flatTabbedPaneUI = new FlatTabbedPaneUI() {
        final Insets insets = new Insets(0, 0, 0, 0);

        @Override
        protected Insets getTabInsets(int tabPlacement, int tabIndex) {
            return new Insets(0, 1, 4, 1);
        }

        @Override
        protected Insets getTabAreaInsets(int tabPlacement) {
            return insets;
        }

        public Insets getInsets() {
            return insets;
        }

        @Override
        protected Insets getContentBorderInsets(int tabPlacement) {
            return insets;
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {

        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {

        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {

        }

        @Override
        protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

        }

        @Override
        protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

        }

        @Override
        protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

        }

        @Override
        protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

        }
    };
    private final BasicTabbedPaneUI basicTabbedPaneUI = new BasicTabbedPaneUI() {
        @Override
        protected LayoutManager createLayoutManager() {
            return new BasicTabbedPaneUI.TabbedPaneLayout() {
                @Override
                protected void calculateTabRects(int tabPlacement, int tabCount) {
                    final int spacer = 3;
                    final int indent = 3;
                    super.calculateTabRects(tabPlacement,tabCount);
                    for (int i = 0; i < rects.length; i++) {
                        rects[i].x += i * spacer + indent;
                    }
                }
            };
        }
    };

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
                             JButton backwardNav, JButton reloadButton, JButton addBookmarkButton, JTextField browserField, JTextField browserSearchField) {
        super();
        this.windowFrame = windowFrame;
        this.homeButton = homeButton;
        this.forwardNav = forwardNav;
        this.backwardNav = backwardNav;
        this.reloadButton = reloadButton;
        this.browserField = browserField;
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        if (Main.settings.theme != Theme.System) {
            setUI(flatTabbedPaneUI);
        } else {
            setUI(basicTabbedPaneUI);
        }
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (getTabCount() > 1) {
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
                if (getTabCount() > 1) {
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
            }
        });
        addChangeListener(l -> {
            windowFrame.setTitle(getTitleAt(getSelectedIndex()) + " - Navix");
            if (!browserSearchField.getText().isEmpty()) {
                browserComponentMap.get(getComponentAt(previousTabIndex)).stopFinding(true);
                browserSearchField.setText("");
            }
            previousTabIndex = getSelectedIndex();
            if (getSelectedBrowser() == null) {

                windowFrame.loadBar.setVisible(false);

                browserSearchField.setEnabled(false);
                browserField.setText("");
                browserField.setEnabled(false);
                homeButton.setEnabled(false);
                forwardNav.setEnabled(false);
                backwardNav.setEnabled(false);
                reloadButton.setEnabled(false);
                addBookmarkButton.setEnabled(false);
                if (windowFrame.splitPane.getRightComponent() != null) {
                    windowFrame.splitPane.setRightComponent(null);
                }
            } else if (!getSelectedBrowser().getURL().contains("newtab-dark.html") && !getSelectedBrowser().getURL().contains("newtab-light.html")) {

                if (getSelectedBrowser().isLoading()) {
                    windowFrame.loadBar.setIndeterminate(false);
                    windowFrame.loadBar.setValue(0);
                    windowFrame.loadBar.setIndeterminate(true);
                    windowFrame.loadBar.setVisible(true);
                } else {
                    windowFrame.loadBar.setVisible(false);
                }

                browserSearchField.setEnabled(true);
                browserField.setEnabled(true);
                homeButton.setEnabled(true);
                forwardNav.setEnabled(getSelectedBrowser().canGoForward());
                backwardNav.setEnabled(getSelectedBrowser().canGoBack());
                reloadButton.setEnabled(true);
                addBookmarkButton.setEnabled(true);
                browserField.setText(getSelectedBrowser().getURL());
                if (windowFrame.splitPane.getRightComponent() != null) {
                    windowFrame.splitPane.setRightComponent(null);
                }
            } else {

                if (getSelectedBrowser().isLoading()) {
                    windowFrame.loadBar.setIndeterminate(false);
                    windowFrame.loadBar.setValue(0);
                    windowFrame.loadBar.setIndeterminate(true);
                    windowFrame.loadBar.setVisible(true);
                } else {
                    windowFrame.loadBar.setVisible(false);
                }

                browserSearchField.setEnabled(true);
                browserField.setEnabled(true);
                homeButton.setEnabled(true);
                forwardNav.setEnabled(getSelectedBrowser().canGoForward());
                backwardNav.setEnabled(getSelectedBrowser().canGoBack());
                reloadButton.setEnabled(true);
                addBookmarkButton.setEnabled(true);
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
        if (getTabCount() > 1) {
            if (dragging && currentMouseLocation != null) {
                int destTab = getUI().tabForCoordinate(BrowserTabbedPane.this,
                        Math.round(Math.round(currentMouseLocation.getX())), 10);
                if (destTab != -1) {
                    Rectangle bounds = getUI().getTabBounds(BrowserTabbedPane.this, destTab);
                    g2d.setColor(Main.getTextColorForBackground());
                    if (Main.settings.theme != Theme.System) {
                        g2d.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
                    } else {
                        g2d.drawLine(bounds.x - 2, bounds.y, bounds.x - 2, bounds.y + bounds.height);
                    }
                }
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
        setTabComponentAt(getTabCount() - 1, generateTabPanel(windowFrame, this, cefApp, cefBrowser, "New Tab"));
        setSelectedIndex(getTabCount() - 1);
    }

    public void addSettingsTab(CefApp cefApp) {
        if (!settingsTabOpen) {
            settingsTabOpen = true;
            var settingsDialog = new SettingsPanel(windowFrame);
            addTab("Settings", null, settingsDialog, "Navix settings");
            setTitleAt(getTabCount() - 1, "Settings");
            setTabComponentAt(getTabCount() - 1, generateSettingsTabPanel(windowFrame, this, cefApp, settingsDialog));
            setSelectedIndex(getTabCount() - 1);
        } else {
            setSelectedIndex(indexOfTab("Settings"));
        }
    }

    public void addDownloadsTab(CefApp cefApp) {
        if (!downloadsTabOpen) {
            downloadsTabOpen = true;
            var downloadsDialog = new DownloadsPanel();
            addTab("Downloads", null, downloadsDialog, "Navix downloads");
            setTitleAt(getTabCount() - 1, "Downloads");
            setTabComponentAt(getTabCount() - 1, generateDownloadsTabPanel(windowFrame, this, cefApp, downloadsDialog));
            setSelectedIndex(getTabCount() - 1);
        } else {
            setSelectedIndex(indexOfTab("Downloads"));
        }
    }

    public void removeBrowserTab(CefBrowser browser) {
        browserComponentMap.remove(browser.getUIComponent());
        removeTabAt(indexOfComponent(browser.getUIComponent()));
        browser.close(true);
    }

    public void removeSettingsTab(Component settingsDialog) {
        removeTabAt(indexOfComponent(settingsDialog));
        settingsTabOpen = false;
    }

    public void removeDownloadsTab(Component downloadsDialog) {
        removeTabAt(indexOfComponent(downloadsDialog));
        downloadsTabOpen = false;
    }

    public CefBrowser getSelectedBrowser() {
        return browserComponentMap.getOrDefault(getSelectedComponent(), null);
    }

    public static JPanel generateTabPanel(BrowserWindow windowFrame, BrowserTabbedPane tabbedPane, CefApp cefApp,
                                          CefBrowser cefBrowser, String newTitle) {
        tabbedPane.setToolTipTextAt(tabbedPane.indexOfComponent(cefBrowser.getUIComponent()), newTitle);
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
                if (Main.settings.theme != Theme.System) {
                    g2d.setColor(windowFrame.getBackground());
                    g2d.fill(g2d.getClip());

                    TopEnd topEnd = new TopEnd(getWidth(), getHeight(), 30);
                    GradientPaint gradientPaint = new GradientPaint(
                            0,
                            0,
                            getTabColor(),
                            0,
                            g2d.getClip().getBounds().height,
                            windowFrame.getBackground());
                    g2d.setPaint(gradientPaint);
                    g2d.fill(topEnd);
                }
            }
        };
        JLabel tabInfoLabel = new JLabel(newTitle);
        tabInfoLabel.setForeground(Main.getTextColorForBackground());
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
        if (Main.settings.theme != Theme.System) {
            closeTabButton.setBackground(new Color(0x0, true));
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
        }
        closeTabButton.setIcon(closeImage);
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
                                                  CefApp cefApp, Component settingsDialog) {
        JPanel tabPanel = new JPanel(new BorderLayout(4, 4)) {
            private static final long serialVersionUID = 3725666626083864341L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (Main.settings.theme != Theme.System) {
                    g2d.setColor(windowFrame.getBackground());
                    g2d.fill(g2d.getClip());

                    TopEnd topEnd = new TopEnd(getWidth(), getHeight(), 30);
                    GradientPaint gradientPaint = new GradientPaint(
                            0,
                            0,
                            getTabColor(),
                            0,
                            g2d.getClip().getBounds().height,
                            windowFrame.getBackground());
                    g2d.setPaint(gradientPaint);
                    g2d.fill(topEnd);
                }
            }
        };
        JLabel tabInfoLabel = new JLabel("Settings");
        tabInfoLabel.setForeground(Main.getTextColorForBackground());
        tabInfoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabPanel.add(tabInfoLabel, BorderLayout.CENTER);
        JButton closeTabButton = new JButton();
        closeTabButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        if (Main.settings.theme != Theme.System) {
            closeTabButton.setBackground(new Color(0x0, true));
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
        }
        closeTabButton.setIcon(closeImage);
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
                                                   CefApp cefApp, Component downloadsDialog) {
        JPanel tabPanel = new JPanel(new BorderLayout(4, 4)) {
            private static final long serialVersionUID = 3725666626083864341L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (Main.settings.theme != Theme.System) {
                    g2d.setColor(windowFrame.getBackground());
                    g2d.fill(g2d.getClip());

                    TopEnd topEnd = new TopEnd(getWidth(), getHeight(), 30);
                    GradientPaint gradientPaint = new GradientPaint(
                            0,
                            0,
                            getTabColor(),
                            0,
                            g2d.getClip().getBounds().height,
                            windowFrame.getBackground());
                    g2d.setPaint(gradientPaint);
                    g2d.fill(topEnd);
                }
            }
        };
        JLabel tabInfoLabel = new JLabel("Downloads");
        tabInfoLabel.setForeground(Main.getTextColorForBackground());
        tabInfoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabPanel.add(tabInfoLabel, BorderLayout.CENTER);
        JButton closeTabButton = new JButton();
        closeTabButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        if (Main.settings.theme != Theme.System) {
            closeTabButton.setBackground(new Color(0x0, true));
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
        }
        closeTabButton.setIcon(closeImage);
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
        if (Main.settings.theme != Theme.System) {
            setUI(flatTabbedPaneUI);
        } else {
            setUI(basicTabbedPaneUI);
        }
        if (windowFrame.tabbedPane.settingsTabOpen) {
            int index = indexOfTab("Settings");
            Component settingsDialog = getComponentAt(index);
            SwingUtilities.updateComponentTreeUI(settingsDialog);
            setTabComponentAt(index, generateSettingsTabPanel(windowFrame, this, windowFrame.cefApp, settingsDialog));
        }
        if (windowFrame.tabbedPane.downloadsTabOpen) {
            int index = indexOfTab("Downloads");
            Component downloadsDialog = getComponentAt(index);
            SwingUtilities.updateComponentTreeUI(downloadsDialog);
            setTabComponentAt(index, generateDownloadsTabPanel(windowFrame, this, windowFrame.cefApp, downloadsDialog));
        }
        for (int i = 0; i < getTabCount(); i++) {
            var browser = browserComponentMap.get(getComponentAt(i));
            if (browser != null) {
                if (browser.getURL().contains("newtab-dark.html") || browser.getURL().contains("newtab-light.html")) {
                    browser.loadURL("navix://home");
                }
                setTabComponentAt(i, generateTabPanel(windowFrame, this, windowFrame.cefApp, browser, getToolTipTextAt(i)));
            }
            Arrays.stream(((JPanel) getTabComponentAt(i)).getComponents())
                    .filter(component -> component instanceof JLabel).findFirst().ifPresent(component ->
                            component.setForeground(Main.getTextColorForBackground()));
        }
        SwingUtilities.invokeLater(() -> Main.downloadPanels.forEach(SwingUtilities::updateComponentTreeUI));
        windowFrame.toolBar.setFloatable(false);
        windowFrame.toolBar2.setFloatable(false);
    }

    private static Color getTabColor() {
        return Main.getTextColorForBackground() == Color.WHITE ? tabColorDarkMode : tabColorLightMode;
    }

    private void addCefHandlers(CefApp cefApp, CefClient cefClient) {
        cefClient.addContextMenuHandler(new NavixContextMenuHandler(cefApp, windowFrame));
        cefClient.addDialogHandler(new NavixDialogHandler(windowFrame));
        cefClient.addDisplayHandler(new NavixDisplayHandler(windowFrame, BrowserTabbedPane.this, browserField, cefApp));
        cefClient.addDownloadHandler(new NavixDownloadHandler(windowFrame));
        cefClient.addFocusHandler(new NavixFocusHandler(windowFrame));
        cefClient.addLoadHandler(new NavixLoadHandler(forwardNav, backwardNav, windowFrame));
        cefClient.addDragHandler(new NavixDragHandler());
        cefClient.addRequestHandler(new NavixRequestHandler());
    }
}
