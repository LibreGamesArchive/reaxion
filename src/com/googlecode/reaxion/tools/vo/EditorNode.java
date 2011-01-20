package com.googlecode.reaxion.tools.vo;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Stores data for nodes and has methods for manipulating connections.
 * 
 * @author Brian Clanton
 *
 */
public class EditorNode implements Comparable<EditorNode> {

	private int id;
	private Point position;
	private int depth;
	private HashMap<Integer, Integer> connections;
	private int cost;
	private String data, type;
	private Color color;
	
	private boolean selected = false;
	private boolean selectedForConnection = false;
	
	public EditorNode(int id, String type, String data, int depth) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.depth = depth;
		
		connections = new HashMap<Integer, Integer>();
	}
	
	public void addConnection(int id, int cost) {
		connections.put(id, cost);
	}
	
	public void editConnection(int id, int cost) {
		if (connections.containsKey(id)) {
			connections.remove(id);
			connections.put(id, cost);
		}
	}
	
	public void removeConnection(int id) {
		if (connections.containsKey(id))
			connections.remove(id);
	}
	
	public ArrayList<Integer> getConnectionIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.addAll(connections.keySet());
		
		return ids;
	}
	
	public boolean hasConnectionId(int id) {
		return connections.containsKey(id);
	}
	
	public int getConnectionCost(int id) {
		if (connections.containsKey(id))
			return connections.get(id);
		else
			return -1;
	}

	public String toString() {
		String s = "" + id + " " + type + " " + data + "-";
		
		Iterator<Integer> itr = connections.keySet().iterator();
		
		while (itr.hasNext()) {
			int i = itr.next();
			s += i + "," + connections.get(i) + ", ";
		}
		
		s.trim();
		s += "-" + position.x + "," + position.y + "," + depth;
		
		return s;
	}
	
	public int compareTo(EditorNode e) {
		return id - e.getId();
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
	
	public int getCost() {
		return cost;
	}

	protected void setCost(int cost) {
		this.cost = cost;
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

}
