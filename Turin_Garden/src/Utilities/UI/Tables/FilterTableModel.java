package Utilities.UI.Tables;

import Utilities.UI.HideList;
import javafx.scene.control.Tab;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Table model la quale struttura e' mutevole seguendo gli HideList, e' possibile rendere invisibili delle colonne senza modificare la struttura generale della tabella
 * @author Andrea Marco Farioli
 */
public class FilterTableModel extends AbstractTableModel {

    private boolean editable = false;
    private HideList<Object[]> data;
    private HideList<Object> column;
    private HideList<Integer> getter = new HideList<>();

    public FilterTableModel(Object[][] data, Object[] columns){
        this.column = new HideList<>(Arrays.asList(columns));
        for (int i = 0; i < this.column.size(); i++){
            getter.add(i);
        }

        this.data = new HideList<>(Arrays.asList(data));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return this.column.getContent().length;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Object[] obj = getter.getContent();
        return data.get(i)[(Integer)obj[i1]];
    }

    @Override
    public String getColumnName(int column) {
        return (String)this.column.getContent()[column];
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void addRow(TableObject row){
        data.add(row.getContent());
        fireTableRowsInserted(0, data.size());
    }

    public void removeRow(TableObject row){
        data.remove(row.getContent());
        fireTableRowsDeleted(0, data.size());
    }

    public void setColumns(Object[] columns){
        column = new HideList<>(Arrays.asList(columns));
        getter.clear();
        for (int i = 0; i < this.column.size(); i++){
            getter.add(i);
        }

        fireTableStructureChanged();
    }

    public void addColumn(Object column){
        getter.add(this.column.size());
        this.column.add(column);
        fireTableStructureChanged();
    }

    public void removeColumn(Object column){
        this.column.remove(column);
        getter.remove(getter.size()-1);
        fireTableStructureChanged();
    }

    public void hideColumn(int index){
        this.column.setInvisible(index);
        getter.setInvisible(index);
        fireTableStructureChanged();
    }

    public void showColumn(int index){
        this.column.setVisible(index);
        getter.setVisible(index);
        fireTableStructureChanged();
    }

    public void removeColumn(int index){
        this.column.remove(index);
        getter.remove(getter.size()-1);
        fireTableStructureChanged();
    }

    public void setData(HideList<Object[]> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public void setRow(int index, TableObject value){
        data.set(index, value.getContent());
        fireTableDataChanged();
    }
}
