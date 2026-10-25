package model;

import java.math.BigDecimal;

/**
 * Satu baris barang pada sebuah transaksi. Class ini IMMUTABLE (tidak punya
 * setter) dan menyimpan SNAPSHOT nama produk, harga satuan, jumlah, dan subtotal
 * pada saat transaksi terjadi. Karena itu perubahan atau penghapusan produk
 * tidak dapat memengaruhi riwayat.
 */
public final class TransactionDetail {

    private final int id;
    private final int transactionId;
    private final int productId;
    private final String productName;
    private final BigDecimal price;
    private final int quantity;
    private final BigDecimal subtotal;

    public TransactionDetail(int id, int transactionId, int productId, String productName,
            BigDecimal price, int quantity, BigDecimal subtotal) {
        this.id = id;
        this.transactionId = transactionId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public int getTransactionId() {
        return transactionId;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
