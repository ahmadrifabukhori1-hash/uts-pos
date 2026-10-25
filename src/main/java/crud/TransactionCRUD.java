package crud;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Transaction;
import model.TransactionDetail;

/**
 * Penyimpanan transaksi di MySQL (tabel transactions dan transaction_details).
 * Transaksi yang tersimpan tidak pernah diubah (tidak ada method update).
 */
public class TransactionCRUD {

    /**
     * Menyimpan transaksi dan MENGURANGI STOK produk dalam satu transaksi
     * database: jika salah satu langkah gagal, semuanya dibatalkan (rollback),
     * sehingga tidak mungkin ada transaksi tersimpan tanpa stok berkurang (atau sebaliknya).
     * Id transaksi dan id detail dibuat oleh database.
     */
    public Transaction save(Transaction draft) {
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                Transaction saved = simpan(c, draft);
                c.commit();
                return saved;
            } catch (SQLException | RuntimeException ex) {
                c.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal menyimpan transaksi: " + ex.getMessage(), ex);
        }
    }

    private Transaction simpan(Connection c, Transaction draft) throws SQLException {
        int transactionId;
        String sqlHeader = "INSERT INTO transactions (transaction_date, customer_name, total, cash_paid, "
                + "change_amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(draft.getDate()));
            ps.setString(2, draft.getCustomerName());
            ps.setBigDecimal(3, draft.getTotal());
            ps.setBigDecimal(4, draft.getCashPaid());
            ps.setBigDecimal(5, draft.getChangeAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new DataException("Database tidak mengembalikan id transaksi baru.");
                }
                transactionId = keys.getInt(1);
            }
        }

        List<TransactionDetail> savedDetails = new ArrayList<>();
        String sqlDetail = "INSERT INTO transaction_details (transaction_id, product_id, product_name, price, "
                + "quantity, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlStok = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        for (TransactionDetail d : draft.getDetails()) {
            // Pengurangan stok bersyarat: gagal (0 baris) jika stok sudah tidak cukup.
            try (PreparedStatement ps = c.prepareStatement(sqlStok)) {
                ps.setInt(1, d.getQuantity());
                ps.setInt(2, d.getProductId());
                ps.setInt(3, d.getQuantity());
                if (ps.executeUpdate() == 0) {
                    throw new DataException("Stok \"" + d.getProductName() + "\" tidak cukup saat transaksi disimpan. "
                            + "Tidak ada data yang berubah.");
                }
            }
            try (PreparedStatement ps = c.prepareStatement(sqlDetail, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, transactionId);
                ps.setInt(2, d.getProductId());
                ps.setString(3, d.getProductName());
                ps.setBigDecimal(4, d.getPrice());
                ps.setInt(5, d.getQuantity());
                ps.setBigDecimal(6, d.getSubtotal());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    savedDetails.add(new TransactionDetail(keys.getInt(1), transactionId, d.getProductId(),
                            d.getProductName(), d.getPrice(), d.getQuantity(), d.getSubtotal()));
                }
            }
        }
        return new Transaction(transactionId, draft.getDate(), draft.getCustomerName(), draft.getTotal(),
                draft.getCashPaid(), draft.getChangeAmount(), savedDetails);
    }

    /** Semua transaksi, urutan dari yang paling lama ke paling baru. */
    public List<Transaction> findAll() {
        try (Connection c = DatabaseConnection.getConnection()) {
            Map<Integer, List<TransactionDetail>> detailPerTransaksi = new LinkedHashMap<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM_DETAIL
                    + " FROM transaction_details ORDER BY transaction_id, id");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TransactionDetail d = bacaDetail(rs);
                    detailPerTransaksi.computeIfAbsent(d.getTransactionId(), k -> new ArrayList<>()).add(d);
                }
            }
            List<Transaction> result = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM_HEADER
                    + " FROM transactions ORDER BY id");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    result.add(bacaHeader(rs, detailPerTransaksi.getOrDefault(id, List.of())));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Gagal membaca riwayat transaksi: " + ex.getMessage(), ex);
        }
    }

    /** Mengembalikan null jika tidak ditemukan. */
    public Transaction findById(int id) {
        try (Connection c = DatabaseConnection.getConnection()) {
            List<TransactionDetail> details = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM_DETAIL
                    + " FROM transaction_details WHERE transaction_id = ? ORDER BY id")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        details.add(bacaDetail(rs));
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT " + KOLOM_HEADER
                    + " FROM transactions WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? bacaHeader(rs, details) : null;
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal membaca transaksi: " + ex.getMessage(), ex);
        }
    }

    /** Apakah produk ini pernah muncul di detail transaksi mana pun. */
    public boolean isProductUsed(int productId) {
        try (Connection c = DatabaseConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(
                        "SELECT 1 FROM transaction_details WHERE product_id = ? LIMIT 1")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DataException("Gagal memeriksa riwayat produk: " + ex.getMessage(), ex);
        }
    }

    private static final String KOLOM_HEADER =
            "id, transaction_date, customer_name, total, cash_paid, change_amount";
    private static final String KOLOM_DETAIL =
            "id, transaction_id, product_id, product_name, price, quantity, subtotal";

    private static Transaction bacaHeader(ResultSet rs, List<TransactionDetail> details) throws SQLException {
        return new Transaction(rs.getInt("id"), rs.getTimestamp("transaction_date").toLocalDateTime(),
                rs.getString("customer_name"), rs.getBigDecimal("total"), rs.getBigDecimal("cash_paid"),
                rs.getBigDecimal("change_amount"), details);
    }

    /** product_id NULL (produk dihapus permanen) terbaca sebagai 0. */
    private static TransactionDetail bacaDetail(ResultSet rs) throws SQLException {
        return new TransactionDetail(rs.getInt("id"), rs.getInt("transaction_id"), rs.getInt("product_id"),
                rs.getString("product_name"), rs.getBigDecimal("price"), rs.getInt("quantity"),
                rs.getBigDecimal("subtotal"));
    }
}
