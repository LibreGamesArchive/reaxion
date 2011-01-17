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

public class BurstGridEditor extends JFrame implements ActionListener, NodeEventListener, ValidationEventListener {

	public static final String gridDir = "src/com/googlecode/reaxion/resources/burstgrid/";
	public static final String nodeIconDir = "src/com/googlecode/reaxion/tools/icons/";
	public static final String[] nodeTypes = {"ability", "attack", "gauge1", "gauge2", "hp", "rate", "strength"};
	public static final String[] attributes = {"Ability Name", "Attack Name", "Min Gauge Plus", "Max Gauge Plus", 
		"HP Plus", "Rate Plus", "Strength Plus"};

	private static int nodeID = 1;

	private JFileChooser fileChooser;

	private NodeIDField idField;
	private JComboBox character;
	private BurstGridPanel bgp;
	private JPanel nodeAttributes;
	private JComboBox types;
	private ToolButton createNode;
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

	private void initMenu() {
		JMenu file = new JMenu("File");
		file.add(new ToolMenuItem("Save Grid", this));
		file.add(new JSeparator());
		file.add(new ToolMenuItem("Exit", this));

		JMenuBar bar = new JMenuBar();
		bar.add(file);

		setJMenuBar(bar);
	}

	private void initToolbar() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panel.setPreferredSize(new Dimension(250, 480));
		JPanel toolbar = new JPanel();
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
		
		costsLabel = new JLabel("Costs: ");
		costs = new JTextField();
		costs.setEnabled(false);

		addComponentToToolbar(character, toolbar, true);
		addComponentToToolbar(new JSeparator(), toolbar, true);
		addComponentToToolbar(idField, toolbar, true);
		addComponentToToolbar(typeDesc, toolbar, true);
		addComponentToToolbar(types, toolbar, true);
		addComponentToToolbar(nodeAttributes, toolbar, true);
		addComponentToToolbar(costsLabel, toolbar, true);
		addComponentToToolbar(costs, toolbar, true);
		addComponentToToolbar(createNode, toolbar, false);

		panel.add(toolbar);
		add(panel);
	}

	private void initAttributesPanel() {
		nodeAttributes = new JPanel(new CardLayout());

		for (int i = 0; i < attributes.length; i++)
			nodeAttributes.add(new AttributePanel(attributes[i], i >= 2 ? true : false, this), attributes[i]);

		CardLayout c = (CardLayout) nodeAttributes.getLayout();
		c.show(nodeAttributes, attributes[0]);
	}

	private void addComponentToToolbar(JComponent c, JPanel toolbar, boolean hasSpacing) {
		toolbar.add(c);
		c.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		if (hasSpacing)
			toolbar.add(Box.createRigidArea(new Dimension(230, 10)));
	}

	private ImageIcon getIcon(String filename) {
		try {
			BufferedImage original = ImageIO.read(new File(filename));
			int scaledWidth = (int) original.getWidth() / 2;
			int scaledHeight = (int) original.getHeight() / 2;
			BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = scaled.createGraphics();
			AffineTransform scale = AffineTransform.getScaleInstance(.5, .5);
			g.drawRenderedImage(original, scale);
			return new ImageIcon(scaled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

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
			CardLayout c = (CardLayout) nodeAttributes.getLayout();
			c.show(nodeAttributes, attributes[((JComboBox) e.getSource()).getSelectedIndex()]);
		} else if (command.equals("Create Node")) {
			AttributePanel temp = (AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()];
			EditorNode node = new EditorNode(idField.getId(), (String)types.getSelectedItem(), temp.getData(), temp.getDepth());
			bgp.createSelectedNode(types.getSelectedIndex(), node);
		} else if (command.equals("Edit Node")) {
			createNode.setText("Create Node");
			bgp.applyChanges(selectedNode);
			selectedNode = null;
		} else if (command.equals("Save Grid")) {
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
		createNode.setEnabled(true);
		nodeID++;
		idField.updateNodes(bgp.getNodes());
		idField.reset();

		for (Component c : nodeAttributes.getComponents())
			((AttributePanel) c).resetFields();
	}

	public void nodeRemoved(NodeEvent e) {
		idField.updateNodes(bgp.getNodes());		
	}

	/**
	 * TODO Make this method change the action performed by the CreateNode button so that when clicked,
	 * the currently selected node's information is edited to match the information in the boxes on the left
	 */
	public void nodeSelected(NodeEvent e) {
		// TODO Auto-generated method stub
		selectedNode = e.getNode();
		System.out.println(selectedNode);
		idField.updateNodes(bgp.getNodes());
		idField.setText("" + selectedNode.getId());
		createNode.setText("Edit Node");

		int index = 0;
		
		for (int i = 0; i < nodeTypes.length; i++) {
			if (selectedNode.getType().equals(nodeTypes[i])) {
				index = i;
				break;
			}
		}
		
		((AttributePanel) nodeAttributes.getComponents()[index]).setFields(selectedNode.getData(), selectedNode.getDepth());
	}

	public void fieldFoundInvalid(ValidationEvent e) {
		createNode.setEnabled(false);		
	}
	
	public void fieldFoundValid(ValidationEvent e) {
		createNode.setEnabled(idField.isValid() && 
				((AttributePanel) nodeAttributes.getComponents()[types.getSelectedIndex()]).hasValidInfo());		
	}
	
}
