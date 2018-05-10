package com.db4J.glb;

import javax.swing.table.DefaultTableModel;

/**
 * @author: love720720@163.com
 * @date: 2018年04月26日 09时50分
 */
public class TableModel extends DefaultTableModel {

    private static final long serialVersionUID = 9196990665580875650L;

    public TableModel(Object[][] data, String[] columns) {
        super(data, columns);
    }

    // 设置Table单元格是否可编辑
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return true;
        }
        return false;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }
        return Object.class;
    }
}
