package com.googlecode.reaxion.tools.vo;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class EditorNode {

	private int id;
	private Point position;
	private int depth;
	private ArrayList<Integer> nodes;
	private ArrayList<Integer> costs;
	private String data, type;
	private Color color;
	
	public EditorNode(int id, String type, String data) {
		this.id = id;
		this.type = type;
		this.data = data;
		
		nodes = new ArrayList<Integer>();
		costs = new ArrayList<Integer>();
	}

	public String toString() {
		String s = "" + id + " " + type + " " + data + "-";
		
		for (int i = 0; i < nodes.size(); i++)
			s += nodes.get(i) + "," + costs.get(i) + ", ";
		
		s.trim();
		s += "-" + position.x + "," + position.y + "," + depth;
		
		return s;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public ArrayList<Integer> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Integer> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Integer> getCosts() {
		return costs;
	}

	public void setCosts(ArrayList<Integer> costs) {
		this.costs = costs;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
