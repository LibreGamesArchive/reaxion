package com.googlecode.reaxion.tools.vo;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class EditorNode {

	private int id;
	private Point position;
	private int depth;
	private ArrayList<Integer> connections;
	private String data, type;
	private Color color;
	
	public EditorNode(int id, String type, String data) {
		this.id = id;
		this.type = type;
		this.data = data;
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

	public ArrayList<Integer> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Integer> connections) {
		this.connections = connections;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
