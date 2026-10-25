package service;

import crud.TransactionCRUD;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.CartItem;
import model.Product;
import model.Transaction;
import model.TransactionDetail;

/** Proses checkout dan pembacaan riwayat transaksi. */
public class TransactionService {

    public static final String DEFAULT_CUSTOMER = "Umum";

    private final TransactionCRUD transactionCRUD;
    private final ProductService productService;

    public TransactionService(TransactionCRUD transactionCRUD, ProductService productService) {
        this.transactionCRUD = transactionCRUD;
        this.productService = productService;
    }

    /** Kembalian = uang tunai - total. Gagal jika uang kurang. */
    public BigDecimal calculateChange(BigDecimal total, BigDecimal cashPaid) {
        if (cashPaid == null) {
            throw new ValidationException("Uang tunai belum diisi.");
        }
        if (cashPaid.compareTo(total) < 0) {
            throw new ValidationException("Uang pembayaran kurang "
                    + util.CurrencyUtil.format(total.subtract(cashPaid)) + " dari total belanja.");
        }
        return cashPaid.subtract(total);
    }

    /**
     * Menyelesaikan transaksi dari isi keranjang. Semua pemeriksaan dilakukan
     * SEBELUM data disimpan, sehingga jika ada yang gagal tidak ada data
     * yang berubah. Setelah berhasil: stok dikurangi dan transaksi disimpan
     * (dengan snapshot nama dan harga) secara atomik, lalu keranjang dikosongkan.
     */
    public Transaction checkout(CartService cart, String customerName, BigDecimal cashPaid) {
        if (cart.isEmpty()) {
            throw new ValidationException("Keranjang masih kosong.");
        }
        List<CartItem> items = cart.getItems();
        BigDecimal total = cart.getTotal();
        BigDecimal change = calculateChange(total, cashPaid);

        for (CartItem item : items) {
            Product product = productService.getProduct(item.getProductId());
            if (product == null || !product.isActive()) {
                throw new ValidationException("Produk \"" + item.getProductName()
                        + "\" sudah tidak aktif. Hapus dari keranjang untuk melanjutkan.");
            }
            if (product.getPrice().compareTo(item.getPrice()) != 0) {
                throw new ValidationException("Harga \"" + item.getProductName()
                        + "\" berubah sejak dimasukkan ke keranjang. Hapus dari keranjang lalu tambahkan lagi.");
            }
            if (item.getQuantity() > product.getStock()) {
                throw new ValidationException("Stok \"" + item.getProductName() + "\" tidak cukup. Tersedia: "
                        + product.getStock() + ", diminta: " + item.getQuantity() + ".");
            }
        }

        String name = customerName == null ? "" : customerName.trim();
        if (name.isEmpty()) {
            name = DEFAULT_CUSTOMER;
        }

        List<TransactionDetail> details = new ArrayList<>();
        for (CartItem item : items) {
            details.add(new TransactionDetail(0, 0, item.getProductId(), item.getProductName(),
                    item.getPrice(), item.getQuantity(), item.getSubtotal()));
        }
        // save() mengurangi stok dan menyimpan transaksi dalam satu transaksi database.
        Transaction saved = transactionCRUD.save(new Transaction(0, LocalDateTime.now(), name,
                total, cashPaid, change, details));
        cart.clear();
        return saved;
    }

    /** Riwayat dengan transaksi terbaru di urutan pertama. */
    public List<Transaction> getHistory() {
        List<Transaction> all = transactionCRUD.findAll();
        Collections.reverse(all);
        return all;
    }

    /** Mengembalikan null jika tidak ditemukan. */
    public Transaction getTransaction(int id) {
        return transactionCRUD.findById(id);
    }
}
