package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import com.uniqueapps.navixbrowser.Main;

import me.friwi.jcefmaven.EnumProgress;
import me.friwi.jcefmaven.IProgressHandler;

public class RuntimeDownloadHandler extends JFrame implements IProgressHandler {
	
	private static final long serialVersionUID = -1763136977454349368L;

	BetterJProgressBar progressBar = new BetterJProgressBar();
	BetterJLabel label = new BetterJLabel("Preparing runtime...");
	
	public RuntimeDownloadHandler() {
		super();
        label.setFont(label.getFont().deriveFont(20.0F));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        try {
            label.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/navix.png"))).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH)));
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "Failed to load app icon: {0}", e);
        }
        add(label, BorderLayout.CENTER);
        progressBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        progressBar.setValue(0);
        add(progressBar, BorderLayout.SOUTH);
        setSize(400, 300);
        try {
			setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/images/navix.png"))));
		} catch (IOException e) {
			Main.logger.log(Level.SEVERE, "Failed to load app icon: {0}", e);
		}
		setResizable(false);
        setLocationRelativeTo(null);
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