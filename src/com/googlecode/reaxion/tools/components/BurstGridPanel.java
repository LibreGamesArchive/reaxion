package com.googlecode.reaxion.tools.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import com.googlecode.reaxion.tools.events.NodeEvent;
import com.googlecode.reaxion.tools.listeners.NodeEventListener;
import com.googlecode.reaxion.tools.vo.EditorNode;

/**
 * Contains a grid for node addition, removal, and editing. 
 * 
 * @author Brian Clanton, Cy Neita
 *
 */
public class BurstGridPanel extends JPanel implements MouseListener {

	private ArrayList<NodeEventListener> listeners = new ArrayList<NodeEventListener>();
	
	private static final int lineCount = 24;
	private static final Color[] nodeColors = {Color.gray, Color.blue, Color.pink,
		Color.magenta.darker(), Color.green.darker(), Color.yellow.darker(), Color.red};
	
	private HashMap<Integer, EditorNode> nodes;
	
	private EditorNode currentNode;
	
	boolean clickable; //checks to see if you can add another node to the grid 
	
	private BufferedImage backbuffer;
	
	public BurstGridPanel(Dimension size) {
		super();
		
		System.out.println(size.toString());
		
		nodes = new HashMap<Integer, EditorNode>();
		
		setPreferredSize(new Dimension(size.width, size.height));

		backbuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		
		addMouseListener(this);
	}
	
