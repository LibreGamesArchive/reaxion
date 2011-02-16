package com.googlecode.reaxion.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import com.googlecode.reaxion.tools.components.DialogueTable;
import com.googlecode.reaxion.tools.components.ToolMenuItem;
import com.googlecode.reaxion.tools.filters.DialogueEditorFilter;

public class DialogueEditor extends JFrame implements ActionListener {

	private static final Dimension SIZE = new Dimension(800, 600);
	private static final File dir = new File("src/com/googlecode/reaxion/resources/cutscene/scenes/");
	
	private DialogueTable table;
	private JTabbedPane tabbedPane;
	private JFileChooser fileChooser;
	
	public static void main(String[] args) {
		DialogueEditor d = new DialogueEditor();
	}
	
	public DialogueEditor() {
		super("Dialogue Creator - Brian Clanton");
		
		init();
	}
	
	private void init() {
		initFileChooser();
		initTabbedPane();
		initMenu();
		initFrame();
	}
	
	private void initFileChooser() {		
		fileChooser = new JFileChooser(dir);
		fileChooser.setFileFilter(new DialogueEditorFilter());		
	}
	
	private void initTabbedPane() {
		tabbedPane = new JTabbedPane();
		
		initTablePane();
		
		add(tabbedPane);
	}
	
	private void initTablePane() {
		initTable();
		
		JScrollPane t = new JScrollPane(table);
		t.setPreferredSize(SIZE);
		
		tabbedPane.addTab("Dialogue", t);
	}
	
	private void initTable() {
		table = new DialogueTable();
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		
		table.getColumnModel().getColumn(0).setMaxWidth(100);
	}
	
	private void initMenu() {
		JMenu file = new JMenu("File");
		file.add(new ToolMenuItem("New Cutscene", this));
		file.add(new ToolMenuItem("Open...", this));
		file.add(new ToolMenuItem("Save...", this));
		file.add(new JSeparator());
		file.add(new ToolMenuItem("Exit", this));
		
		JMenu edit = new JMenu("Edit");
		edit.add(new ToolMenuItem("Add Dialogue Row", this));
		
		JMenuBar bar = new JMenuBar();
		bar.add(file);
		bar.add(edit);
		
		setJMenuBar(bar);
	}
	
	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		centerOnScreen();
		setVisible(true);
	}
	
	private void centerOnScreen() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - SIZE.width) / 2, (screen.height - SIZE.height) / 2);
	}
	
	private void saveScene(File f) {
		
	}

	private void openScene(File f) {
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("Save...")) {
			int returnValue = fileChooser.showSaveDialog(this);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				saveScene(fileChooser.getSelectedFile());
			}
		} else if (command.equals("Open...")) {
			int returnValue = fileChooser.showOpenDialog(this);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				openScene(fileChooser.getSelectedFile());
			}
		} else if (command.equals("Exit")) {
			System.exit(0);
		} else if (command.equals("Add Dialogue Row")) {
			table.addRow();
		}
	}
		
}
