package com.uniqueapps.navixbrowser.component;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cef.misc.CefPdfPrintSettings;

public class PrintFrame extends JFrame {

	private static final long serialVersionUID = -4993210211868881728L;
	BrowserWindow browserWindow;
    BrowserTabbedPane browserTabbedPane;
    CefPdfPrintSettings printSettings;

    public PrintFrame(BrowserWindow browserWindow, BrowserTabbedPane browserTabbedPane) {
        this.browserWindow = browserWindow;
        this.browserTabbedPane = browserTabbedPane;
        printSettings = new CefPdfPrintSettings();
        prepareFrame();
    }

    private void prepareFrame() {
        setTitle("Print to PDF");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(new BetterJLabel("Orientation:"));
        BetterJComboBox<String> orientation = new BetterJComboBox<>(new String[] { "Portrait", "Landscape" });
        panel.add(orientation);
        panel.add(new BetterJLabel("Print background colors and images:"));
        JCheckBox printBackground = new JCheckBox();
        printBackground.setSelected(printSettings.print_background);
        panel.add(printBackground);
        BetterJButton ok = new BetterJButton("OK");
        ok.addActionListener(l2 -> SwingUtilities.invokeLater(() -> {
            dispose();
            printSettings.landscape = orientation.getSelectedIndex() == 1;
            printSettings.print_background = printBackground.isSelected();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setDialogTitle("Save PDF");
            if (fileChooser.showSaveDialog(browserWindow) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getAbsolutePath() + ".pdf");
                }
                browserTabbedPane.getSelectedBrowser().printToPDF(file.getAbsolutePath(), printSettings,
                        (path, success) -> SwingUtilities.invokeLater(() -> {
                            if (success) {
                                JOptionPane.showMessageDialog(browserWindow, "PDF saved to " + path);
                            } else {
                                JOptionPane.showMessageDialog(browserWindow, "Failed to save PDF to " + path);
                            }
                        }));
            }
        }));
        panel.add(ok);
        BetterJButton cancel = new BetterJButton("Cancel");
        cancel.addActionListener(l2 -> dispose());
        panel.add(cancel);
        
        setContentPane(panel);
        pack();
    }
}