	/**
	 * Creates a node and uses {@code index} for color assignment.
	 * 
	 * @param index Index of the node type in {@code BurstGridEditor} {@code JComboBox}. Used for color assignment.
	 * @param node {@code EditorNode} to be added to the grid.
	 */
	public void createSelectedNode(int index, EditorNode node) {
		currentNode = node;
		currentNode.setColor(nodeColors[index]);
		clickable = true;
		fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.CREATED));
	}
	
	/**
	 * Returns an {@code ArrayList<EdtitorNode>} of all the nodes currently on the grid. Used for validation for 
	 * {@code NodeIDField}.
	 * 
	 * @return {@code ArrayList<EdtitorNode>} of all the nodes currently on the grid.
	 */
	public ArrayList<EditorNode> getNodes() {
		ArrayList<EditorNode> temp = new ArrayList<EditorNode>();

		Iterator<Integer> itr = nodes.keySet().iterator();
		
		while (itr.hasNext())
			temp.add(nodes.get(itr.next()));
		
		return temp;
	}
	
	/**
	 * Creates connections between the currently selected node and nodes selected for connection.
	 */
	private void createConnections() {
		currentNode.setSelected(false);
		
		Iterator<Integer> itr = nodes.keySet().iterator();
		
		while (itr.hasNext()) {
			EditorNode n = nodes.get(itr.next());
			if (n.isSelectedForConnection()) {
				currentNode.addConnection(n);
				currentNode.setCostString(currentNode.getCostString() + " " + n.getId() + ", 1");
				n.setSelectedForConnection(false);
			}
		}
	}
	
	/**
	 * Applies attribute change to an already created node.
	 * 
	 * @param index Index of the node type in {@code BurstGridEditor} {@code JComboBox}. Used for color assignment.
	 * @param selectedNode {@code EditorNode} to be edited.
	 */
	public void applyChanges(int index, EditorNode selectedNode) {
		nodes.remove(currentNode.getId());
		selectedNode.setColor(nodeColors[index]);
		currentNode = selectedNode;
		nodes.put(currentNode.getId(), currentNode);
		fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.EDITED));
	}
	
	/**
	 * Removes the from the grid the {@code EditorNode} with the specified id.
	 * 
	 * @param id Id of the {@code EditorNode} to be removed.
	 */
	public void removeSelectedNode() {
		nodes.remove(currentNode.getId());
		
		Iterator<Integer> itr = nodes.keySet().iterator();
		
		while (itr.hasNext()) {
			EditorNode n = nodes.get(itr.next());
			
			if (n.getNodes().contains(currentNode.getId()))
				n.getNodes().remove((Object) currentNode.getId());
		}
		
		fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.REMOVED));
		currentNode = null;
	}
	
	private void setCosts(){
		
	}
	
	/**
	 * Checks to see if a location is inside the bounds of the graphical representation of a node. Used for mouse clicks.
	 * 
	 * @param n Node location.
	 * @param p Mouse location.
	 * @return {@code true} if within the node, {@code false} if not within the node.
	 */
	private boolean isWithinNode(Point n, Point p) {
		boolean check = n.x - 10 <= p.x && n.x + 10 >= p.x && n.y + 10 >= p.y && n.y - 10 <= p.y;
		System.out.println(n + " || " + p + " || " + check);
		return check;
	}
	
	public void paintComponent(Graphics g) {
		update(g);
	}
	
	/**
	 * Draws grid, nodes on the grid, and connections between nodes.
	 */
	public void update(Graphics g) {
		Graphics2D g2 = backbuffer.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, backbuffer.getWidth(), backbuffer.getHeight());
	
		g2.setColor(Color.black);
		
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
		g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		g2.setStroke(new BasicStroke(1));
			
		for(int i = 0; i < lineCount; i++){
			g2.drawLine(i*getWidth()/lineCount, 0, i*getWidth()/lineCount, getHeight());
			g2.drawLine(0, i*getHeight()/lineCount, getWidth(), i*getHeight()/lineCount);
		}
		
		g2.translate(getWidth()/2,getHeight()/2);
		
		drawConnections(g2);

		Iterator<Integer> itr = nodes.keySet().iterator();
		
		while (itr.hasNext()) {
			EditorNode e = nodes.get(itr.next());
			
			if (e.isSelected()) {
				g2.setColor(Color.green);
				g2.fillRect(e.getPosition().x - 8, e.getPosition().y - 8, 16, 16);
			} else if (e.isSelectedForConnection()) {
				g2.setColor(Color.blue.darker().darker());
				g2.fillRect(e.getPosition().x - 8, e.getPosition().y - 8, 16, 16);
			}
			
			g2.setColor(e.getColor());
			g2.fillRect(e.getPosition().x-5,e.getPosition().y-5,10,10);
		}
		
		g.drawImage(backbuffer, 0, 0, null);
	}
	
	/**
	 * Draws connections between nodes.
	 * @param g2 {@code Graphics2D} object from panel.
	 */
	private void drawConnections(Graphics2D g2){
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(2));
		
		Iterator<Integer> itr = nodes.keySet().iterator();
		
		while (itr.hasNext()) {
			EditorNode n = nodes.get(itr.next());
			for(int id : n.getNodes()){
				g2.drawLine(n.getPosition().x, n.getPosition().y, nodes.get(id).getPosition().x, nodes.get(id).getPosition().y);
			}
		}
		
		g2.setStroke(new BasicStroke());
	}

	public void mouseClicked(MouseEvent e) {
		boolean contains = false;
		int mouseX = e.getX() - getWidth()/2;
		int mouseY = (e.getY() - getHeight()/2)*-1;
		
		int a = getWidth()/lineCount;
		int b = getHeight()/lineCount;
		
		int x = a*((mouseX+a/2*(int)Math.copySign(1, mouseX))/a);
		int y =  -1*(b*((mouseY+b/2*(int)Math.copySign(1, mouseY))/b));		
		
		Point p = new Point(x, y);

		if(clickable){
			if(nodes.isEmpty()){
				currentNode.setPosition(p);
				nodes.put(currentNode.getId(), currentNode);
				fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.ADDED));
				clickable = false;
				currentNode = null;
			}
			else{
				Iterator<Integer> itr = nodes.keySet().iterator();
				
				while (itr.hasNext()) {
					EditorNode en = nodes.get(itr.next());
					if(en.getPosition().equals(p)){
						contains = true;
						break;
					}
				}
				if(!contains){
					currentNode.setPosition(p);
					nodes.put(currentNode.getId(), currentNode);
					fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.ADDED));
					clickable = false;
					currentNode = null;
				}
			}
		} else {
			EditorNode n = null;
			
			Iterator<Integer> itr = nodes.keySet().iterator();
			
			while (itr.hasNext()) {
				EditorNode en = nodes.get(itr.next());
				if(isWithinNode(en.getPosition(), p)){
					n = en;
					contains = true;
					break;
				}
			}
			
			if (contains) {
				if (currentNode == null || !currentNode.isSelected()) {
					currentNode = n;
					currentNode.setSelected(true);
					fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.SELECTED));
				} else {
					n.setSelectedForConnection(!n.isSelectedForConnection());
				}
			} else if (currentNode != null){
				fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.DESELECTED));

				createConnections();
				setCosts();
			}
		}
		repaint();
		e.consume();
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void addNodeEventListener(NodeEventListener n) {
		listeners.add(n);
	}
	
	public synchronized void removeNodeEventListener(NodeEventListener n) {
		listeners.remove(n);
	}
	
	private synchronized void fireNodeEvent(NodeEvent event) {
		if (listeners.size() != 0) {
			for (NodeEventListener n : listeners) {
				if (event.getType().equals(NodeEvent.ADDED))
					n.nodeAdded(event);
				else if (event.getType().equals(NodeEvent.REMOVED))
					n.nodeRemoved(event);
				else if (event.getType().equals(NodeEvent.CREATED))
					n.nodeCreated(event);
				else if (event.getType().equals(NodeEvent.SELECTED))
					n.nodeSelected(event);
				else if (event.getType().equals(NodeEvent.DESELECTED))
					n.nodeDeselected(event);
				else if (event.getType().equals(NodeEvent.EDITED))
					n.nodeEdited(event);
			}
			
			repaint();
		}
	}	
	
}
