package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;

/** Keranjang belanja yang sedang berjalan, beserta validasi jumlah dan stok. */
public class CartService {

    private final ProductService productService;
    private final List<CartItem> items = new ArrayList<>();

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    /** Menambah barang. Jika produk sudah ada di keranjang, jumlahnya ditambah. */
    public void addItem(int productId, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Jumlah harus lebih dari 0.");
        }
        Product product = productService.getProduct(productId);
        if (product == null) {
            throw new ValidationException("Produk tidak ditemukan.");
        }
        if (!product.isActive()) {
            throw new ValidationException("Produk \"" + product.getName() + "\" tidak aktif dan tidak bisa dijual.");
        }
        CartItem existing = find(productId);
        int inCart = existing == null ? 0 : existing.getQuantity();
        if (inCart + quantity > product.getStock()) {
            throw new ValidationException("Stok \"" + product.getName() + "\" tidak cukup. Tersedia: "
                    + product.getStock() + ", sudah di keranjang: " + inCart + ".");
        }
        if (existing == null) {
            items.add(new CartItem(product.getId(), product.getName(), product.getPrice(), quantity));
        } else {
            existing.setQuantity(inCart + quantity);
        }
    }

    /** Mengganti jumlah barang di keranjang menjadi nilai baru. */
    public void updateQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Jumlah harus lebih dari 0. Untuk membuang barang, gunakan Hapus dari Keranjang.");
        }
        CartItem existing = find(productId);
        if (existing == null) {
            throw new ValidationException("Barang tidak ada di keranjang.");
        }
        Product product = productService.getProduct(productId);
        if (product == null) {
            throw new ValidationException("Produk tidak ditemukan.");
        }
        if (quantity > product.getStock()) {
            throw new ValidationException("Stok \"" + product.getName() + "\" tidak cukup. Tersedia: "
                    + product.getStock() + ".");
        }
        existing.setQuantity(quantity);
    }

    public void removeItem(int productId) {
        CartItem existing = find(productId);
        if (existing == null) {
            throw new ValidationException("Barang tidak ada di keranjang.");
        }
        items.remove(existing);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Salinan isi keranjang; mengubah salinan tidak memengaruhi keranjang. */
    public List<CartItem> getItems() {
        List<CartItem> copy = new ArrayList<>();
        for (CartItem item : items) {
            copy.add(new CartItem(item));
        }
        return copy;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    private CartItem find(int productId) {
        for (CartItem item : items) {
            if (item.getProductId() == productId) {
                return item;
            }
        }
        return null;
    }
}
