package view;

import java.math.BigDecimal;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.Product;
import service.AppData;
import service.ProductService;
import service.ValidationException;
import util.CurrencyUtil;
import util.InputUtil;
import util.Pesan;
import util.TableUtil;

/**
 * Form manajemen produk: tambah, ubah, nonaktifkan/aktifkan, dan hapus
 * (hanya untuk produk yang belum pernah terjual).
 */
public class ProductForm extends javax.swing.JFrame {

    private final ProductService productService = AppData.getInstance().getProductService();
    private boolean memuat = false;

    public ProductForm() {
        initComponents();
        tblProduk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProduk.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !memuat) {
                isiFormDariPilihan();
            }
        });
        refreshData();
    }

    /** Menampilkan form dengan data terbaru. */
    public void tampilkan() {
        refreshData();
        setVisible(true);
        setState(java.awt.Frame.NORMAL);
        toFront();
    }

    /** Memuat ulang tabel; baris yang dipilih tetap dipilih jika masih ada. */
    public void refreshData() {
        Integer terpilih = TableUtil.getSelectedId(tblProduk);
        memuat = true;
        try {
            DefaultTableModel model = (DefaultTableModel) tblProduk.getModel();
            model.setRowCount(0);
            boolean semua = chkNonaktif.isSelected();
            for (Product p : productService.getAllProducts()) {
                if (!semua && !p.isActive()) {
                    continue;
                }
                model.addRow(new Object[]{p.getId(), p.getName(), CurrencyUtil.format(p.getPrice()),
                    p.getStock(), p.isActive() ? "Aktif" : "Nonaktif"});
            }
            TableUtil.selectById(tblProduk, terpilih);
        } finally {
            memuat = false;
        }
    }

    private void isiFormDariPilihan() {
        Integer id = TableUtil.getSelectedId(tblProduk);
        if (id == null) {
            return;
        }
        Product p = productService.getProduct(id);
        if (p == null) {
            return;
        }
        txtNama.setText(p.getName());
        txtHarga.setText(p.getPrice().stripTrailingZeros().toPlainString());
        txtStok.setText(String.valueOf(p.getStock()));
    }

    private void bersihkanForm() {
        memuat = true;
        try {
            tblProduk.clearSelection();
        } finally {
            memuat = false;
        }
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtNama.requestFocusInWindow();
    }

    /** Id produk terpilih, atau menampilkan pesan dan mengembalikan null. */
    private Integer wajibPilihProduk(String pesan) {
        Integer id = TableUtil.getSelectedId(tblProduk);
        if (id == null) {
            Pesan.error(this, pesan);
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chkNonaktif = new javax.swing.JCheckBox();
        scrollProduk = new javax.swing.JScrollPane();
        tblProduk = new javax.swing.JTable();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblHarga = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        lblStok = new javax.swing.JLabel();
        txtStok = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnNonaktifkan = new javax.swing.JButton();
        btnAktifkan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBersihkan = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        lblCatatan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Manajemen Produk");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        chkNonaktif.setText("Tampilkan produk nonaktif");
        chkNonaktif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNonaktifActionPerformed(evt);
            }
        });

        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama Produk", "Harga", "Stok", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollProduk.setViewportView(tblProduk);

        lblNama.setText("Nama:");


        lblHarga.setText("Harga:");


        lblStok.setText("Stok:");


        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Simpan Perubahan");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnNonaktifkan.setText("Nonaktifkan");
        btnNonaktifkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNonaktifkanActionPerformed(evt);
            }
        });

        btnAktifkan.setText("Aktifkan");
        btnAktifkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAktifkanActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBersihkan.setText("Bersihkan Form");
        btnBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBersihkanActionPerformed(evt);
            }
        });

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        lblCatatan.setText("Pilih produk di tabel untuk mengubah. Produk yang sudah pernah terjual hanya bisa dinonaktifkan.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(chkNonaktif)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(scrollProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblNama, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblStok, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtStok, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(btnTambah)
                    .addGap(6, 6, 6)
                    .addComponent(btnUbah)
                    .addGap(6, 6, 6)
                    .addComponent(btnNonaktifkan)
                    .addGap(6, 6, 6)
                    .addComponent(btnAktifkan)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(btnHapus)
                    .addGap(6, 6, 6)
                    .addComponent(btnBersihkan)
                    .addGap(6, 6, 6)
                    .addComponent(btnTutup)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblCatatan)
                    .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(chkNonaktif)
                    .addGap(8, 8, 8)
                    .addComponent(scrollProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNama)
                        .addComponent(txtNama))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblHarga)
                        .addComponent(txtHarga))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblStok)
                        .addComponent(txtStok))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnUbah)
                        .addComponent(btnNonaktifkan)
                        .addComponent(btnAktifkan))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnHapus)
                        .addComponent(btnBersihkan)
                        .addComponent(btnTutup))
                    .addGap(8, 8, 8)
                    .addComponent(lblCatatan)
                    .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        try {
            BigDecimal harga = InputUtil.parseMoney(txtHarga.getText(), "Harga");
            int stok = InputUtil.parseInt(txtStok.getText(), "Stok");
            Product p = productService.addProduct(txtNama.getText(), harga, stok);
            bersihkanForm();
            refreshData();
            Pesan.info(this, "Produk \"" + p.getName() + "\" berhasil ditambahkan.");
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        Integer id = wajibPilihProduk("Pilih produk di tabel yang akan diubah.");
        if (id == null) {
            return;
        }
        try {
            BigDecimal harga = InputUtil.parseMoney(txtHarga.getText(), "Harga");
            int stok = InputUtil.parseInt(txtStok.getText(), "Stok");
            Product p = productService.updateProduct(id, txtNama.getText(), harga, stok);
            refreshData();
            isiFormDariPilihan();
            Pesan.info(this, "Produk \"" + p.getName() + "\" berhasil diubah.");
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnNonaktifkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonaktifkanActionPerformed
        Integer id = wajibPilihProduk("Pilih produk di tabel yang akan dinonaktifkan.");
        if (id == null) {
            return;
        }
        try {
            productService.deactivateProduct(id);
            refreshData();
            Pesan.info(this, "Produk dinonaktifkan. Produk ini tidak bisa dipakai untuk transaksi baru.");
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnNonaktifkanActionPerformed

    private void btnAktifkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAktifkanActionPerformed
        Integer id = wajibPilihProduk("Pilih produk di tabel yang akan diaktifkan.");
        if (id == null) {
            return;
        }
        try {
            productService.activateProduct(id);
            refreshData();
            Pesan.info(this, "Produk diaktifkan kembali.");
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnAktifkanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        Integer id = wajibPilihProduk("Pilih produk di tabel yang akan dihapus.");
        if (id == null) {
            return;
        }
        try {
            Product p = productService.getProduct(id);
            if (p == null) {
                throw new ValidationException("Produk tidak ditemukan.");
            }
            if (productService.isProductSold(id)) {
                if (!p.isActive()) {
                    Pesan.info(this, "Produk \"" + p.getName() + "\" sudah pernah terjual sehingga tidak dapat dihapus, "
                            + "dan sudah dalam keadaan nonaktif.");
                } else if (Pesan.confirm(this, "Produk \"" + p.getName() + "\" sudah pernah terjual dan tidak dapat "
                        + "dihapus agar riwayat transaksi tetap utuh.\nNonaktifkan produk ini saja?")) {
                    productService.deactivateProduct(id);
                    refreshData();
                }
                return;
            }
            if (Pesan.confirm(this, "Hapus produk \"" + p.getName() + "\" secara permanen?")) {
                productService.deleteProduct(id);
                bersihkanForm();
                refreshData();
            }
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void chkNonaktifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonaktifActionPerformed
        refreshData();
    }//GEN-LAST:event_chkNonaktifActionPerformed

    private void btnBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBersihkanActionPerformed
        bersihkanForm();
    }//GEN-LAST:event_btnBersihkanActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnTutupActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refreshData();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAktifkan;
    private javax.swing.JButton btnBersihkan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnNonaktifkan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnUbah;
    private javax.swing.JCheckBox chkNonaktif;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblStok;
    private javax.swing.JScrollPane scrollProduk;
    private javax.swing.JTable tblProduk;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
