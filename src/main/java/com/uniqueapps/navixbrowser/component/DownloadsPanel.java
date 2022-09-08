package com.uniqueapps.navixbrowser.component;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.uniqueapps.navixbrowser.Main;

public class DownloadsPanel extends JPanel {

	private static final long serialVersionUID = 3069119638383824437L;

	public DownloadsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ScrollablePanel panel = new ScrollablePanel();
		panel.setLayout(new GridLayout(0, 1));
		JLabel title = new JLabel("Downloads");
		title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		title.setBorder(new EmptyBorder(0, 10, 0, 0));
		panel.add(title);
		JScrollPane scrollPane = new JScrollPane(panel);
		if (!Main.downloadPanels.isEmpty()) {
		Main.downloadPanels.forEach(downloadPanel -> panel.add(downloadPanel));
		} else {
			title.setBorder(new EmptyBorder(5, 5, 0, 0));
			title.setText("No downloads yet!");
		}
		add(scrollPane);
	}
}
