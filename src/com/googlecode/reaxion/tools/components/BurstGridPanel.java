package com.googlecode.reaxion.tools.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.googlecode.reaxion.tools.BurstGridEditor;

public class BurstGridPanel extends JPanel {

	private static ArrayList<Image> nodeIcons;
	
	private Image selectedIcon;
	
	private BufferedImage backbuffer;
	
	private Dimension size;
	
	public BurstGridPanel(Dimension size) {
		super();
		this.size = size;
		
		setPreferredSize(new Dimension(size.width, size.height));

		backbuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		
		initIcons();
	}
	
	private void initIcons() {
		nodeIcons = new ArrayList<Image>();
		
		try {
			for (String s : BurstGridEditor.nodeTypes) 
				nodeIcons.add(ImageIO.read(new File(BurstGridEditor.nodeIconDir + s + ".png")));

			selectedIcon = nodeIcons.get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		update(g);
	}
	
	public void update(Graphics g) {
		Graphics2D g2 = backbuffer.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.white);
		g2.fillRect(0, 0, backbuffer.getWidth(), backbuffer.getHeight());
		
		g.drawImage(backbuffer, 0, 0, null);
	}
}
