package com.googlecode.reaxion.game.burstgrid;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import com.googlecode.reaxion.game.burstgrid.node.BurstNode;
import com.googlecode.reaxion.tools.events.NodeEvent;
import com.googlecode.reaxion.tools.listeners.NodeEventListener;


public class BurstGridEditor extends JApplet implements MouseInputListener{
	
	int width = 480, height = 480;
	int mx, my, x, y;
	int numLines = 24;
	ArrayList<Point> nodes, drawNodes;
	ArrayList<BurstNode> grid = new ArrayList<BurstNode>();
	ArrayList<String> conns;

	public BurstNode b;
	public JFrame frame = new JFrame();
	public String[] typeList = {"Ability", "Attack", "HP", "MaxGauge", "MinGauge", "Rate", "Strength"};
	public String loc = "com.googlecode.reaxion.game.burstgrid.node.";

	public JComboBox types = new JComboBox(typeList);
	public JLabel idLabel = new JLabel("ID No.");
	public JTextField idBox = new JTextField("1", 8);
	public JTextField info = new JTextField("Info", 20);
	JButton finish = new JButton("Finish");

	boolean clickable = true; //checks to see if you can add another node

	BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	public void init() {
		setSize(width, height);
		setBackground( Color.white );
		Graphics g2 = buffer.getGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);

		mx = width/2;
		my = height/2;
		nodes = new ArrayList<Point>();
		drawNodes = new ArrayList<Point>();

		finish.setEnabled(false);

		finish.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(!clickable)
					try {
						boolean duplicate = false;
						for(BurstNode node: grid){
							if(node.id == Integer.parseInt(idBox.getText())){
								duplicate = true;
								System.out.println("This node already exists!");
								break;
							}
						}		
						if(!duplicate){
							System.out.println((String)types.getSelectedItem());
							if(((String)types.getSelectedItem()).contains("ili") || ((String)types.getSelectedItem()).contains("tta"))
								b = (BurstNode) (Class.forName(loc + (String)types.getSelectedItem() + "Node").getConstructors()[1].newInstance(info.getText(), Integer.parseInt(idBox.getText())));
							else
								b = (BurstNode) (Class.forName(loc + (String)types.getSelectedItem() + "Node").getConstructors()[1].newInstance(Integer.parseInt(info.getText()), Integer.parseInt(idBox.getText())));

							idBox.setText("" + (Integer.parseInt(idBox.getText())+1));
							grid.add(b);
							System.out.println(grid);
							System.out.println(nodes);
						}
					} catch (IllegalArgumentException e) {
					} catch (SecurityException e) {
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {
					} catch (InvocationTargetException e) {
					} catch (ClassNotFoundException e) {
					}
					finish.setEnabled(clickable);
					clickable = true;
			}

		});

		frame.setLayout(new BorderLayout());

		JPanel idpanel = new JPanel();
		idpanel.setLayout(new FlowLayout());
		idpanel.add(idLabel);
		idpanel.add(idBox);

		frame.add(idpanel, BorderLayout.NORTH);
		frame.add(types, BorderLayout.WEST);
		frame.add(info, BorderLayout.EAST);
		frame.add(finish, BorderLayout.SOUTH);

		frame.setVisible(true);
		frame.setBounds(510, 0, 350, 120);

		addMouseListener( this );
		addMouseMotionListener( this );
	}

	public void mouseEntered( MouseEvent e ) {
		// called when the pointer enters the applet's rectangular area
	}
	public void mouseExited( MouseEvent e ) {
	}

	public void mouseClicked( MouseEvent e ) {
		boolean contains = false;
		if(clickable){
			mx = e.getX() - width/2;
			my = (e.getY() - height/2)*-1;
			
			int a = width/numLines;
			int b = height/numLines;
			
			x = a*((mx+(numLines/2))/a)+ 0*(int)Math.copySign(1, mx);
			y =  -1*(b*((my+(numLines/2)*(int)Math.copySign(1, my))/b));		

			Point p = new Point(x, y);
			drawNodes.add(p);
			if(nodes.isEmpty()){
				nodes.add(p);
				finish.setEnabled(clickable);
				clickable = false;
			}
			else{
				for(Point pnt: nodes)
					if(pnt.equals(p)){
						int i = nodes.indexOf(pnt);
						BurstNode temp = grid.get(i);
						idBox.setText("" + temp.id);
						info.setText(temp.toString());
						contains = true;
						break;
					}
				if(!contains){
					nodes.add(p);
					//BurstNode b = createNode(p);
					finish.setEnabled(clickable);
					clickable = false;
				}
			}
		}
		repaint();
		e.consume();
	}

	public void mousePressed( MouseEvent e ) {

	}
	
	public void mouseReleased( MouseEvent e ) {
	
	}
	
	public void mouseMoved( MouseEvent e ) {
		mx = e.getX() - width/2;
		my = (e.getY() - height/2)*-1;

		showStatus( "Mouse at (" + mx + "," + my + ")" );
		repaint();
		e.consume();
	}
	public void mouseDragged( MouseEvent e ) { 
		mx = e.getX() - width/2;
		my = (e.getY() - height/2)*-1;

		showStatus( "Mouse at (" + mx + "," + my + ")" );
		repaint();
		e.consume();
	}

	public void paint( Graphics g ) {
		Graphics g2 = buffer.getGraphics();
		Graphics2D g2d = (Graphics2D)g2;

		g2.translate(width/2, height/2);
		//		g2.setColor(Color.blue);
		//		g2.fillRect(x, y, 10, 10);
		//		g.drawImage(buffer, 0, 0, null);
		//		g2.setColor(Color.white);
		//		g2.fillRect(x, y, 10, 10);

		g2.translate(-width/2,-height/2);
		g2.setColor(Color.black);
		for(int i = 0; i < numLines; i++){
			g2.drawLine(i*width/numLines, 0, i*width/numLines, height);
			g2.drawLine(0, i*height/numLines, width, i*height/numLines);
		}
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine(0, height/2, width, height/2);
		g2d.drawLine(width/2, 0, width/2, height);
		
		g2.translate(width/2,height/2);

		g2.setColor(Color.blue);
		for(Point p: drawNodes)
			g2.fillRect(p.x-5,p.y-5,10,10);
		g.drawImage(buffer, 0, 0, null);
	}
	
}