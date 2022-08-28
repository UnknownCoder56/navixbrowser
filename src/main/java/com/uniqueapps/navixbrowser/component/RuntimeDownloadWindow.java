package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;    

public class RuntimeDownloadWindow extends JFrame {

    public RuntimeDownloadWindow() {
        super();
        this.setLocationRelativeTo(null);
        this.add(new JLabel("Downloading runtime..."), BorderLayout.CENTER);
        JLabel label = new JLabel();
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        try {
            label.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/navix.png")).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(label, BorderLayout.WEST);
        this.pack();
    }
}