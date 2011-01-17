package com.googlecode.reaxion.tools.vo;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class EditorNode implements Comparable<EditorNode> {

	private int id;
	private Point position;
	private int depth;
	private ArrayList<Integer> nodes;
	private ArrayList<Integer> costs;
	private String data, type, costString;
	private Color color;
	
	private boolean selected = false;
	private boolean selectedForConnection = false;
	
	public EditorNode(int id, String type, String data, int depth) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.depth = depth;
		
		nodes = new ArrayList<Integer>();
		costs = new ArrayList<Integer>();
	}
	
	public void addConnection(EditorNode n) {
		nodes.add(n.getId());
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
	
	public String getCostString(){
		return costString;
	}
	
	public void setCostString(String s){
		costString = s;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelectedForConnection() {
		return selectedForConnection;
	}

	public void setSelectedForConnection(boolean selectedForConnection) {
		this.selectedForConnection = selectedForConnection;
	}

	public int compareTo(EditorNode e) {
		return id - e.getId();
	}
}
