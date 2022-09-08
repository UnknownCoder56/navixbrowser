package com.uniqueapps.navixbrowser.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import org.cef.callback.CefDownloadItemCallback;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.object.DownloadObject;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadAction;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadState;

public class DownloadObjectPanel extends JPanel {

	private static final long serialVersionUID = 7157271078207817368L;

	public DownloadObject downloadObject;
	public CefDownloadItemCallback callback;
	public JProgressBar progressBar = new JProgressBar();
	public JPanel actions = new JPanel(new GridLayout(1, 0, 3, 5));
	public JLabel downloadSpeed = new JLabel("0 bytes/s");
	public JLabel partDone = new JLabel("0 bytes / 0 bytes");

	public DownloadObjectPanel(BrowserWindow browserWindow, DownloadObject downloadObject) {
		this.downloadObject = downloadObject;
		progressBar.setStringPainted(true);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(5, 40, 5, 40));
		JLabel textName = new JLabel(downloadObject.name);
		textName.setFont(textName.getFont().deriveFont(18.0F));
		JLabel textUrl = new JLabel(downloadObject.url);
		textUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				textUrl.setForeground(Color.BLUE);
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
		JLabel textDate = new JLabel(new SimpleDateFormat("dd MMMM yyyy hh:mm:ss aa").format(downloadObject.date));

		JButton pause = new JButton("Pause");
		if (downloadObject.downloadState == DownloadState.DOWNLOADING)
			pause.setEnabled(true);
		else
			pause.setEnabled(false);
		JButton resume = new JButton("Resume");
		resume.setEnabled(false);
		JButton cancel = new JButton("Cancel");
		if (downloadObject.downloadState == DownloadState.DOWNLOADING)
			pause.setEnabled(true);
		else
			pause.setEnabled(false);
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

		actions.add(pause);
		actions.add(resume);
		actions.add(cancel);
		actions.add(downloadSpeed);
		actions.add(partDone);

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
			add(progressBar, gbc3);
		}

		if (downloadObject.downloadState == DownloadState.DOWNLOADING) {
			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.ipady = 2;
			gbc4.anchor = GridBagConstraints.WEST;
			gbc4.weighty = 2;
			gbc4.gridy = 4;
			add(actions, gbc4);
		}
	}
}