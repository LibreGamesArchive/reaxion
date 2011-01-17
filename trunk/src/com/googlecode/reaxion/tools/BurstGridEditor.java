package com.googlecode.reaxion.tools;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.googlecode.reaxion.tools.components.AttributePanel;
import com.googlecode.reaxion.tools.components.BurstGridPanel;
import com.googlecode.reaxion.tools.components.NodeIDField;
import com.googlecode.reaxion.tools.components.ToolButton;
import com.googlecode.reaxion.tools.components.ToolMenuItem;
import com.googlecode.reaxion.tools.events.NodeEvent;
import com.googlecode.reaxion.tools.events.ValidationEvent;
import com.googlecode.reaxion.tools.filters.TextFilter;
import com.googlecode.reaxion.tools.listeners.NodeEventListener;
import com.googlecode.reaxion.tools.listeners.ValidationEventListener;
import com.googlecode.reaxion.tools.util.ToolUtils;
import com.googlecode.reaxion.tools.vo.EditorNode;

/**
 * This application is used for the creation of burst grids for use in Reaxion. It allows users to place nodes on a grid, 
 * edit their attributes, create connections between nodes, and export the grid to a text file.
 * 
 * @author Brian Clanton, Cy Neita
 *
 */
public class BurstGridEditor extends JFrame implements ActionListener, NodeEventListener, ValidationEventListener {

	public static final String gridDir = "src/com/googlecode/reaxion/resources/burstgrid/";
	public static final String nodeIconDir = "src/com/googlecode/reaxion/tools/icons/";
	public static final String[] nodeTypes = {"ability", "attack", "gauge1", "gauge2", "hp", "rate", "strength"};
	public static final String[] attributes = {"Ability Name", "Attack Name", "Min Gauge Plus", "Max Gauge Plus", 
		"HP Plus", "Rate Plus", "Strength Plus"};

	private static int nodeID = 1;

	private JFileChooser fileChooser;

	private BurstGridPanel bgp;

	private JPanel toolbar;
	private JPanel nodeAttributes;

	private JComboBox character;
	private JComboBox types;
	
	private ToolButton createNode;
	private ToolButton removeNode;
	
	private NodeIDField idField;

	private JLabel costsLabel;
	private JTextField costs;
	
	private EditorNode selectedNode;

	public static void main(String[] args) {
		BurstGridEditor bge = new BurstGridEditor();
	}

	public BurstGridEditor() {
		super("Burst Grid Editor - Brian Clanton");

		init();
	}

	/**
	 * Initializes entire application.
	 */
	private void init() {
		ToolUtils.initialize();
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		fileChooser = new JFileChooser(new File(gridDir));
		fileChooser.setFileFilter(new TextFilter());

		initMenu();
		initToolbar();

		// Init Burst Grid
		Dimension temp = new Dimension(480, 480);
		bgp = new BurstGridPanel(temp);
		bgp.addNodeEventListener(this);
		add(bgp);		

		initFrame();
	}

	/**
	 * Initializes menu.
	 */
	private void initMenu() {
		JMenu file = new JMenu("File");
		file.add(new ToolMenuItem("Save Grid", this));
		file.add(new JSeparator());
		file.add(new ToolMenuItem("Exit", this));

		JMenuBar bar = new JMenuBar();
		bar.add(file);

		setJMenuBar(bar);
	}

