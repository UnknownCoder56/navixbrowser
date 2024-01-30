package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

		// Title
		BetterJLabel title = new BetterJLabel("Settings");
		title.setFont(title.getFont().deriveFont(20F));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 10, 5, 5);
		gbc.ipady = 12;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(title, gbc);


		// HAL
		BetterJLabel hal = new BetterJLabel("Hardware acceleration");
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


		// OSR
		BetterJLabel osr = new BetterJLabel("Off-screen rendering");
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


		// Search engine
		BetterJLabel searchEngine = new BetterJLabel("Preferred search engine");
		GridBagConstraints gbc5 = new GridBagConstraints();
		gbc5.ipadx = 18;
		gbc5.anchor = GridBagConstraints.WEST;
		gbc5.insets = new Insets(0, 15, 5, 5);
		gbc5.gridx = 0;
		gbc5.gridy = 3;
		panel.add(searchEngine, gbc5);

		BetterJComboBox<String> searchEngineList = new BetterJComboBox<>();
		searchEngineList.setModel(new DefaultComboBoxModel<>(engines.keySet().toArray(String[]::new)));
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
		gbc6.insets = new Insets(5, 5, 5, 5);
		gbc6.anchor = GridBagConstraints.NORTH;
		gbc6.fill = GridBagConstraints.HORIZONTAL;
		gbc6.weightx = 10.0;
		gbc6.gridwidth = GridBagConstraints.REMAINDER;
		gbc6.gridx = 1;
		gbc6.gridy = 3;
		panel.add(searchEngineList, gbc6);


		// Theme
		BetterJLabel theme = new BetterJLabel("Theme");
		GridBagConstraints gbc7 = new GridBagConstraints();
		gbc7.anchor = GridBagConstraints.WEST;
		gbc7.insets = new Insets(0, 15, 5, 5);
		gbc7.gridx = 0;
		gbc7.gridy = 4;
		panel.add(theme, gbc7);

		BetterJButton themeColor = new BetterJButton("Select background color for modern theme");
		BetterJComboBox<Theme> themeList = new BetterJComboBox<>();
		themeList.setModel(new DefaultComboBoxModel<>(Theme.values()));
		themeList.setSelectedItem(Main.settings.theme);
		themeList.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Theme selectedTheme = (Theme) themeList.getSelectedItem();
				if (selectedTheme != Main.settings.theme) {
					Main.settings.theme = selectedTheme;
					Main.refreshSettings();
					themeColor.setEnabled(Main.settings.theme == Theme.Modern);
					try {
						switch (Main.settings.theme) {
							case Modern:
								UIManager.setLookAndFeel(Main.getModernLookAndFeelForBackground());
								break;
							case System:
								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
								break;
							case CrossPlatform:
								UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
								break;	
						}
					} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
							 IllegalAccessException e1) {
						Main.logger.log(Level.SEVERE, "Failed to set theme: {0}", e1);
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

		themeColor.setEnabled(Main.settings.theme == Theme.Modern);
		themeColor.addActionListener(l -> SwingUtilities.invokeLater(() -> {
			Color color = JColorChooser.showDialog(this, "Select background color", (Color) UIManager.get("control"));
			if (color != null) {
				String hex = "#" + Integer.toHexString(color.getRGB()).substring(2);
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("./themes/FlatLaf.properties", false))) {
					writer.write("@bg = " + hex + "\n\n" +
							"*.foreground = " + (Main.getTextColorForBackground(color) == Color.WHITE ? "#FFF" : "#000") + "\n" +
							"*.selectionForeground = " + (Main.getTextColorForBackground(color) == Color.WHITE ? "#FFF" : "#000") + "\n" +
							"*.caretForeground = " + (Main.getTextColorForBackground(color) == Color.WHITE ? "#FFF" : "#000") + "\n" +
							"*.background = @bg\n" +
							"@background = @bg\n" +
							"desktop = @bg\n" +
							"window = @bg\n" +
							"menu = @bg\n" +
							"text = @bg\n" +
							"control = @bg");
				} catch (IOException e) {
					Main.logger.log(Level.SEVERE, "Failed to write theme file: {0}", e);
				}
				try {
					UIManager.setLookAndFeel(Main.getModernLookAndFeelForBackground(color));
				} catch (UnsupportedLookAndFeelException e) {
					Main.logger.log(Level.SEVERE, "Failed to set theme: {0}", e);
				}
				SwingUtilities.updateComponentTreeUI(browserWindow);
				browserWindow.tabbedPane.applyThemeChange();
			}
		}));
		GridBagConstraints gbc8x = new GridBagConstraints();
		gbc8x.insets = new Insets(0, 5, 0, 5);
		gbc8x.anchor = GridBagConstraints.EAST;
		gbc8x.gridx = 2;
		gbc8x.gridy = 4;
		panel.add(themeColor, gbc8x);


		// Launch maximized
		BetterJLabel launchMaximized = new BetterJLabel("Launch maximized");
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


		// Ad block
		BetterJLabel adBlock = new BetterJLabel("Enable ad block");
		GridBagConstraints gbc11 = new GridBagConstraints();
		gbc11.anchor = GridBagConstraints.WEST;
		gbc11.insets = new Insets(0, 15, 5, 5);
		gbc11.gridx = 0;
		gbc11.gridy = 6;
		panel.add(adBlock, gbc11);

		JCheckBox adBlockEnabled = new JCheckBox();
		adBlockEnabled.setSelected(Main.settings.enableAdBlock);
		adBlockEnabled.addChangeListener(l -> {
			if (adBlockEnabled.isSelected() != Main.settings.enableAdBlock) {
				Main.settings.enableAdBlock = adBlockEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc12 = new GridBagConstraints();
		gbc12.insets = new Insets(5, 1, 5, 0);
		gbc12.anchor = GridBagConstraints.NORTHWEST;
		gbc12.gridx = 1;
		gbc12.gridy = 6;
		panel.add(adBlockEnabled, gbc12);


		// Tracker block
		BetterJLabel trackerBlock = new BetterJLabel("Enable tracker block");
		GridBagConstraints gbc13 = new GridBagConstraints();
		gbc13.anchor = GridBagConstraints.WEST;
		gbc13.insets = new Insets(0, 15, 5, 5);
		gbc13.gridx = 0;
		gbc13.gridy = 7;
		panel.add(trackerBlock, gbc13);

		JCheckBox trackerBlockEnabled = new JCheckBox();
		trackerBlockEnabled.setSelected(Main.settings.enableTrackerBlock);
		trackerBlockEnabled.addChangeListener(l -> {
			if (trackerBlockEnabled.isSelected() != Main.settings.enableTrackerBlock) {
				Main.settings.enableTrackerBlock = trackerBlockEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc14 = new GridBagConstraints();
		gbc14.insets = new Insets(5, 1, 5, 0);
		gbc14.anchor = GridBagConstraints.NORTHWEST;
		gbc14.gridx = 1;
		gbc14.gridy = 7;
		panel.add(trackerBlockEnabled, gbc14);


		// Safe browsing
		BetterJLabel safeBrowsing = new BetterJLabel("Enable safe browsing");
		GridBagConstraints gbc15 = new GridBagConstraints();
		gbc15.anchor = GridBagConstraints.WEST;
		gbc15.insets = new Insets(0, 15, 5, 5);
		gbc15.gridx = 0;
		gbc15.gridy = 8;
		panel.add(safeBrowsing, gbc15);

		JCheckBox safeBrowsingEnabled = new JCheckBox();
		safeBrowsingEnabled.setSelected(Main.settings.enableSafeBrowsing);
		safeBrowsingEnabled.addChangeListener(l -> {
			if (safeBrowsingEnabled.isSelected() != Main.settings.enableSafeBrowsing) {
				Main.settings.enableSafeBrowsing = safeBrowsingEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc16 = new GridBagConstraints();
		gbc16.insets = new Insets(5, 1, 5, 0);
		gbc16.anchor = GridBagConstraints.NORTHWEST;
		gbc16.gridx = 1;
		gbc16.gridy = 8;
		panel.add(safeBrowsingEnabled, gbc16);


		// Command-line arguments
		BetterJLabel cmdLineArgs = new BetterJLabel("Command-line arguments (JCEF)");
		GridBagConstraints gbc17 = new GridBagConstraints();
		gbc17.anchor = GridBagConstraints.WEST;
		gbc17.insets = new Insets(5, 15, 0, 5);
		gbc17.gridx = 0;
		gbc17.gridy = 9;
		panel.add(cmdLineArgs, gbc17);

		BetterJTextField argsField = new BetterJTextField();
		if (Main.settings.args.length > 0) {
			String argsString = Arrays.toString(Main.settings.args);
			argsField.setText(argsString.substring(1, argsString.length() - 1));
		}
		argsField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!browserWindow.browserIsInFocus)
					return;
				browserWindow.browserIsInFocus = false;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				argsField.requestFocusInWindow();
			}
		});
		GridBagConstraints gbc18 = new GridBagConstraints();
		gbc18.insets = new Insets(5, 5, 0, 0);
		gbc18.anchor = GridBagConstraints.NORTH;
		gbc18.fill = GridBagConstraints.HORIZONTAL;
		gbc18.gridx = 1;
		gbc18.gridy = 9;
		panel.add(argsField, gbc18);

		BetterJButton setArgs = new BetterJButton("Save");
		setArgs.addActionListener(l -> {
			if (argsField.getText().isEmpty()) {
				Main.settings.args = new String[]{};
			} else if (!argsField.getText().contains(",")) {
				Main.settings.args = new String[]{argsField.getText()};
			} else {
				java.util.List<String> args = new java.util.ArrayList<>(List.of(argsField.getText().split(",")));
				args.replaceAll(String::strip);
				Main.settings.args = args.toArray(String[]::new);
			}
			Main.refreshSettings();
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Saved arguments!"));
		});
		GridBagConstraints gbc19 = new GridBagConstraints();
		gbc19.insets = new Insets(5, 5, 0, 5);
		gbc19.anchor = GridBagConstraints.EAST;
		gbc19.fill = GridBagConstraints.HORIZONTAL;
		gbc19.gridx = 2;
		gbc19.gridy = 9;
		panel.add(setArgs, gbc19);

		BetterJLabel argsInfo = new BetterJLabel("Will be applied on next launch. Example: arg1, arg2, ...");
		GridBagConstraints gbc20 = new GridBagConstraints();
		gbc20.insets = new Insets(2, 5, 5, 0);
		gbc20.anchor = GridBagConstraints.WEST;
		gbc20.gridx = 1;
		gbc20.gridy = 10;
		panel.add(argsInfo, gbc20);


		// Safe browsing
		BetterJLabel forceDarkMode = new BetterJLabel("Force dark mode in sites");
		GridBagConstraints gbc21 = new GridBagConstraints();
		gbc21.anchor = GridBagConstraints.WEST;
		gbc21.insets = new Insets(0, 15, 5, 5);
		gbc21.gridx = 0;
		gbc21.gridy = 11;
		panel.add(forceDarkMode, gbc21);

		JCheckBox forceDarkModeEnabled = new JCheckBox();
		forceDarkModeEnabled.setSelected(Main.settings.forceDarkMode);
		forceDarkModeEnabled.addChangeListener(l -> {
			if (forceDarkModeEnabled.isSelected() != Main.settings.forceDarkMode) {
				Main.settings.forceDarkMode = forceDarkModeEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc22 = new GridBagConstraints();
		gbc22.insets = new Insets(5, 1, 5, 0);
		gbc22.anchor = GridBagConstraints.NORTHWEST;
		gbc22.gridx = 1;
		gbc22.gridy = 11;
		panel.add(forceDarkModeEnabled, gbc22);


		// Search suggestions
		BetterJLabel searchSuggestions = new BetterJLabel("Enable search suggestions");
		GridBagConstraints gbc23 = new GridBagConstraints();
		gbc23.anchor = GridBagConstraints.WEST;
		gbc23.insets = new Insets(0, 15, 5, 5);
		gbc23.gridx = 0;
		gbc23.gridy = 12;
		panel.add(searchSuggestions, gbc23);

		JCheckBox searchSuggestionsEnabled = new JCheckBox();
		searchSuggestionsEnabled.setSelected(Main.settings.enableSearchSuggestions);
		searchSuggestionsEnabled.addChangeListener(l -> {
			if (searchSuggestionsEnabled.isSelected() != Main.settings.enableSearchSuggestions) {
				Main.settings.enableSearchSuggestions = searchSuggestionsEnabled.isSelected();
				Main.refreshSettings();
			}
		});
		GridBagConstraints gbc24 = new GridBagConstraints();
		gbc24.insets = new Insets(5, 1, 5, 0);
		gbc24.anchor = GridBagConstraints.NORTHWEST;
		gbc24.gridx = 1;
		gbc24.gridy = 12;
		panel.add(searchSuggestionsEnabled, gbc24);

		
		// New tab URL
		BetterJLabel newTabURL = new BetterJLabel("New tab URL");
        GridBagConstraints gbc25 = new GridBagConstraints();
        gbc25.anchor = GridBagConstraints.WEST;
        gbc25.insets = new Insets(5, 15, 0, 5);
        gbc25.gridx = 0;
        gbc25.gridy = 13;
        panel.add(newTabURL, gbc25);

		BetterJTextField newTabURLField = new BetterJTextField();
        newTabURLField.setText(Main.settings.newTabURL);
        newTabURLField.addFocusListener(new FocusAdapter() {
        	@Override
			public void focusGained(FocusEvent e) {
				if (!browserWindow.browserIsInFocus)
					return;
				browserWindow.browserIsInFocus = false;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				argsField.requestFocusInWindow();
			}
        });
        GridBagConstraints gbc26 = new GridBagConstraints();
        gbc26.insets = new Insets(5, 5, 0, 0);
        gbc26.anchor = GridBagConstraints.NORTH;
        gbc26.fill = GridBagConstraints.HORIZONTAL;
        gbc26.gridx = 1;
        gbc26.gridy = 13;
        panel.add(newTabURLField, gbc26);

		BetterJButton setNewTabURL = new BetterJButton("Save");
        setNewTabURL.addActionListener(l -> {
            Main.settings.newTabURL = newTabURLField.getText();
            Main.refreshSettings();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Saved URL!"));
        });
        GridBagConstraints gbc27 = new GridBagConstraints();
        gbc27.insets = new Insets(5, 5, 0, 5);
        gbc27.anchor = GridBagConstraints.EAST;
        gbc27.fill = GridBagConstraints.HORIZONTAL;
        gbc27.gridx = 2;
        gbc27.gridy = 13;
        panel.add(setNewTabURL, gbc27);
        
        
        // Debug port
		BetterJLabel debugPort = new BetterJLabel("Debug port");
        GridBagConstraints gbc28 = new GridBagConstraints();
        gbc28.anchor = GridBagConstraints.WEST;
        gbc28.insets = new Insets(5, 15, 0, 5);
        gbc28.gridx = 0;
        gbc28.gridy = 14;
        panel.add(debugPort, gbc28);

		BetterJTextField debugPortField = new BetterJTextField();
        debugPortField.setText(Integer.toString(Main.settings.debugPort));
        debugPortField.addFocusListener(new FocusAdapter() {
        	@Override
			public void focusGained(FocusEvent e) {
				if (!browserWindow.browserIsInFocus)
					return;
				browserWindow.browserIsInFocus = false;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				argsField.requestFocusInWindow();
			}
        });
        GridBagConstraints gbc29 = new GridBagConstraints();
        gbc29.insets = new Insets(5, 5, 0, 0);
        gbc29.anchor = GridBagConstraints.NORTH;
        gbc29.fill = GridBagConstraints.HORIZONTAL;
        gbc29.gridx = 1;
        gbc29.gridy = 14;
        panel.add(debugPortField, gbc29);

		BetterJButton setDebugPort = new BetterJButton("Save");
        setDebugPort.addActionListener(l -> {
        	try {
        		int port = Integer.parseInt(debugPortField.getText());
        		if (port >= 0 && port <= 65535) {
        			Main.settings.debugPort = port;
            		Main.refreshSettings();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Saved port! Will be used on next launch."));
        		} else {
        			debugPortField.setText(Integer.toString(Main.settings.debugPort));
        			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Not a valid port number! Must be between 0 and 65535."));
        		}
        	} catch (NumberFormatException e) {
        		debugPortField.setText(Integer.toString(Main.settings.debugPort));
        		SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(browserWindow, "Not a valid port number!"));
        	}
        });
        GridBagConstraints gbc30 = new GridBagConstraints();
        gbc30.insets = new Insets(5, 5, 0, 5);
        gbc30.anchor = GridBagConstraints.EAST;
        gbc30.fill = GridBagConstraints.HORIZONTAL;
        gbc30.gridx = 2;
        gbc30.gridy = 14;
        panel.add(setDebugPort, gbc30);


		BetterJLabel bottomInfo = new BetterJLabel("Version " + Main.VERSION + " (Chromium "
				+ browserWindow.cefApp.getVersion().getChromeVersion() + ")."
				+ " Changes to graphics settings will be applied on next launch.");

		supreme.add(new JScrollPane(parent), BorderLayout.CENTER);
		supreme.add(bottomInfo, BorderLayout.SOUTH);
		add(supreme);
	}
}
