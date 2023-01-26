package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import me.friwi.jcefmaven.EnumProgress;
import me.friwi.jcefmaven.IProgressHandler;    

public class RuntimeDownloadHandler extends JFrame implements IProgressHandler {
	
	static final long serialVersionUID = -1763136977454349368L;

	BrowserWindow browser;
	JProgressBar progressBar = new JProgressBar();
	JLabel label = new JLabel("Preparing runtime...");
	
	public RuntimeDownloadHandler(BrowserWindow browser) {
        super();
        this.browser = browser;
        label.setFont(label.getFont().deriveFont(20.0F));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        try {
            label.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/navix.png")).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(label, BorderLayout.CENTER);
        progressBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        progressBar.setValue(0);
        this.add(progressBar, BorderLayout.SOUTH);
        this.setSize(400, 300);
        try {
			this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/images/navix.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.setLocationRelativeTo(null);
    }

	@Override
	public void handleProgress(EnumProgress state, float percent) {
		if (state != null) {
			String cap = state.toString().substring(0, 1).toUpperCase() + state.toString().substring(1).toLowerCase();
			label.setText(cap + " runtime...");
			if (percent != -1F) {
				progressBar.setValue(Math.round(percent));
			}
		} else {
			throw new RuntimeException("State cannot be null!");
		}
	}
}