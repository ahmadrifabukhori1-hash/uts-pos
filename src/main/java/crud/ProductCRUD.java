package crud;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Product;

/**
 * Penyimpanan produk di MySQL (tabel products). Setiap method membuka dan
 * menutup koneksinya sendiri, dan mengembalikan objek Product baru (salinan),
 * sehingga pemanggil tidak bisa mengubah data tersimpan tanpa lewat insert/update.
 */
public class ProductCRUD {

    private static final String KOLOM = "id, name, price, stock, is_active";

    /** Menyimpan produk baru; id dibuat oleh database. */
    public Product insert(Product product) {
        String sql = "INSERT INTO products (name, price, stock, is_active) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getStock());
            ps.setBoolean(4, product.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DataException("Database tidak mengembalikan id produk baru.");
                }
                return new Product(keys.getInt(1), product.getName(), product.getPrice(),
                        product.getStock(), product.isActive());
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal menyimpan produk: " + ex.getMessage(), ex);
        }
    }

    /** Mengganti data produk berdasarkan id. Mengembalikan false jika id tidak ada. */
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, stock = ?, is_active = ? WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getStock());
            ps.setBoolean(4, product.isActive());
            ps.setInt(5, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataException("Gagal mengubah produk: " + ex.getMessage(), ex);
        }
    }

    /** Menghapus permanen. Mengembalikan false jika id tidak ada. */
    public boolean delete(int id) {
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataException("Gagal menghapus produk: " + ex.getMessage(), ex);
        }
    }

    /** Mengembalikan null jika tidak ditemukan. */
    public Product findById(int id) {
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM + " FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? baca(rs) : null;
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal membaca produk: " + ex.getMessage(), ex);
        }
    }

    public List<Product> findAll() {
        return query("SELECT " + KOLOM + " FROM products ORDER BY id");
    }

    public List<Product> findActive() {
        return query("SELECT " + KOLOM + " FROM products WHERE is_active = 1 ORDER BY id");
    }

    private List<Product> query(String sql) {
        List<Product> result = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(baca(rs));
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal membaca daftar produk: " + ex.getMessage(), ex);
        }
        return result;
    }

    private static Product baca(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("name"), rs.getBigDecimal("price"),
                rs.getInt("stock"), rs.getBoolean("is_active"));
    }
}
