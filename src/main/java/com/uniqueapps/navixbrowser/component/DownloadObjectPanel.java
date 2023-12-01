package com.uniqueapps.navixbrowser.component;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.object.DownloadObject;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadAction;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadState;
import org.cef.callback.CefDownloadItemCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class DownloadObjectPanel extends JPanel {

	private static final long serialVersionUID = 7157271078207817368L;

	public DownloadObject downloadObject;
	public CefDownloadItemCallback callback;
	public BetterJProgressBar progressBar = new BetterJProgressBar();
	public JPanel actions = new JPanel(new GridBagLayout());
	public BetterJLabel downloadSpeed = new BetterJLabel("0 bytes/s");
	public BetterJLabel partDone = new BetterJLabel("0 bytes / 0 bytes");

	public DownloadObjectPanel(BrowserWindow browserWindow, DownloadObject downloadObject) {
		this.downloadObject = downloadObject;
		progressBar.setStringPainted(true);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(5, 40, 5, 40));
		BetterJLabel textName = new BetterJLabel(downloadObject.name);
		textName.setFont(textName.getFont().deriveFont(18.0F));
		BetterJLabel textUrl = new BetterJLabel(downloadObject.url);
		textUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				textUrl.setForeground(Color.BLUE.brighter().brighter());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				textUrl.setForeground(textName.getForeground());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				browserWindow.tabbedPane.addBrowserTab(browserWindow.cefApp, downloadObject.url, Main.settings.OSR,
						false);
			}
		});
		BetterJLabel textDate = new BetterJLabel(new SimpleDateFormat("dd MMMM yyyy hh:mm:ss aa").format(downloadObject.date));

		BetterJButton pause = new BetterJButton("Pause");
		pause.setEnabled(downloadObject.downloadState == DownloadState.DOWNLOADING);
		BetterJButton resume = new BetterJButton("Resume");
		resume.setEnabled(false);
		BetterJButton cancel = new BetterJButton("Cancel");
		downloadSpeed.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		partDone.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		pause.addActionListener(l -> {
			pause.setEnabled(false);
			resume.setEnabled(true);
			Main.downloadsActionBuffer.put(downloadObject, DownloadAction.PAUSE);
		});

		resume.addActionListener(l -> {
			resume.setEnabled(false);
			pause.setEnabled(true);
			callback.resume();
		});

		cancel.addActionListener(l -> {
			actions.setVisible(false);
			progressBar.setVisible(false);
			downloadObject.downloadState = DownloadState.FINISHED;
			if (callback != null) {
				callback.cancel();
			} else {
				Main.downloadsActionBuffer.put(downloadObject, DownloadAction.CANCEL);
			}
		});

		GridBagConstraints gbcA = new GridBagConstraints();
		gbcA.insets = new Insets(0, 5, 0, 5);
		actions.add(pause, gbcA);
		actions.add(resume, gbcA);
		actions.add(cancel, gbcA);
		actions.add(downloadSpeed, gbcA);
		actions.add(partDone, gbcA);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipady = 2;
		gbc.weightx = 100;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 3;
		add(textName, gbc);

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.ipady = 2;
		gbc1.anchor = GridBagConstraints.WEST;
		gbc1.weighty = 2;
		gbc1.gridy = 1;
		add(textUrl, gbc1);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.ipady = 2;
		gbc2.anchor = GridBagConstraints.WEST;
		gbc2.weighty = 2;
		gbc2.gridy = 2;
		add(textDate, gbc2);

		if (downloadObject.downloadState == DownloadState.DOWNLOADING) {
			GridBagConstraints gbc3 = new GridBagConstraints();
			gbc3.ipady = 2;
			gbc3.weighty = 1;
			gbc3.gridy = 3;
			gbc3.fill = GridBagConstraints.HORIZONTAL;
			gbc3.insets = new Insets(2, 0, 0, 0);
			add(progressBar, gbc3);
		}

		if (downloadObject.downloadState == DownloadState.DOWNLOADING) {
			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.ipady = 2;
			gbc4.anchor = GridBagConstraints.WEST;
			gbc4.weighty = 2;
			gbc4.gridy = 4;
			gbc4.insets = new Insets(2, 0, 0, 5);
			add(actions, gbc4);
		}
	}
}