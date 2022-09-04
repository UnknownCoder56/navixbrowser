package com.uniqueapps.navixbrowser.component;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		JLabel title = new JLabel("Settings");
		title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.ipady = 12;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(title, gbc);

		JLabel hal = new JLabel("Hardware acceleration");
		hal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.anchor = GridBagConstraints.WEST;
		gbc1.insets = new Insets(0, 15, 0, 0);
		gbc1.gridx = 0;
		gbc1.gridy = 1;
		add(hal, gbc1);

		JCheckBox halEnabled = new JCheckBox();
		halEnabled.setSelected(Main.settings.HAL);
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(5, 1, 5, 5);
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.gridx = 1;
		gbc2.gridy = 1;
		add(halEnabled, gbc2);

		JLabel osr = new JLabel("Off-screen rendering");
		osr.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.anchor = GridBagConstraints.WEST;
		gbc3.insets = new Insets(0, 15, 0, 0);
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		add(osr, gbc3);

		JCheckBox osrEnabled = new JCheckBox();
		osrEnabled.setSelected(Main.settings.OSR);
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.insets = new Insets(5, 1, 5, 5);
		gbc4.anchor = GridBagConstraints.NORTHWEST;
		gbc4.gridx = 1;
		gbc4.gridy = 2;
		add(osrEnabled, gbc4);

		JLabel searchEngine = new JLabel("Preferred search engine");
		searchEngine.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.ipadx = 18;
		gbc5.anchor = GridBagConstraints.WEST;
		gbc5.insets = new Insets(0, 15, 0, 0);
		gbc5.gridx = 0;
		gbc5.gridy = 3;
		add(searchEngine, gbc5);

		JComboBox<String> searchEngineList = new JComboBox<>();
		searchEngineList.setModel(new DefaultComboBoxModel<>(engines.keySet().toArray(new String[] {})));
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		GridBagConstraints gbc6 = new GridBagConstraints();
		gbc6.insets = new Insets(5, 5, 5, 5);
		gbc6.anchor = GridBagConstraints.NORTH;
		gbc6.fill = GridBagConstraints.HORIZONTAL;
		gbc6.weightx = 10.0;
		gbc6.gridx = 1;
		gbc6.gridy = 3;
		add(searchEngineList, gbc6);

		JLabel theme = new JLabel("Theme");
		theme.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.anchor = GridBagConstraints.WEST;
		gbc7.insets = new Insets(0, 15, 0, 0);
		gbc7.gridx = 0;
		gbc7.gridy = 4;
		add(theme, gbc7);

		JComboBox<Theme> themeList = new JComboBox<>();
		themeList.setModel(new DefaultComboBoxModel<>(Theme.values()));

		GridBagConstraints gbc8 = new GridBagConstraints();
		gbc8.insets = new Insets(5, 5, 5, 5);
		gbc8.anchor = GridBagConstraints.NORTH;
		gbc8.fill = GridBagConstraints.HORIZONTAL;
		gbc8.weightx = 10.0;
		gbc8.gridx = 1;
		gbc8.gridy = 4;
		add(themeList, gbc8);

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
				Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
				Main.settings.theme = (Theme) themeList.getSelectedItem();
				JOptionPane.showMessageDialog(this, "App will shut down to apply changes. Relaunch it.");
				Main.refreshSettings();
				browserWindow.dispatchEvent(new WindowEvent(browserWindow, WindowEvent.WINDOW_CLOSING));
			} else {
				Main.settings.HAL = halEnabled.isSelected();
				Main.settings.OSR = osrEnabled.isSelected();
				Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
				Main.settings.theme = (Theme) themeList.getSelectedItem();
				JOptionPane.showMessageDialog(this, "App will be restarted to apply settings.");
				Main.refreshSettings();
				browserWindow.setVisible(false);
				browserWindow.dispose();
				Main.restart(browserWindow.cefApp);
			}
		});

		confirmationButtons.add(ok);
		confirmationButtons.add(cancel);

		GridBagConstraints gbc9 = new GridBagConstraints();
		gbc9.anchor = GridBagConstraints.SOUTHEAST;
		gbc9.gridx = 1;
		gbc9.gridy = 6;
		add(confirmationButtons, gbc9);

		halEnabled.setSelected(Main.settings.HAL);
		osrEnabled.setSelected(Main.settings.OSR);
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		themeList.setSelectedItem(Main.settings.theme);
	}
}
