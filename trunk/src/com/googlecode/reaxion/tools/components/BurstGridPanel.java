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

import javax.swing.JPanel;

import com.googlecode.reaxion.tools.events.NodeEvent;
import com.googlecode.reaxion.tools.listeners.NodeEventListener;
import com.googlecode.reaxion.tools.vo.EditorNode;

public class BurstGridPanel extends JPanel implements MouseListener {

	private NodeEventListener listener;
	
	private static final int lineCount = 24;
	private static final Color[] nodeColors = {Color.gray, Color.blue, Color.pink,
		Color.magenta.darker(), Color.green, Color.yellow.darker(), Color.red};
	
	private ArrayList<EditorNode> nodes;
	
	private Color nodeColor;
	
	private EditorNode currentNode;
	
	boolean clickable; //checks to see if you can add another node to the grid 
	
	private BufferedImage backbuffer;
	
	public BurstGridPanel(Dimension size) {
		super();
		
		System.out.println(size.toString());
		
		nodes = new ArrayList<EditorNode>();
		
		setPreferredSize(new Dimension(size.width, size.height));

		backbuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		
		addMouseListener(this);
	}
	
	public void createSelectedNode(int index, EditorNode node) {
		nodeColor = nodeColors[index];
		currentNode = node;
		currentNode.setColor(nodeColor);
		clickable = true;
		fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.CREATED));
	}
	
	public ArrayList<EditorNode> getNodes()	{
		return nodes;
	}
	
	public void paintComponent(Graphics g) {
		update(g);
	}
	
	public void update(Graphics g) {
		Graphics2D g2 = backbuffer.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, backbuffer.getWidth(), backbuffer.getHeight());
		
//		g2.translate(size.width/2, size.height/2);
//		//		g2.setColor(Color.blue);
//		//		g2.fillRect(x, y, 10, 10);
//		//		g.drawImage(buffer, 0, 0, null);
//		//		g2.setColor(Color.white);
//		//		g2.fillRect(x, y, 10, 10);
//
//		g2.translate(-size.width/2,-size.height/2);
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
		
		for(EditorNode e: nodes){
			g2.setColor(e.getColor());
			g2.fillRect(e.getPosition().x-5,e.getPosition().y-5,10,10);
		}
		
		g.drawImage(backbuffer, 0, 0, null);
	}

	public void mouseClicked(MouseEvent e) {
		boolean contains = false;
		if(clickable){
			int mouseX = e.getX() - getWidth()/2;
			int mouseY = (e.getY() - getHeight()/2)*-1;

			int a = getWidth()/lineCount;
			int b = getHeight()/lineCount;
			
			int x = a*((mouseX+5)/a); //+a/2*(int)Math.copySign(1, mouseX);
			int y =  -1*(b*((mouseY+5)/b)); //-b/2*(int)Math.copySign(1, mouseY);		

			Point p = new Point(x, y);
			if(nodes.isEmpty()){
				currentNode.setPosition(p);
				nodes.add(currentNode);
				clickable = false;
				fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.ADDED));
				currentNode = null;
			}
			else{
				for(EditorNode en: nodes)
					if(en.getPosition().equals(p)){
						contains = true;
						break;
					}
				if(!contains){
					currentNode.setPosition(p);
					nodes.add(currentNode);
					clickable = false;
					fireNodeEvent(new NodeEvent(this, currentNode, NodeEvent.ADDED));
					currentNode = null;
				}
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
		listener = n;
	}
	
	public synchronized void removeNodeEventListener(NodeEventListener n) {
		listener = null;
	}
	
	private synchronized void fireNodeEvent(NodeEvent event) {
		if (listener != null) {
			if (event.getType().equals(NodeEvent.ADDED))
				listener.nodeAdded(event);
			else if (event.getType().equals(NodeEvent.REMOVED))
				listener.nodeRemoved(event);
			else if (event.getType().equals(NodeEvent.CREATED))
				listener.nodeCreated(event);
		}
	}	
	
}
