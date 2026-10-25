

CREATE DATABASE IF NOT EXISTS db_uts_pos
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_uts_pos;

-- Produk. is_active = 0 berarti dinonaktifkan (soft delete).
-- Nama produk harus unik hanya di antara produk AKTIF (kolom active_name bernilai
-- NULL untuk produk nonaktif, dan UNIQUE mengabaikan NULL), sehingga produk lama
-- yang dinonaktifkan tidak menghalangi pembuatan produk baru dengan nama yang sama.
CREATE TABLE IF NOT EXISTS products (
    id         INT            NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100)   NOT NULL,
    price      DECIMAL(15,2)  NOT NULL,
    stock      INT            NOT NULL DEFAULT 0,
    is_active  TINYINT(1)     NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    active_name VARCHAR(100) GENERATED ALWAYS AS (CASE WHEN is_active = 1 THEN name ELSE NULL END) STORED,
    UNIQUE KEY uq_products_active_name (active_name),
    CONSTRAINT chk_products_price CHECK (price > 0),
    CONSTRAINT chk_products_stock CHECK (stock >= 0)
) ENGINE=InnoDB;

-- Transaksi (header).
CREATE TABLE IF NOT EXISTS transactions (
    id                INT            NOT NULL AUTO_INCREMENT,
    transaction_date  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_name     VARCHAR(100)   NOT NULL DEFAULT 'Umum',
    total             DECIMAL(15,2)  NOT NULL,
    cash_paid         DECIMAL(15,2)  NOT NULL,
    change_amount     DECIMAL(15,2)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_trx_paid CHECK (cash_paid >= total)
) ENGINE=InnoDB;

-- Detail transaksi.
-- product_name dan price adalah SNAPSHOT saat transaksi; jangan pernah
-- membaca nama/harga dari tabel products untuk menampilkan riwayat.
-- product_id boleh NULL: jika suatu produk dihapus permanen dari tabel
-- products, baris detail tetap ada (ON DELETE SET NULL) dan riwayat utuh.
CREATE TABLE IF NOT EXISTS transaction_details (
    id              INT            NOT NULL AUTO_INCREMENT,
    transaction_id  INT            NOT NULL,
    product_id      INT            NULL,
    product_name    VARCHAR(100)   NOT NULL,
    price           DECIMAL(15,2)  NOT NULL,
    quantity        INT            NOT NULL,
    subtotal        DECIMAL(15,2)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_details_transaction (transaction_id),
    KEY idx_details_product (product_id),
    CONSTRAINT fk_details_transaction FOREIGN KEY (transaction_id)
        REFERENCES transactions (id) ON DELETE CASCADE,
    CONSTRAINT fk_details_product FOREIGN KEY (product_id)
        REFERENCES products (id) ON DELETE SET NULL,
    CONSTRAINT chk_details_qty CHECK (quantity > 0)
) ENGINE=InnoDB;


INSERT IGNORE INTO products (name, price, stock, is_active) VALUES
   ('Kopi Hitam', 5000, 50, 1),
    ('Kopi Susu', 7000, 40, 1),
    ('Kopi Susu Gula Aren', 10000, 30, 1),
    ('Cappuccino', 10000, 25, 1),
    ('Mocca', 9000, 25, 1),
    ('Teh Manis', 4000, 40, 1),
    ('Teh Lemon', 6000, 30, 1),
    ('Teh Susu', 7000, 30, 1),
    ('Jeruk Peras', 7000, 30, 1),
    ('Es Jeruk', 6000, 40, 1),
    ('Air Mineral', 3000, 100, 1),
    ('Es Coklat', 8000, 30, 1),
    ('Es Milo', 8000, 30, 1),
    ('Es Susu', 7000, 30, 1),
    ('Soda Gembira', 10000, 20, 1),
    ('Indomie Goreng', 7000, 40, 1),
    ('Indomie Rebus', 7000, 40, 1),
    ('Indomie Telur', 10000, 35, 1),
    ('Nasi Goreng', 12000, 25, 1),
    ('Nasi Telur', 10000, 30, 1),
    ('Roti Bakar Coklat', 8000, 25, 1),
    ('Roti Bakar Keju', 9000, 25, 1),
    ('Roti Bakar Coklat Keju', 10000, 25, 1),
    ('Pisang Goreng', 7000, 30, 1),
    ('Pisang Coklat', 8000, 30, 1),
    ('Kentang Goreng', 9000, 25, 1),
    ('Sosis Goreng', 8000, 25, 1),
    ('Otak-Otak', 8000, 25, 1),
    ('Telur Ceplok', 4000, 30, 1),
    ('Telur Dadar', 5000, 30, 1),
    ('Nasi Putih', 4000, 40, 1);