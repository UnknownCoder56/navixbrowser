package com.uniqueapps.navixbrowser.component;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.Main.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 4379679088941442158L;

	public SettingsPanel(BrowserWindow browserWindow) {
		Map<String, String> engines = new HashMap<>();
		engines.put("Google", "https://google.com/search?q=");
		engines.put("Bing", "https://bing.com/search?q=");
		engines.put("Yahoo", "https://search.yahoo.com/search?p=");
		engines.put("Yandex", "https://yandex.com/search/?text=");
		engines.put("Ecosia", "https://www.ecosia.org/search?q=");
		engines.put("SLSearch", "https://slsearch.cf/search?q=");
		engines.put("DuckDuckGo", "https://duckduckgo.com/?q=");
		engines.put("Brave", "https://search.brave.com/search?q=");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel supreme = new JPanel(new BorderLayout());
		JPanel parent = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new GridBagLayout());
		parent.add(panel, BorderLayout.NORTH);

		JLabel title = new JLabel("Settings");
		title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 10, 5, 5);
		gbc.ipady = 12;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(title, gbc);

		JLabel hal = new JLabel("Hardware acceleration");
		hal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.anchor = GridBagConstraints.WEST;
		gbc1.insets = new Insets(0, 15, 5, 5);
		gbc1.gridx = 0;
		gbc1.gridy = 1;
		panel.add(hal, gbc1);

		JCheckBox halEnabled = new JCheckBox();
		halEnabled.setSelected(Main.settings.HAL);
		halEnabled.addChangeListener(l -> {
			if (halEnabled.isSelected() != Main.settings.HAL) {
				Main.settings.HAL = halEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(5, 1, 5, 0);
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.gridx = 1;
		gbc2.gridy = 1;
		panel.add(halEnabled, gbc2);

		JLabel osr = new JLabel("Off-screen rendering");
		osr.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.anchor = GridBagConstraints.WEST;
		gbc3.insets = new Insets(0, 15, 5, 5);
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		panel.add(osr, gbc3);

		JCheckBox osrEnabled = new JCheckBox();
		osrEnabled.setSelected(Main.settings.OSR);
		osrEnabled.addChangeListener(l -> {
			if (osrEnabled.isSelected() != Main.settings.OSR) {
				Main.settings.OSR = osrEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.insets = new Insets(5, 1, 5, 0);
		gbc4.anchor = GridBagConstraints.NORTHWEST;
		gbc4.gridx = 1;
		gbc4.gridy = 2;
		panel.add(osrEnabled, gbc4);

		JLabel searchEngine = new JLabel("Preferred search engine");
		searchEngine.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.ipadx = 18;
		gbc5.anchor = GridBagConstraints.WEST;
		gbc5.insets = new Insets(0, 15, 5, 5);
		gbc5.gridx = 0;
		gbc5.gridy = 3;
		panel.add(searchEngine, gbc5);

		JComboBox<String> searchEngineList = new JComboBox<>();
		searchEngineList.setModel(new DefaultComboBoxModel<>(engines.keySet().toArray(new String[] {})));
		Object[] enginesArray = engines.values().toArray();
		for (int i = 0; i < enginesArray.length; i++) {
			if (enginesArray[i].equals(Main.settings.searchEngine)) {
				searchEngineList.setSelectedIndex(i);
			}
		}
		searchEngineList.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selectedEngine = engines.get((String) searchEngineList.getSelectedItem());
				if (!Objects.equals(selectedEngine, Main.settings.searchEngine)) {
					Main.settings.searchEngine = selectedEngine;
					Main.refreshSettings();
				}
			}
		});
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.insets = new Insets(5, 5, 5, 0);
		gbc6.anchor = GridBagConstraints.NORTH;
		gbc6.fill = GridBagConstraints.HORIZONTAL;
		gbc6.weightx = 10.0;
		gbc6.gridx = 1;
		gbc6.gridy = 3;
		panel.add(searchEngineList, gbc6);

		JLabel theme = new JLabel("Theme");
		theme.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.anchor = GridBagConstraints.WEST;
		gbc7.insets = new Insets(0, 15, 5, 5);
		gbc7.gridx = 0;
		gbc7.gridy = 4;
		panel.add(theme, gbc7);

		JComboBox<Theme> themeList = new JComboBox<>();
		themeList.setModel(new DefaultComboBoxModel<>(Theme.values()));
		themeList.setSelectedItem(Main.settings.theme);
		themeList.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Theme selectedTheme = (Theme) themeList.getSelectedItem();
				if (selectedTheme != Main.settings.theme) {
					Main.settings.theme = selectedTheme;
					Main.refreshSettings();
					try {
						if (Main.settings.theme == Theme.Dark) {
							UIManager.setLookAndFeel(new FlatDarkLaf());
						} else {
							UIManager.setLookAndFeel(new FlatLightLaf());
						}
					} catch (UnsupportedLookAndFeelException e1) {
						e1.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(browserWindow);
					browserWindow.tabbedPane.applyThemeChange();
				}
			}
		});
		GridBagConstraints gbc8 = new GridBagConstraints();
		gbc8.insets = new Insets(5, 5, 5, 0);
		gbc8.anchor = GridBagConstraints.NORTH;
		gbc8.fill = GridBagConstraints.HORIZONTAL;
		gbc8.weightx = 10.0;
		gbc8.gridx = 1;
		gbc8.gridy = 4;
		panel.add(themeList, gbc8);

		JLabel launchMaximized = new JLabel("Launch maximized");
		launchMaximized.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc9 = new GridBagConstraints();
		gbc9.anchor = GridBagConstraints.WEST;
		gbc9.insets = new Insets(0, 15, 5, 5);
		gbc9.gridx = 0;
		gbc9.gridy = 5;
		panel.add(launchMaximized, gbc9);

		JCheckBox launchMaximizedEnabled = new JCheckBox();
		launchMaximizedEnabled.setSelected(Main.settings.launchMaximized);
		launchMaximizedEnabled.addChangeListener(l -> {
			if (launchMaximizedEnabled.isSelected() != Main.settings.launchMaximized) {
				Main.settings.launchMaximized = launchMaximizedEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc10 = new GridBagConstraints();
		gbc10.insets = new Insets(5, 1, 5, 0);
		gbc10.anchor = GridBagConstraints.NORTHWEST;
		gbc10.gridx = 1;
		gbc10.gridy = 5;
		panel.add(launchMaximizedEnabled, gbc10);

		JLabel bottomInfo = new JLabel("Version " + Main.VERSION + " (Chromium "
				+ browserWindow.cefApp.getVersion().getChromeVersion() + ")."
				+ " Changes to graphics settings will be applied on next launch.");

		supreme.add(new JScrollPane(parent), BorderLayout.CENTER);
		supreme.add(bottomInfo, BorderLayout.SOUTH);
		add(supreme);
	}
}
