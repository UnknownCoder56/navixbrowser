package com.uniqueapps.navixbrowser.component;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.uniqueapps.navixbrowser.Main;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 4379679088941442158L;

	public SettingsPanel() {
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
		gridBagLayout.columnWidths = new int[]{236, 21, 92, 127, 0};
		gridBagLayout.rowHeights = new int[]{35, 16, 16, 22, 258, 33, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
						JLabel title = new JLabel("Settings");
						title.setBorder(new EmptyBorder(5, 5, 5, 5));
						title.setFont(title.getFont().deriveFont(20.0F));
						GridBagConstraints gbc_title = new GridBagConstraints();
						gbc_title.anchor = GridBagConstraints.NORTHWEST;
						gbc_title.insets = new Insets(0, 0, 5, 5);
						gbc_title.gridx = 0;
						gbc_title.gridy = 0;
						add(title, gbc_title);
						JLabel hal = new JLabel("Hardware acceleration");
						hal.setFont(new Font("Tahoma", Font.PLAIN, 13));
						GridBagConstraints gbc_hal = new GridBagConstraints();
						gbc_hal.fill = GridBagConstraints.HORIZONTAL;
						gbc_hal.anchor = GridBagConstraints.NORTH;
						gbc_hal.insets = new Insets(0, 10, 5, 5);
						gbc_hal.gridx = 0;
						gbc_hal.gridy = 1;
						add(hal, gbc_hal);
						JCheckBox halEnabled = new JCheckBox();
						
								halEnabled.setSelected(Main.settings.HAL);
								GridBagConstraints gbc_halEnabled = new GridBagConstraints();
								gbc_halEnabled.anchor = GridBagConstraints.WEST;
								gbc_halEnabled.fill = GridBagConstraints.VERTICAL;
								gbc_halEnabled.insets = new Insets(0, 0, 5, 5);
								gbc_halEnabled.gridx = 1;
								gbc_halEnabled.gridy = 1;
								add(halEnabled, gbc_halEnabled);
				
						JLabel searchEngine = new JLabel("Preferred search engine");
						searchEngine.setFont(new Font("Tahoma", Font.PLAIN, 13));
						GridBagConstraints gbc_searchEngine = new GridBagConstraints();
						gbc_searchEngine.anchor = GridBagConstraints.NORTH;
						gbc_searchEngine.fill = GridBagConstraints.HORIZONTAL;
						gbc_searchEngine.insets = new Insets(0, 10, 5, 5);
						gbc_searchEngine.gridx = 0;
						gbc_searchEngine.gridy = 2;
						add(searchEngine, gbc_searchEngine);
				JCheckBox osrEnabled = new JCheckBox();
				osrEnabled.setSelected(Main.settings.OSR);
				GridBagConstraints gbc_osrEnabled = new GridBagConstraints();
				gbc_osrEnabled.anchor = GridBagConstraints.WEST;
				gbc_osrEnabled.fill = GridBagConstraints.VERTICAL;
				gbc_osrEnabled.insets = new Insets(0, 0, 5, 5);
				gbc_osrEnabled.gridx = 1;
				gbc_osrEnabled.gridy = 2;
				add(osrEnabled, gbc_osrEnabled);
		
				JLabel osr = new JLabel("Off-screen rendering");
				osr.setFont(new Font("Tahoma", Font.PLAIN, 13));
				GridBagConstraints gbc_osr = new GridBagConstraints();
				gbc_osr.fill = GridBagConstraints.HORIZONTAL;
				gbc_osr.insets = new Insets(0, 10, 5, 5);
				gbc_osr.gridx = 0;
				gbc_osr.gridy = 3;
				add(osr, gbc_osr);
		JComboBox<String> searchEngineList = new JComboBox<>(engines.keySet().toArray(new String[] {}));
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		GridBagConstraints gbc_searchEngineList = new GridBagConstraints();
		gbc_searchEngineList.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchEngineList.anchor = GridBagConstraints.NORTH;
		gbc_searchEngineList.insets = new Insets(0, 0, 5, 0);
		gbc_searchEngineList.gridwidth = 3;
		gbc_searchEngineList.gridx = 1;
		gbc_searchEngineList.gridy = 3;
		add(searchEngineList, gbc_searchEngineList);
		
		JPanel confirmationButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(l -> {
			halEnabled.setSelected(Main.settings.HAL);
			osrEnabled.setSelected(Main.settings.OSR);
			searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		});
		
				JButton ok = new JButton("OK");
				ok.addActionListener(l -> {
					Main.settings.HAL = halEnabled.isSelected();
					Main.settings.OSR = osrEnabled.isSelected();
					Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
					JOptionPane.showMessageDialog(this, "Any changes to graphics settings will be applied on next launch.");
					Main.refreshSettings();
				});
				
				confirmationButtons.add(ok);
				confirmationButtons.add(cancel);
				GridBagConstraints gbc_confirmationButtons = new GridBagConstraints();
				gbc_confirmationButtons.anchor = GridBagConstraints.NORTHWEST;
				gbc_confirmationButtons.gridx = 3;
				gbc_confirmationButtons.gridy = 5;
				add(confirmationButtons, gbc_confirmationButtons);
	}
}