	/**
	 * Initializes toolbar.
	 */
	private void initToolbar() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panel.setPreferredSize(new Dimension(250, 480));
		toolbar = new JPanel();
		toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));

		character = new JComboBox(ToolUtils.getCharacterNames().toArray());

		String[] temp = new String[nodeTypes.length];

		for (int i = 0; i < temp.length; i++)
			temp[i] = Character.toUpperCase(nodeTypes[i].charAt(0)) + nodeTypes[i].substring(1);

		idField = new NodeIDField("1");
		idField.addValidationEventListener(this);
		JLabel typeDesc = new JLabel("Node Type:");
		types = new JComboBox(temp);
		types.addActionListener(this);
		types.setActionCommand("node_types");

		initAttributesPanel();

		createNode = new ToolButton("Create Node", this);
		createNode.setEnabled(false);
		
		removeNode = new ToolButton("Remove Node", this);
		removeNode.setVisible(false);
		
		costsLabel = new JLabel("Costs: ");
		costs = new JTextField();
		costs.setEnabled(false);

		addComponentToToolbar(character, true);
		addComponentToToolbar(new JSeparator(), true);
		addComponentToToolbar(idField, true);
		addComponentToToolbar(typeDesc, true);
		addComponentToToolbar(types, true);
		addComponentToToolbar(nodeAttributes, true);
		addComponentToToolbar(costsLabel, true);
		addComponentToToolbar(costs, true);
		addComponentToToolbar(createNode, true);
		addComponentToToolbar(removeNode, false);

		panel.add(toolbar);
		add(panel);
	}

	/**
	 * Initializes all {@code AttributePanel} objects and adds them to a {@code JPanel} with a {@code CardLayout}.
	 */
	private void initAttributesPanel() {
		nodeAttributes = new JPanel(new CardLayout());

		for (int i = 0; i < attributes.length; i++)
			nodeAttributes.add(new AttributePanel(attributes[i], i >= 2 ? true : false, this), attributes[i]);

		CardLayout c = (CardLayout) nodeAttributes.getLayout();
		c.show(nodeAttributes, attributes[0]);
	}

	/**
	 * Adds a {@code JComponent} to a toolbar.
	 * 
	 * @param c {@code JComponent} to be added
	 * @param hasSpacing Indicates whether a rigid spacing area should be added under the {@code JComponent}.
	 */
	private void addComponentToToolbar(JComponent c, boolean hasSpacing) {
		toolbar.add(c);
		c.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		if (hasSpacing)
			toolbar.add(Box.createRigidArea(new Dimension(230, 10)));
	}

	/**
	 * Initializes the {@code JFrame}.
	 */
	private void initFrame() {
		try {
			setIconImage(ImageIO.read(new File(nodeIconDir + "ability.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,100);
		pack();
		setVisible(true);
		requestFocus();
	}

	/**
	 * Saves grid to file.
	 * @param f Specified file to save to.
	 */
	private void saveGrid(File f) {
		try {
			if (!f.getName().contains(".txt"))
				f = new File(f.getPath() + ".txt");

			PrintWriter p = new PrintWriter(new FileWriter(f));
			Scanner reader = new Scanner(new File(gridDir + "info.txt"));

			while (reader.hasNextLine())
				p.println(reader.nextLine());

			reader.close();

			p.println((String) character.getSelectedItem() + "\n");

			for (EditorNode n : bgp.getNodes())
				p.println(n);

			p.close();

			JOptionPane.showMessageDialog(null, "File \"" + f.getName() + "\" saved.", "Saving Complete", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (e.getSource() instanceof JComboBox) {
			// Switch currently displayed AttributePanel
			CardLayout c = (CardLayout) nodeAttributes.getLayout();
			c.show(nodeAttributes, attributes[((JComboBox) e.getSource()).getSelectedIndex()]);
		} else if (command.equals("Create Node")) {
			// Creates a node to be added to the BurstGridPanel
			AttributePanel temp = (AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()];
			EditorNode node = new EditorNode(idField.getId(), (String)types.getSelectedItem(), temp.getData(), temp.getDepth());
			bgp.createSelectedNode(types.getSelectedIndex(), node);
		} else if (command.equals("Edit Node")) {
			// Incorporates changed fields in toolbar to the selectedNode and sends it to the BurstGridPanel to be added to the grid
			AttributePanel temp = (AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()];
			
			selectedNode.setId(idField.getId());
			selectedNode.setType((String) types.getSelectedItem());
			selectedNode.setData(temp.getData());
			selectedNode.setDepth(temp.getDepth());
			
			bgp.applyChanges(types.getSelectedIndex(), selectedNode);
			selectedNode = null;
		} else if (command.equals("Remove Node")) {
			// Removes the current selected node from the grid
			bgp.removeSelectedNode();
			selectedNode = null;
		} else if (command.equals("Save Grid")) {
			// Saves grid
			int returnValue = fileChooser.showSaveDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION)
				saveGrid(fileChooser.getSelectedFile());

		} else if (command.equals("Exit")) {
			System.exit(0);
		}
	}

	public void nodeCreated(NodeEvent e) {
		createNode.setEnabled(false);
	}

	public void nodeAdded(NodeEvent e) {
		idField.updateNodes(bgp.getNodes());
		idField.setText("" + (e.getNode().getId() + 1));

		for (Component c : nodeAttributes.getComponents())
			((AttributePanel) c).resetFields();
	}

	public void nodeRemoved(NodeEvent e) {
		idField.updateNodes(bgp.getNodes());
		idField.setText("" + Math.max((e.getNode().getId() - 1), 1));

		for (Component c : nodeAttributes.getComponents())
			((AttributePanel) c).resetFields();
		
		createNode.setText("Create Node");
		createNode.setEnabled(false);
		removeNode.setVisible(false);
	}

	public void nodeSelected(NodeEvent e) {
		selectedNode = e.getNode();
		
		idField.updateNodes(bgp.getNodes());
		idField.setText("" + selectedNode.getId());
		createNode.setText("Edit Node");
		removeNode.setVisible(true);

		int index = 0;
		
		for (int i = 0; i < types.getItemCount(); i++) {
			if (selectedNode.getType().equals(types.getItemAt(i))) {
				index = i;
				break;
			}
		}
		
		types.setSelectedIndex(index);
		
		((AttributePanel) nodeAttributes.getComponents()[index]).setFields(selectedNode.getData(), selectedNode.getDepth());
	}
	
	public void nodeDeselected(NodeEvent e) {
		idField.setText("" + (e.getNode().getId() + 1));
		
		for (Component c : nodeAttributes.getComponents())
			((AttributePanel) c).resetFields();
		
		createNode.setText("Create Node");
		createNode.setEnabled(false);
		removeNode.setVisible(false);
	}
	
	public void nodeEdited(NodeEvent e) {
		idField.updateNodes(bgp.getNodes());
	}

	public void fieldFoundInvalid(ValidationEvent e) {
		createNode.setEnabled(false);		
	}
	
	public void fieldFoundValid(ValidationEvent e) {
		createNode.setEnabled(idField.isValid() && 
				((AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()]).hasValidInfo());		
	}
	
}
