package com.uniqueapps.navixbrowser.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.apache.tika.Tika;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefFileDialogCallback;
import org.cef.handler.CefDialogHandler;

import com.uniqueapps.navixbrowser.Main;

public class NavixDialogHandler implements CefDialogHandler {

    JFrame windowFrame;

    public NavixDialogHandler(JFrame windowFrame) {
        this.windowFrame = windowFrame;
    }

    @Override
    public boolean onFileDialog(CefBrowser cefBrowser, FileDialogMode fileDialogMode, String title,
                                String defaultFilePath, Vector<String> acceptFilters,
                                Vector<String> acceptExtensions, Vector<String> acceptDescriptions,
                                CefFileDialogCallback cefFileDialogCallback) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            Main.logger.log(Level.SEVERE, "Failed to set LookAndFeel: {0}", e.getMessage());
        }

        JFileChooser chooser = new JFileChooser();
        File targetFile = new File(defaultFilePath);

        if (fileDialogMode != FileDialogMode.FILE_DIALOG_SAVE) {
            if (!acceptFilters.isEmpty()) {
                chooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        Tika tika = new Tika();
                        if (!file.isFile()) return true;
                        return acceptFilters.stream().anyMatch(s -> {
                            try {
                                if (s.contains("/")) {
                                    if (s.contains("*")) {
                                        return s.split("/")[0].equals(tika.detect(file).split("/")[0]);
                                    } else {
                                        return s.equals(tika.detect(file));
                                    }
                                }
                                if (s.contains(".")) {
                                    String required = tika.detect(s);
                                    String detected = tika.detect(file);
                                    if (required.contains("*")) {
                                        return required.split("/")[0].equals(detected.split("/")[0]);
                                    } else {
                                        return required.equals(detected);
                                    }
                                }
                                if (s.contains("|")) {
                                    String[] required = s.split("\\|")[1].split(";");
                                    String detected = tika.detect(file);
                                    for (String eachRequired : required) {
                                        String mimeRequired = tika.detect(eachRequired);
                                        if (mimeRequired.contains("*")) {
                                            return mimeRequired.split("/")[0].equals(detected.split("/")[0]);
                                        } else {
                                            return mimeRequired.equals(detected);
                                        }
                                    }
                                }
                                return false;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public String getDescription() {
                        return "Filter: " + acceptFilters;
                    }
                });
            }
        }
        chooser.setCurrentDirectory(targetFile.getParentFile());
        chooser.setSelectedFile(targetFile);
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(true);

        int result;
        switch (fileDialogMode) {
            case FILE_DIALOG_OPEN:
                chooser.setMultiSelectionEnabled(false);
                result = chooser.showOpenDialog(windowFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    cefFileDialogCallback.Continue(
                            new Vector<>(List.of(new String[] { chooser.getSelectedFile().getAbsolutePath() })));
                }
                break;
            case FILE_DIALOG_OPEN_MULTIPLE:
                chooser.setMultiSelectionEnabled(true);
                result = chooser.showOpenDialog(windowFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    List<String> selectedPaths = new ArrayList<>();
                    Arrays.stream(chooser.getSelectedFiles())
                            .forEach(file -> selectedPaths.add(file.getAbsolutePath()));
                    cefFileDialogCallback.Continue(new Vector<>(selectedPaths));
                }
                break;
            case FILE_DIALOG_SAVE:
                result = chooser.showSaveDialog(windowFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    cefFileDialogCallback.Continue(
                            new Vector<>(List.of(new String[] { chooser.getSelectedFile().getAbsolutePath() })));
                }
                break;
        }
        try {
            UIManager.setLookAndFeel(Main.getModernLookAndFeelForBackground());
        } catch (UnsupportedLookAndFeelException e) {
            Main.logger.log(Level.SEVERE, "Failed to set LookAndFeel: {0}", e.getMessage());
        }

        return true;
    }
}
