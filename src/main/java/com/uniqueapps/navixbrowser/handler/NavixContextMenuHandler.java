package com.uniqueapps.navixbrowser.handler;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.object.TransferableImage;
import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefContextMenuParams.EditStateFlags;
import org.cef.callback.CefContextMenuParams.MediaType;
import org.cef.callback.CefMenuModel;
import org.cef.handler.CefContextMenuHandlerAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class NavixContextMenuHandler extends CefContextMenuHandlerAdapter {

    private static final int BACK = 26500;
    private static final int FORWARD = 26501;
    private static final int REFRESH = 26502;
    private static final int ZOOM_IN = 26503;
    private static final int ZOOM_OUT = 26504;
    private static final int ZOOM_RESET = 26505;
    private static final int SCREENSHOT = 26506;
    private static final int VIEW_PAGE_SOURCE = 26507;
    private static final int INSPECT = 26508;
    private static final int CANCEL = 26509;

    private static final int COPY = 26510;
    private static final int SAVE_AS = 26511;

    private static final int OPEN_IN_NEW_TAB = 26512;
    private static final int SEARCH_IN_NEW_TAB = 26513;

    private static final int COPY_IMAGE_LINK = 26514;

    private static final int COPY_LINK_ADDRESS = 26515;

    CefApp cefApp;
    BrowserWindow browserWindow;

    public NavixContextMenuHandler(CefApp cefApp, BrowserWindow browserWindow) {
        this.cefApp = cefApp;
        this.browserWindow = browserWindow;
    }

    @Override
    public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params,
                                    CefMenuModel model) {

        super.onBeforeContextMenu(browser, frame, params, model);
        model.clear();

        if (params.getMediaType() != MediaType.CM_MEDIATYPE_IMAGE) {
            if (params.isEditable()) {
                Vector<String> suggestions = new Vector<>();
                params.getDictionarySuggestions(suggestions);
                switch (suggestions.size()) {
                    case 0:
                        break;
                    case 1:
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0, suggestions.get(0));
                        model.addSeparator();
                        break;
                    case 2:
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0, suggestions.get(0));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_1, suggestions.get(1));
                        model.addSeparator();
                        break;
                    case 3:
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0, suggestions.get(0));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_1, suggestions.get(1));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_2, suggestions.get(2));
                        model.addSeparator();
                        break;
                    case 5:
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0, suggestions.get(0));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_1, suggestions.get(1));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_2, suggestions.get(2));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_3, suggestions.get(3));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_4, suggestions.get(4));
                        model.addSeparator();
                        break;
                    default:
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0, suggestions.get(0));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_1, suggestions.get(1));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_2, suggestions.get(2));
                        model.addItem(CefMenuModel.MenuId.MENU_ID_SPELLCHECK_SUGGESTION_3, suggestions.get(3));
                        model.addSeparator();
                        break;
                }
                if ((params.getEditStateFlags()
                        & EditStateFlags.CM_EDITFLAG_CAN_CUT) == EditStateFlags.CM_EDITFLAG_CAN_CUT) {
                    model.addItem(CefMenuModel.MenuId.MENU_ID_CUT, "Cut");
                }
                if ((params.getEditStateFlags()
                        & EditStateFlags.CM_EDITFLAG_CAN_COPY) == EditStateFlags.CM_EDITFLAG_CAN_COPY) {
                    model.addItem(CefMenuModel.MenuId.MENU_ID_COPY, "Copy");
                }
                if ((params.getEditStateFlags()
                        & EditStateFlags.CM_EDITFLAG_CAN_PASTE) == EditStateFlags.CM_EDITFLAG_CAN_PASTE) {
                    model.addItem(CefMenuModel.MenuId.MENU_ID_PASTE, "Paste");
                }
                if ((params.getEditStateFlags()
                        & EditStateFlags.CM_EDITFLAG_CAN_DELETE) == EditStateFlags.CM_EDITFLAG_CAN_DELETE) {
                    model.addItem(CefMenuModel.MenuId.MENU_ID_DELETE, "Delete");
                }
                if ((params.getEditStateFlags()
                        & EditStateFlags.CM_EDITFLAG_CAN_SELECT_ALL) == EditStateFlags.CM_EDITFLAG_CAN_SELECT_ALL) {
                    model.addItem(CefMenuModel.MenuId.MENU_ID_SELECT_ALL, "Select All");
                }
                model.addSeparator();
                model.addItem(INSPECT, "Inspect");
                model.addSeparator();
            } else if (params.getSelectionText() != null && !params.getSelectionText().isEmpty()) {
                model.addItem(COPY, "Copy");
                if (isValidURL(params.getSelectionText())) {
                    model.addItem(OPEN_IN_NEW_TAB, "Open in new tab");
                } else {
                    model.addItem(SEARCH_IN_NEW_TAB, "Search for text in new tab");
                }
                model.addSeparator();
                model.addItem(INSPECT, "Inspect");
                model.addSeparator();
            } else if (params.getLinkUrl() != null && !params.getLinkUrl().isEmpty()) {
                if (isValidURL(params.getLinkUrl())) {
                    model.addItem(OPEN_IN_NEW_TAB, "Open in new tab");
                    if (params.getUnfilteredLinkUrl() != null && !params.getUnfilteredLinkUrl().isEmpty()) {
                        if (isValidURL(params.getUnfilteredLinkUrl())) {
                            model.addItem(COPY_LINK_ADDRESS, "Copy link address");
                        }
                    }
                    model.addSeparator();
                    model.addItem(INSPECT, "Inspect");
                    model.addSeparator();
                }
            } else {
                if (browser.canGoBack())
                    model.addItem(BACK, "Back");
                if (browser.canGoForward())
                    model.addItem(FORWARD, "Forward");
                model.addItem(REFRESH, "Refresh");
                model.addSeparator();
                model.addItem(ZOOM_IN, "Zoom in");
                model.addItem(ZOOM_OUT, "Zoom out");
                model.addItem(ZOOM_RESET, "Reset zoom level");
                model.addSeparator();
                model.addItem(SCREENSHOT, "Take screenshot");
                model.addSeparator();
                model.addItem(VIEW_PAGE_SOURCE, "View page source");
                model.addItem(INSPECT, "Inspect");
                model.addSeparator();
            }
        } else {
            model.addItem(COPY, "Copy image");
            model.addItem(COPY_IMAGE_LINK, "Copy image link");
            model.addItem(SAVE_AS, "Save image as");
            model.addSeparator();
            model.addItem(INSPECT, "Inspect");
            model.addSeparator();
        }
        model.addItem(CANCEL, "Cancel");
    }

    @Override
    public boolean onContextMenuCommand(CefBrowser browser, CefFrame frame, CefContextMenuParams params, int commandId,
                                        int eventFlags) {

        if (commandId == CANCEL)
            return false;
        var success = true;

        if (params.getMediaType() != MediaType.CM_MEDIATYPE_IMAGE) {
            if (params.isEditable()) {
                // Default JCEF handlers
                if (commandId == INSPECT) {
                    browserWindow.splitPane.setRightComponent(
                            browser.getDevTools(new Point(params.getXCoord(), params.getYCoord())).getUIComponent());
                    browserWindow.splitPane.setDividerLocation(1000);
                    return true;
                }
                return false;
            } else if (params.getSelectionText() != null && !params.getSelectionText().isEmpty()) {
                String selectedText = params.getSelectionText();
                if (commandId == COPY) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selectedText),
                            null);
                }
                if (isValidURL(params.getSelectionText())) {
                    if (commandId == OPEN_IN_NEW_TAB) {
                        browserWindow.tabbedPane.addBrowserTab(cefApp, selectedText, Main.settings.OSR, false);
                    }
                } else {
                    if (commandId == SEARCH_IN_NEW_TAB) {
                        browserWindow.tabbedPane.addBrowserTab(cefApp, Main.settings.searchEngine + selectedText,
                                Main.settings.OSR, false);
                    }
                }
                if (commandId == INSPECT) {
                    browserWindow.splitPane.setRightComponent(
                            browser.getDevTools(new Point(params.getXCoord(), params.getYCoord())).getUIComponent());
                    browserWindow.splitPane.setDividerLocation(1000);
                }
            } else if (params.getLinkUrl() != null && !params.getLinkUrl().isEmpty()) {
                if (isValidURL(params.getLinkUrl())) {
                    if (commandId == OPEN_IN_NEW_TAB) {
                        browserWindow.tabbedPane.addBrowserTab(cefApp, params.getLinkUrl(), Main.settings.OSR, false);
                    }
                    if (params.getUnfilteredLinkUrl() != null && !params.getUnfilteredLinkUrl().isEmpty()) {
                        if (isValidURL(params.getUnfilteredLinkUrl())) {
                            if (commandId == COPY_LINK_ADDRESS) {
                                Toolkit.getDefaultToolkit().getSystemClipboard()
                                        .setContents(new StringSelection(params.getUnfilteredLinkUrl()), null);
                            }
                        }
                    }
                    if (commandId == INSPECT) {
                        browserWindow.splitPane.setRightComponent(browser
                                .getDevTools(new Point(params.getXCoord(), params.getYCoord())).getUIComponent());
                        browserWindow.splitPane.setDividerLocation(1000);
                    }
                }
            } else {
                switch (commandId) {
                    case BACK:
                        browser.goBack();
                        break;
                    case FORWARD:
                        browser.goForward();
                        break;
                    case REFRESH:
                        browser.reload();
                        break;
                    case ZOOM_IN:
                        browser.setZoomLevel(browser.getZoomLevel() + 0.25);
                        break;
                    case ZOOM_OUT:
                        browser.setZoomLevel(browser.getZoomLevel() - 0.25);
                        break;
                    case ZOOM_RESET:
                        browser.setZoomLevel(0);
                        break;
                    case SCREENSHOT:
                        SwingUtilities.invokeLater(() -> {
                            try {
                                BufferedImage screenshot = Main.settings.OSR ?
                                        browser.createScreenshot(true).get() :
                                        getComponentScreenshot(browser.getUIComponent());
                                JFileChooser chooser = new JFileChooser();
                                chooser.setSelectedFile(new File("Navix_Screenshot_" + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) + (Main.settings.OSR ? ".jpg" : ".bmp")));
                                chooser.setFileFilter(new FileNameExtensionFilter(Main.settings.OSR ? "JPEG image" : "Bitmap image", Main.settings.OSR ? ".jpg" : ".bmp"));
                                int option1 = chooser.showSaveDialog(browserWindow);
                                if (option1 == JFileChooser.APPROVE_OPTION) {
                                    if (!chooser.getSelectedFile().createNewFile()) {
                                        int option2 = JOptionPane.showConfirmDialog(browserWindow,
                                                "File \"" + chooser.getSelectedFile().getName() +
                                                        "\" already exists. Do you want to overwrite it?",
                                                "File already exists",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.WARNING_MESSAGE);
                                        if (option2 == JOptionPane.YES_OPTION) {
                                            ImageIO.write(screenshot, Main.settings.OSR ? "jpg" : "bmp",
                                                    chooser.getSelectedFile());
                                        }
                                    } else {
                                        ImageIO.write(screenshot, Main.settings.OSR ? "jpg" : "bmp",
                                                chooser.getSelectedFile());
                                    }
                                }
                            } catch (IOException | ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    case VIEW_PAGE_SOURCE:
                        browserWindow.tabbedPane.addBrowserTab(cefApp, "view-source:" + frame.getURL(), Main.settings.OSR,
                                false);
                        break;
                    case INSPECT:
                        browserWindow.splitPane.setRightComponent(
                                browser.getDevTools(new Point(params.getXCoord(), params.getYCoord())).getUIComponent());
                        browserWindow.splitPane.setDividerLocation(1000);
                        break;
                    default:
                        success = false;
                        break;
                }
            }
        } else {
            switch (commandId) {
                case COPY:
                    try {
                        var image = ImageIO.read(new URL(params.getSourceUrl()));
                        if (image != null) {
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(image),
                                    null);
                        }
                    } catch (HeadlessException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COPY_IMAGE_LINK:
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(params.getSourceUrl()),
                            null);
                    break;
                case SAVE_AS:
                    SwingUtilities.invokeLater(() -> {
                        try {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setSelectedFile(new File("Navix_Image_" + LocalDateTime.now() + ".jpg"));
                            chooser.setFileFilter(new FileNameExtensionFilter("JPG image", ".jpg"));
                            int option = chooser.showSaveDialog(browserWindow);
                            if (option == JFileChooser.APPROVE_OPTION) {
                                ImageIO.write(ImageIO.read(new URL(params.getSourceUrl())), "jpg",
                                        chooser.getSelectedFile());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case INSPECT:
                    browserWindow.splitPane.setRightComponent(
                            browser.getDevTools(new Point(params.getXCoord(), params.getYCoord())).getUIComponent());
                    browserWindow.splitPane.setDividerLocation(0.7);
                default:
                    success = false;
                    break;
            }
        }
        return success;
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BufferedImage getComponentScreenshot(Component component) {
        Point point = component.getLocationOnScreen();
        Dimension dimension = component.getSize();
        Rectangle rectangle = new Rectangle(point, dimension);
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(rectangle);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
