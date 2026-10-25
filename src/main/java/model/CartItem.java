package model;

import java.math.BigDecimal;

/**
 * Satu baris di keranjang belanja. Nama dan harga disalin dari produk saat
 * barang dimasukkan ke keranjang, sehingga tampilan keranjang dan total
 * konsisten walaupun produk diubah.
 */
public class CartItem {

    private final int productId;
    private final String productName;
    private final BigDecimal price;
    private int quantity;

    public CartItem(int productId, String productName, BigDecimal price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    /** Konstruktor salin. */
    public CartItem(CartItem other) {
        this(other.productId, other.productName, other.price, other.quantity);
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
