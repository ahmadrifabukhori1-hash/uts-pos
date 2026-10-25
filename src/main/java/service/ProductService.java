package service;

import crud.ProductCRUD;
import crud.TransactionCRUD;
import java.math.BigDecimal;
import java.util.List;
import model.Product;

/** Aturan bisnis dan validasi untuk produk. */
public class ProductService {

    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999999999");
    private static final int MAX_NAME_LENGTH = 100;

    private final ProductCRUD productCRUD;
    private final TransactionCRUD transactionCRUD;

    public ProductService(ProductCRUD productCRUD, TransactionCRUD transactionCRUD) {
        this.productCRUD = productCRUD;
        this.transactionCRUD = transactionCRUD;
    }

    public List<Product> getAllProducts() {
        return productCRUD.findAll();
    }

    public List<Product> getActiveProducts() {
        return productCRUD.findActive();
    }

    /** Mengembalikan null jika tidak ada. */
    public Product getProduct(int id) {
        return productCRUD.findById(id);
    }

    public Product addProduct(String name, BigDecimal price, int stock) {
        String cleanName = validate(null, name, price, stock);
        return productCRUD.insert(new Product(0, cleanName, price, stock, true));
    }

    public Product updateProduct(int id, String name, BigDecimal price, int stock) {
        Product existing = requireProduct(id);
        String cleanName = validate(id, name, price, stock);
        existing.setName(cleanName);
        existing.setPrice(price);
        existing.setStock(stock);
        productCRUD.update(existing);
        return existing;
    }

    public void deactivateProduct(int id) {
        Product p = requireProduct(id);
        if (!p.isActive()) {
            throw new ValidationException("Produk \"" + p.getName() + "\" sudah nonaktif.");
        }
        p.setActive(false);
        productCRUD.update(p);
    }

    public void activateProduct(int id) {
        Product p = requireProduct(id);
        if (p.isActive()) {
            throw new ValidationException("Produk \"" + p.getName() + "\" sudah aktif.");
        }
        for (Product other : productCRUD.findActive()) {
            if (other.getId() != id && other.getName().equalsIgnoreCase(p.getName())) {
                throw new ValidationException("Tidak bisa diaktifkan: sudah ada produk aktif bernama \""
                        + p.getName() + "\". Ubah nama salah satunya terlebih dahulu.");
            }
        }
        p.setActive(true);
        productCRUD.update(p);
    }

    /** Apakah produk pernah terjual (muncul di riwayat transaksi). */
    public boolean isProductSold(int id) {
        return transactionCRUD.isProductUsed(id);
    }

    /**
     * Menghapus permanen produk yang BELUM pernah terjual. Produk yang sudah
     * pernah terjual tidak dihapus; gunakan deactivateProduct.
     */
    public void deleteProduct(int id) {
        Product p = requireProduct(id);
        if (isProductSold(id)) {
            throw new ValidationException("Produk \"" + p.getName() + "\" sudah pernah terjual dan tidak dapat "
                    + "dihapus agar riwayat transaksi tetap utuh. Gunakan tombol Nonaktifkan.");
        }
        productCRUD.delete(id);
    }

    /** Mengurangi stok. Dipanggil oleh TransactionService setelah semua validasi lolos. */
    public void reduceStock(int id, int quantity) {
        Product p = requireProduct(id);
        if (quantity <= 0) {
            throw new ValidationException("Jumlah pengurangan stok harus lebih dari 0.");
        }
        if (quantity > p.getStock()) {
            throw new ValidationException("Stok \"" + p.getName() + "\" tidak cukup. Tersedia: " + p.getStock() + ".");
        }
        p.setStock(p.getStock() - quantity);
        productCRUD.update(p);
    }

    private Product requireProduct(int id) {
        Product p = productCRUD.findById(id);
        if (p == null) {
            throw new ValidationException("Produk tidak ditemukan.");
        }
        return p;
    }

    /** Memeriksa data produk dan mengembalikan nama yang sudah dirapikan. */
    private String validate(Integer ownId, String name, BigDecimal price, int stock) {
        String cleanName = name == null ? "" : name.trim();
        if (cleanName.isEmpty()) {
            throw new ValidationException("Nama produk tidak boleh kosong.");
        }
        if (cleanName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Nama produk maksimal " + MAX_NAME_LENGTH + " karakter.");
        }
        if (price == null || price.signum() <= 0) {
            throw new ValidationException("Harga harus lebih dari 0.");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            throw new ValidationException("Harga terlalu besar.");
        }
        if (stock < 0) {
            throw new ValidationException("Stok tidak boleh negatif.");
        }
        // Nama harus unik di antara produk AKTIF; produk nonaktif tidak memblokir nama yang sama.
        for (Product other : productCRUD.findActive()) {
            boolean sameProduct = ownId != null && other.getId() == ownId;
            if (!sameProduct && other.getName().equalsIgnoreCase(cleanName)) {
                throw new ValidationException("Produk dengan nama \"" + cleanName + "\" sudah ada.");
            }
        }
        return cleanName;
    }
}
