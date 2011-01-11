package com.googlecode.reaxion.tools.components;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class DialogueTable extends JTable {

	public DialogueTable() {
		setModel(new DialogueTableModel());
	}
	
	public void addRow() {
		((DialogueTableModel) getModel()).addRow();
	}
	
	public ArrayList<String> getTextBlocks() {
		ArrayList<String> blocks = new ArrayList<String> ();
		ArrayList<ArrayList<String>> d = ((DialogueTableModel) getModel()).data;
		
		for (int i = 0; i < d.size(); i++) {
			ArrayList<String> s = d.get(i);
			blocks.add(s.get(0) + "::" + s.get(1));			
		}
		
		return blocks;
	}
	
	private class DialogueTableModel extends AbstractTableModel {

		private String[] columnNames = { "Name", "Dialogue" };
		private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

		public DialogueTableModel() {
			super();

			addRow();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.size();
		}

		public Object getValueAt(int row, int column) {
			return data.get(row).get(column);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			default:
				return String.class;
			}
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			ArrayList<String> c = data.get(row);
			c.set(column, (String) value);
			fireTableCellUpdated(row, column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public void addRow() {
			data.add(new ArrayList<String>(Arrays
					.asList(new String[] { "", "" })));
			fireTableRowsInserted(data.size() - 1, data.size() - 1);
		}

	}
}
