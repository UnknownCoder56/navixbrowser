package com.uniqueapps.navixbrowser.component;

import com.uniqueapps.navixbrowser.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DownloadsPanel extends JPanel {

	private static final long serialVersionUID = 3069119638383824437L;

	ScrollablePanel panel;
	JLabel title;

	public DownloadsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		panel = new ScrollablePanel();
		panel.setLayout(new GridBagLayout());
		title = new JLabel("Downloads");
		title.setFont(title.getFont().deriveFont(20F));
		title.setBorder(new EmptyBorder(0, 10, 0, 0));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 100;
		gbc.insets = new Insets(5, 0, 0, 0);
		panel.add(title, gbc);
		JScrollPane scrollPane = new JScrollPane(panel);
		if (!Main.downloadPanels.isEmpty()) {
			gbc.insets = new Insets(0, 0, 0, 0);
			Main.downloadPanels.forEach(downloadObjectPanel -> panel.add(downloadObjectPanel, gbc));
		} else {
			title.setBorder(new EmptyBorder(5, 5, 0, 0));
			title.setText("No downloads yet!");
		}
		add(scrollPane);
	}

	public void refreshDownloadPanels() {
		panel.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 100;
		gbc.insets = new Insets(5, 0, 0, 0);
		panel.add(title, gbc);
		if (!Main.downloadPanels.isEmpty()) {
			gbc.insets = new Insets(0, 0, 0, 0);
			Main.downloadPanels.forEach(downloadObjectPanel -> panel.add(downloadObjectPanel, gbc));
		} else {
			title.setBorder(new EmptyBorder(5, 5, 0, 0));
			title.setText("No downloads yet!");
		}
	}
}
