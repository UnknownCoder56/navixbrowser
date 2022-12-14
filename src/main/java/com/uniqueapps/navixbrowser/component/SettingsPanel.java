package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.Main.Theme;

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
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
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
		GridBagConstraints gbc10 = new GridBagConstraints();
		gbc10.insets = new Insets(5, 1, 5, 0);
		gbc10.anchor = GridBagConstraints.NORTHWEST;
		gbc10.gridx = 1;
		gbc10.gridy = 5;
		panel.add(launchMaximizedEnabled, gbc10);

		JPanel confirmationButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(l -> {
			halEnabled.setSelected(Main.settings.HAL);
			osrEnabled.setSelected(Main.settings.OSR);
			searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
			themeList.setSelectedItem(Main.settings.theme);
		});

		JButton ok = new JButton("OK");
		ok.addActionListener(l -> {
			if (Main.settings.HAL != halEnabled.isSelected() || Main.settings.OSR != osrEnabled.isSelected()) {
				Main.settings.HAL = halEnabled.isSelected();
				Main.settings.OSR = osrEnabled.isSelected();
				Main.settings.launchMaximized = launchMaximizedEnabled.isSelected();
				Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
				Main.settings.theme = (Theme) themeList.getSelectedItem();
				Main.refreshSettings();
				JOptionPane.showMessageDialog(this, "App will shut down to apply changes. Relaunch it.");
				browserWindow.dispatchEvent(new WindowEvent(browserWindow, WindowEvent.WINDOW_CLOSING));
			} else {
				Main.settings.HAL = halEnabled.isSelected();
				Main.settings.OSR = osrEnabled.isSelected();
				Main.settings.launchMaximized = launchMaximizedEnabled.isSelected();
				Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
				Main.settings.theme = (Theme) themeList.getSelectedItem();
				Main.refreshSettings();
				JOptionPane.showMessageDialog(this, "App will be restarted to apply settings.");
				browserWindow.setVisible(false);
				browserWindow.dispose();
				Main.start(browserWindow.cefApp);
			}
		});

		confirmationButtons.add(ok);
		confirmationButtons.add(cancel);

		GridBagConstraints gbc11 = new GridBagConstraints();
		gbc11.anchor = GridBagConstraints.SOUTHEAST;
		gbc11.gridx = 1;
		gbc11.gridy = 7;
		panel.add(confirmationButtons, gbc11);

		supreme.add(new JScrollPane(parent), BorderLayout.CENTER);
		add(supreme);
		
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		themeList.setSelectedItem(Main.settings.theme);
	}
}
