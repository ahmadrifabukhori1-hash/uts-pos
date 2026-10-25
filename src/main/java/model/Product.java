package model;

import java.math.BigDecimal;

/**
 * Data produk. Objek ini boleh diubah (nama, harga, stok, status aktif),
 * karena itu riwayat transaksi TIDAK pernah menyimpan referensi ke Product,
 * melainkan salinan nilainya (lihat TransactionDetail).
 */
public class Product {

    private int id;
    private String name;
    private BigDecimal price;
    private int stock;
    private boolean active;

    public Product(int id, String name, BigDecimal price, int stock, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.active = active;
    }

    /** Konstruktor salin. */
    public Product(Product other) {
        this(other.id, other.name, other.price, other.stock, other.active);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return name;
    }
}
