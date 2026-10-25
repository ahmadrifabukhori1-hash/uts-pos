package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Koneksi MySQL. Atur lewat properti sistem bila perlu:
 * kasir.db.url, kasir.db.user, kasir.db.password.
 * Default: database db_uts_pos di localhost:3306, user root tanpa password.
 */
public final class DatabaseConnection {

    private static final String URL = System.getProperty("kasir.db.url",
            "jdbc:mysql://localhost:3306/db_uts_pos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta");
    private static final String USER = System.getProperty("kasir.db.user", "root");
    private static final String PASSWORD = System.getProperty("kasir.db.password", "");

    private DatabaseConnection() {
    }

    /** Membuka koneksi baru. Pemanggil wajib menutupnya (try-with-resources). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Memeriksa koneksi; dilempar SQLException jika MySQL/database tidak bisa dijangkau. */
    public static void periksa() throws SQLException {
        try (Connection c = getConnection()) {
            c.isValid(2);
        }
    }

    /** Nama database dari URL, contoh: db_uts_pos. */
    public static String namaDatabase() {
        String s = URL;
        int garis = s.indexOf('/', s.indexOf("//") + 2);
        if (garis < 0) {
            return "";
        }
        s = s.substring(garis + 1);
        int tanya = s.indexOf('?');
        return tanya >= 0 ? s.substring(0, tanya) : s;
    }

    /**
     * Mengosongkan semua tabel. HANYA diizinkan untuk database yang namanya
     * berakhiran "_test" (dipakai pengujian), agar data asli tidak terhapus.
     */
    public static void kosongkanUntukUji() {
        if (!namaDatabase().endsWith("_test")) {
            throw new IllegalStateException("Pengosongan tabel hanya boleh untuk database *_test, bukan \""
                    + namaDatabase() + "\". Atur -Dkasir.db.url ke database uji.");
        }
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            st.execute("TRUNCATE TABLE transaction_details");
            st.execute("TRUNCATE TABLE transactions");
            st.execute("TRUNCATE TABLE products");
            st.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException ex) {
            throw new crud.DataException("Gagal mengosongkan database uji: " + ex.getMessage(), ex);
        }
    }
}
