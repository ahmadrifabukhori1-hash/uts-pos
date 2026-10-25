package service;

import crud.ProductCRUD;
import crud.TransactionCRUD;

/**
 * Satu-satunya tempat data dan service dibuat. Semua form mengambil service
 * dari sini, sehingga seluruh aplikasi memakai data yang sama.
 */
public class AppData {

    private static AppData instance;

    private final ProductService productService;
    private final CartService cartService;
    private final TransactionService transactionService;

    private AppData() {
        ProductCRUD productCRUD = new ProductCRUD();
        TransactionCRUD transactionCRUD = new TransactionCRUD();
        productService = new ProductService(productCRUD, transactionCRUD);
        cartService = new CartService(productService);
        transactionService = new TransactionService(transactionCRUD, productService);
    }

    /** Data bersama untuk aplikasi (tersimpan di MySQL). Contoh produk berasal dari database-schema.sql. */
    public static synchronized AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    /**
     * Mengosongkan seluruh tabel lalu membuat objek baru; dipakai untuk pengujian.
     * Hanya berjalan pada database berakhiran "_test" (lihat DatabaseConnection).
     */
    public static AppData createEmpty() {
        config.DatabaseConnection.kosongkanUntukUji();
        return new AppData();
    }

    public ProductService getProductService() {
        return productService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }
}
