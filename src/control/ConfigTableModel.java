package control;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * A table model for managing and displaying configuration entries in a JTable.
 * This model supports editable cells and dynamic updates for the configuration data.
 */
public class ConfigTableModel extends AbstractTableModel {

    private final List<ConfigEntry> entries;
    private static final String[] COLUMN_NAMES = {"Length (cm)", "Width (cm)", "Height (cm)", "Weight (g)", "Price (€)"};

    /**
     * Constructs a new ConfigTableModel with a list of configuration entries.
     *
     * @param entries The list of {@link ConfigEntry} objects to be displayed in the table
     */
    public ConfigTableModel(List<ConfigEntry> entries) {
        this.entries = entries;
    }

    /**
     * Returns the number of rows in the table, corresponding to the number of entries.
     *
     * @return The row count
     */
    @Override
    public int getRowCount() {
        return entries.size();
    }

    /**
     * Returns the number of columns in the table.
     *
     * @return The column count
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * Returns the name of the column at the specified index.
     *
     * @param column The column index
     * @return The name of the column
     */
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
     * Returns the value at the specified row and column.
     *
     * @param rowIndex    The row index
     * @param columnIndex The column index
     * @return The value at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ConfigEntry entry = entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getLength();
            case 1 -> entry.getWidth();
            case 2 -> entry.getHeight();
            case 3 -> entry.getWeight();
            case 4 -> entry.getPrice();
            default -> null;
        };
    }

    /**
     * Updates the value at the specified cell.
     *
     * @param aValue      The new value to set
     * @param rowIndex    The row index
     * @param columnIndex The column index
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ConfigEntry entry = entries.get(rowIndex);
        switch (columnIndex) {
            case 0 -> entry.setLength((Integer) aValue);
            case 1 -> entry.setWidth((Integer) aValue);
            case 2 -> entry.setHeight((Integer) aValue);
            case 3 -> entry.setWeight((Integer) aValue);
            case 4 -> entry.setPrice((Double) aValue);
            default -> throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Indicates whether the specified cell is editable.
     *
     * @param rowIndex    The row index
     * @param columnIndex The column index
     * @return {@code true} if the cell is editable; otherwise {@code false}
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true; // All cells are editable
    }

    /**
     * Returns the class type of the values in the specified column.
     *
     * @param columnIndex The column index
     * @return The class type of the column values
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1, 2, 3 -> Integer.class;
            case 4 -> Double.class;
            default -> Object.class;
        };
    }

    /**
     * Adds a new configuration entry to the table and notifies listeners.
     *
     * @param entry The {@link ConfigEntry} to add
     */
    public void addEntry(ConfigEntry entry) {
        entries.add(entry);
        int newRowIndex = entries.size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    /**
     * Removes a configuration entry at the specified index and notifies listeners.
     *
     * @param rowIndex The index of the entry to remove
     */
    public void removeEntry(int rowIndex) {
        entries.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
