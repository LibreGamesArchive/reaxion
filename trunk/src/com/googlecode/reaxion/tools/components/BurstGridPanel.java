package com.googlecode.reaxion.tools.components;

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

public class BurstGridPanel extends JPanel implements MouseListener {

	private static final int lineCount = 25;
	private static final Color[] nodeColors = {Color.gray, Color.blue, Color.pink,
		Color.magenta.darker(), Color.green, Color.yellow.darker(), Color.red};
	
	private ArrayList<Point> nodes, drawNodes;
	
	private Color nodeColor;
	
	boolean clickable; //checks to see if you can add another node
	
	private BufferedImage backbuffer;
	
	public BurstGridPanel(Dimension size) {
		super();
		
		nodes = new ArrayList<Point>();
		drawNodes = new ArrayList<Point>();
		
		setPreferredSize(new Dimension(size.width, size.height));

		backbuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		
		addMouseListener(this);
	}
	
	public void createSelectedNode(int index) {
		nodeColor = nodeColors[index];
		clickable = true;
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
		
		for(int i = 0; i < lineCount; i++){
			g2.drawLine(i*getWidth()/25, 0, i*getWidth()/25, getHeight());
			g2.drawLine(0, i*getHeight()/25, getWidth(), i*getHeight()/25);
		}

		g2.translate(getWidth()/2,getHeight()/2);
		
		g2.setColor(nodeColor);
		for(Point p: drawNodes)
			g2.fillRect(p.x-5,p.y-5,10,10);
		
		g.drawImage(backbuffer, 0, 0, null);
	}

	public void mouseClicked(MouseEvent e) {
		boolean contains = false;
		if(clickable){
			int mouseX = e.getX() - getWidth()/2;
			int mouseY = (e.getY() - getHeight()/2)*-1;

			int x = 20*((mouseX+5)/20)+10*(int)Math.copySign(1, mouseX);
			int y =  -1*(20*((mouseY+5)/20))-10*(int)Math.copySign(1, mouseY);		

			Point p = new Point(x, y);
			drawNodes.add(p);
			if(nodes.isEmpty()){
				nodes.add(p);
				clickable = false;
			}
			else{
				for(Point pnt: nodes)
					if(pnt.equals(p)){
						contains = true;
						break;
					}
				if(!contains){
					nodes.add(p);
					//BurstNode b = createNode(p);
					clickable = false;
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
	
}
