package com.uniqueapps.navixbrowser.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.uniqueapps.navixbrowser.Main;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 4379679088941442158L;

	public SettingsDialog(JFrame owner) {
		super(owner, "Navix Settings", ModalityType.APPLICATION_MODAL);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Main.refreshSettings();
			}
		});

		setSize(500, 400);
		setResizable(false);
		setLocationRelativeTo(owner);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel title = new JLabel("Settings");
		title.setBorder(new EmptyBorder(5, 5, 5, 5));
		title.setFont(title.getFont().deriveFont(20.0F));

		JPanel settingsGrid = new JPanel();

		JLabel hal = new JLabel("Hardware acceleration");
		hal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		JCheckBox halEnabled = new JCheckBox();

		JLabel osr = new JLabel("Off-screen rendering");
		osr.setFont(new Font("Tahoma", Font.PLAIN, 13));
		JCheckBox osrEnabled = new JCheckBox();

		JLabel searchEngine = new JLabel("Preferred search engine");
		searchEngine.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Map<String, String> engines = new HashMap<>();
		engines.put("Google", "https://google.com/search?q=");
		engines.put("Bing", "https://bing.com/search?q=");
		engines.put("Yahoo", "https://search.yahoo.com/search?p=");
		engines.put("Yandex", "https://yandex.com/search/?text=");
		engines.put("Ecosia", "https://www.ecosia.org/search?q=");
		engines.put("SLSearch", "https://slsearch.cf/search?q=");
		engines.put("DuckDuckGo", "https://duckduckgo.com/?q=");
		engines.put("Brave", "https://search.brave.com/search?q=");
		JComboBox<String> searchEngineList = new JComboBox<>(engines.keySet().toArray(new String[] {}));
		
		JPanel confirmationButtons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(l -> dispose());

		JButton ok = new JButton("OK");
		ok.addActionListener(l -> {
			Main.settings.HAL = halEnabled.isSelected();
			Main.settings.OSR = osrEnabled.isSelected();
			Main.settings.searchEngine = engines.get(searchEngineList.getSelectedItem());
			JOptionPane.showMessageDialog(this, "Any changes to graphics settings will be applied on next launch.");
			Main.refreshSettings();
			dispose();
		});
		
		confirmationButtons.add(ok);
		confirmationButtons.add(cancel);
		
		getContentPane().add(title, BorderLayout.NORTH);
		getContentPane().add(settingsGrid, BorderLayout.CENTER);
		getContentPane().add(confirmationButtons, BorderLayout.SOUTH);

		halEnabled.setSelected(Main.settings.HAL);
		osrEnabled.setSelected(Main.settings.OSR);
		searchEngineList.setSelectedIndex(engines.values().stream().toList().indexOf(Main.settings.searchEngine));
		
		GroupLayout gl_settingsGrid = new GroupLayout(settingsGrid);
		gl_settingsGrid.setHorizontalGroup(gl_settingsGrid.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_settingsGrid.createSequentialGroup().addContainerGap()
						.addGroup(gl_settingsGrid.createParallelGroup(Alignment.LEADING)
								.addComponent(searchEngine, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
								.addComponent(osr, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
								.addComponent(hal, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_settingsGrid.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_settingsGrid.createSequentialGroup().addGap(4).addComponent(
										searchEngineList, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
								.addComponent(osrEnabled).addComponent(halEnabled))
						.addContainerGap()));
		gl_settingsGrid.setVerticalGroup(gl_settingsGrid.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsGrid.createSequentialGroup().addGap(31)
						.addGroup(gl_settingsGrid.createParallelGroup(Alignment.TRAILING)
								.addComponent(halEnabled, GroupLayout.PREFERRED_SIZE, 14, Short.MAX_VALUE)
								.addComponent(hal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(26)
						.addGroup(gl_settingsGrid.createParallelGroup(Alignment.BASELINE).addComponent(searchEngine)
								.addComponent(searchEngineList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(25)
						.addGroup(gl_settingsGrid.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(osrEnabled, 0, 0, Short.MAX_VALUE)
								.addComponent(osr, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(170)));
		settingsGrid.setLayout(gl_settingsGrid);
	}
}
