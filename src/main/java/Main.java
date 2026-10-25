import config.DatabaseConnection;
import javax.swing.JOptionPane;
import view.MainForm;

/** Titik awal aplikasi. */
public class Main {

    public static void main(String[] args) {
        // Tema FlatLaf (hanya mengubah tampilan, tanpa mengubah susunan form).
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Throwable ex) {
            System.err.println("FlatLaf tidak dapat dipakai, memakai tampilan standar: " + ex);
        }

        // Kegagalan database di tengah pemakaian ditampilkan sebagai pesan, bukan hanya di konsol.
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage(),
                    "Kesalahan", JOptionPane.ERROR_MESSAGE);
        });

        try {
            DatabaseConnection.periksa();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Tidak dapat terhubung ke database \"" + DatabaseConnection.namaDatabase() + "\".\n"
                    + "Pastikan MySQL berjalan dan database-schema.sql sudah dijalankan.\n\n" + ex.getMessage(),
                    "Koneksi Database Gagal", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }
}
