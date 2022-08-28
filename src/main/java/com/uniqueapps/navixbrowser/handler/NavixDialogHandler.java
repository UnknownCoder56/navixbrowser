package com.uniqueapps.navixbrowser.handler;

import org.apache.tika.Tika;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefFileDialogCallback;
import org.cef.handler.CefDialogHandler;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class NavixDialogHandler implements CefDialogHandler {

    JFrame windowFrame;

    public NavixDialogHandler(JFrame windowFrame) {
        this.windowFrame = windowFrame;
    }

    @Override
    public boolean onFileDialog(CefBrowser cefBrowser, FileDialogMode fileDialogMode, String title,
            String defaultFilePath, Vector<String> acceptFilters, CefFileDialogCallback cefFileDialogCallback) {
        JFileChooser chooser = new JFileChooser();
        boolean success = false;
        File targetFile = new File(defaultFilePath);

        if (acceptFilters.stream().anyMatch(s -> s.contains("/"))) {
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return acceptFilters.stream().anyMatch(s -> {
                        try {
                            if (s.contains("*")) {
                                return s.split("/")[0].equals(new Tika().detect(file).split("/")[0]);
                            } else {
                                return s.equals(new Tika().detect(file));
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                @Override
                public String getDescription() {
                    return "Mime type files " + acceptFilters;
                }
            });
        } else if (acceptFilters.stream().anyMatch(s -> s.contains("."))) {
            chooser.setFileFilter(new FileNameExtensionFilter("Supported files " + acceptFilters,
                    acceptFilters.stream().filter(s -> s.contains(".")).toArray(String[]::new)));
        } else {
            System.out.println(acceptFilters);
        }

        chooser.setCurrentDirectory(targetFile.getParentFile());
        chooser.setSelectedFile(targetFile);
        chooser.setDialogTitle(title);

        if (fileDialogMode == FileDialogMode.FILE_DIALOG_OPEN) {
            chooser.setMultiSelectionEnabled(false);
            int result = chooser.showOpenDialog(windowFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                cefFileDialogCallback
                        .Continue(new Vector<>(List.of(new String[] { chooser.getSelectedFile().getAbsolutePath() })));
                success = true;
            }
        } else if (fileDialogMode == FileDialogMode.FILE_DIALOG_OPEN_MULTIPLE) {
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showOpenDialog(windowFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                List<String> selectedPaths = new ArrayList<>();
                Arrays.stream(chooser.getSelectedFiles()).forEach(file -> selectedPaths.add(file.getAbsolutePath()));
                cefFileDialogCallback.Continue(new Vector<>(selectedPaths));
                success = true;
            }
        } else if (fileDialogMode == FileDialogMode.FILE_DIALOG_SAVE) {
            int result = chooser.showSaveDialog(windowFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                cefFileDialogCallback
                        .Continue(new Vector<>(List.of(new String[] { chooser.getSelectedFile().getAbsolutePath() })));
                success = true;
            }
        }

        if (!success) {
            cefFileDialogCallback.Cancel();
        }

        return success;
    }
}
