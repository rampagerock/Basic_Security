import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class GUI extends JFrame {
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JLabel modeLbl, infoLbl;
	private JRadioButtonMenuItem rbMenu;
	private JPanel contentPanel, labelPanel, btnPanel, centerPanel;
	private JTextArea text;
	private boolean encrypt = true;
	private JButton submitBtn;

	public GUI() {
		createMenu();
		createContent();
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void createContent() {
		contentPanel = new JPanel(new BorderLayout());

		// labelPanel
		infoLbl = new JLabel("Mode :");
		modeLbl = new JLabel("Select mode under \"Action\"");
		labelPanel = new JPanel(new GridLayout(1, 3));
		labelPanel.add(infoLbl);
		labelPanel.add(modeLbl);

		centerPanel = new JPanel();
		text = new JTextArea(" ", 30, 30);
		centerPanel.add(text);

		btnPanel = new JPanel();
		submitBtn = new JButton("submit");
		btnPanel.add(submitBtn);

		contentPanel.add(centerPanel, BorderLayout.CENTER);
		contentPanel.add(btnPanel, BorderLayout.SOUTH);
		contentPanel.add(labelPanel, BorderLayout.NORTH);
		this.add(contentPanel);

	}

	public void createMenu() {
		// Initial Styling
		Font f = new Font("Verdana", Font.PLAIN, 14);
		UIManager.put("Menu.font", f);
		UIManager.put("MenuItem.font", f);
		UIManager.put("RadioButtonMenuItem.font", f);
		UIManager.put("RadioButtonMenuItem.foreground",
				new Color(211, 211, 211));
		UIManager.put("RadioButtonMenuItem.background", new Color(0, 0, 0));
		UIManager.put("Menu.foreground", new Color(211, 211, 211));
		UIManager.put("Menu.opaque", true);
		UIManager.put("Menu.background", new Color(0, 0, 0));
		UIManager.put("MenuBar.opaque", true);
		UIManager.put("MenuBar.background", new Color(0, 0, 0));

		menuBar = new JMenuBar();

		// File menu
		menu = new JMenu("File");
		menuItem = new JMenuItem("Open file");
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					try {
						 BufferedReader in = new BufferedReader(new FileReader(selectedFile));
						 String line = null;
						while ((line = in.readLine()) != null) {
						text.append(line + "\n");
					}
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					

					

				}
			}
		});
		menuItem = new JMenuItem("Close");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		menu.add(menuItem);

		menuBar.add(menu);
		// first Menu
		menu = new JMenu("Action");
		ButtonGroup group = new ButtonGroup();
		rbMenu = new JRadioButtonMenuItem("Encrypt");
		group.add(rbMenu);
		menu.add(rbMenu);
		rbMenu.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				AbstractButton obj = (AbstractButton) e.getSource();
				int state = e.getStateChange();
				String str = obj.getText();
				if (state == e.SELECTED) {
					if (str.equals("Encrypt"))
						modeLbl.setText(str);
					obj.setBackground(new Color(131, 139, 139));
					System.out.println(str);
				} else {
					obj.setBackground(new Color(0, 0, 0));
				}

			}
		});
		rbMenu = new JRadioButtonMenuItem("Decrypt");
		group.add(rbMenu);
		menu.add(rbMenu);
		rbMenu.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				AbstractButton obj = (AbstractButton) e.getSource();
				int state = e.getStateChange();
				String str = obj.getText();
				if (state == e.SELECTED) {
					if (str.equals("Decrypt"))
						modeLbl.setText(str);
					obj.setBackground(new Color(131, 139, 139));
					System.out.println(str);
				} else {
					obj.setBackground(new Color(0, 0, 0));
				}

			}
		});
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GUI();
	}

}
