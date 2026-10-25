package util;

import javax.swing.JTable;

/** Pembantu untuk JTable yang kolom pertamanya berisi id (Integer). */
public final class TableUtil {

    private TableUtil() {
    }

    /** Id pada baris yang dipilih, atau null jika tidak ada baris dipilih. */
    public static Integer getSelectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        Object value = table.getValueAt(row, 0);
        return value instanceof Integer ? (Integer) value : null;
    }

    /** Memilih baris yang kolom pertamanya sama dengan id. Tidak melakukan apa-apa jika id null atau tidak ada. */
    public static void selectById(JTable table, Integer id) {
        if (id == null) {
            return;
        }
        for (int row = 0; row < table.getRowCount(); row++) {
            if (id.equals(table.getValueAt(row, 0))) {
                table.setRowSelectionInterval(row, row);
                return;
            }
        }
    }
}
