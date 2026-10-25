package view;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.CartItem;
import model.Product;
import service.AppData;
import service.CartService;
import service.ProductService;
import service.ValidationException;
import util.CurrencyUtil;
import util.InputUtil;
import util.Pesan;
import util.TableUtil;

/**
 * Form kasir: memilih produk, mengisi keranjang, lalu checkout.
 * Seluruh aturan (stok, jumlah, total) ada di CartService dan TransactionService.
 */
public class KasirForm extends javax.swing.JFrame {

    private final ProductService productService = AppData.getInstance().getProductService();
    private final CartService cartService = AppData.getInstance().getCartService();
    private boolean memuat = false;

    public KasirForm() {
        initComponents();
        tblProduk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                cariProduk();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                cariProduk();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                cariProduk();
            }
        });
        tblKeranjang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKeranjang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !memuat) {
                isiJumlahBaruDariPilihan();
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

    /** Memuat ulang daftar produk dan keranjang dari service. */
    public void refreshData() {
        memuat = true;
        try {
            muatProduk();
            muatKeranjang();
        } finally {
            memuat = false;
        }
    }

    private void muatProduk() {
        Integer terpilih = TableUtil.getSelectedId(tblProduk);
        DefaultTableModel model = (DefaultTableModel) tblProduk.getModel();
        model.setRowCount(0);
        String kata = txtCari.getText().trim().toLowerCase();
        for (Product p : productService.getActiveProducts()) {
            if (!kata.isEmpty() && !p.getName().toLowerCase().contains(kata)
                    && !String.valueOf(p.getId()).equals(kata)) {
                continue;
            }
            model.addRow(new Object[]{p.getId(), p.getName(), CurrencyUtil.format(p.getPrice()), p.getStock()});
        }
        TableUtil.selectById(tblProduk, terpilih);
    }

    /** Dipanggil setiap isi kolom pencarian berubah: memfilter daftar produk (nama atau ID). */
    private void cariProduk() {
        if (!memuat) {
            muatProduk();
        }
    }

    private void muatKeranjang() {
        Integer terpilih = TableUtil.getSelectedId(tblKeranjang);
        DefaultTableModel model = (DefaultTableModel) tblKeranjang.getModel();
        model.setRowCount(0);
        for (CartItem item : cartService.getItems()) {
            model.addRow(new Object[]{item.getProductId(), item.getProductName(),
                CurrencyUtil.format(item.getPrice()), item.getQuantity(),
                CurrencyUtil.format(item.getSubtotal())});
        }
        txtTotal.setText(CurrencyUtil.format(cartService.getTotal()));
        TableUtil.selectById(tblKeranjang, terpilih);
        if (tblKeranjang.getSelectedRow() < 0) {
            txtJumlahBaru.setText("");
        }
    }

    private void isiJumlahBaruDariPilihan() {
        int row = tblKeranjang.getSelectedRow();
        if (row >= 0) {
            txtJumlahBaru.setText(String.valueOf(tblKeranjang.getValueAt(row, 3)));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblProduk = new javax.swing.JLabel();
        lblCari = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        scrollProduk = new javax.swing.JScrollPane();
        tblProduk = new javax.swing.JTable();
        lblJumlah = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        lblKeranjang = new javax.swing.JLabel();
        scrollKeranjang = new javax.swing.JScrollPane();
        tblKeranjang = new javax.swing.JTable();
        lblJumlahBaru = new javax.swing.JLabel();
        txtJumlahBaru = new javax.swing.JTextField();
        btnUbahJumlah = new javax.swing.JButton();
        btnHapusItem = new javax.swing.JButton();
        btnKosongkan = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnCheckout = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Kasir / Transaksi");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        lblProduk.setText("Daftar Produk Aktif");

        lblCari.setText("Cari produk:");

        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama Produk", "Harga", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollProduk.setViewportView(tblProduk);

        lblJumlah.setText("Jumlah:");

        txtJumlah.setText("1");

        btnTambah.setText("Tambah ke Keranjang");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        lblKeranjang.setText("Keranjang Belanja");

        tblKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama Produk", "Harga", "Jumlah", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollKeranjang.setViewportView(tblKeranjang);

        lblJumlahBaru.setText("Jumlah baru:");


        btnUbahJumlah.setText("Ubah Jumlah");
        btnUbahJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahJumlahActionPerformed(evt);
            }
        });

        btnHapusItem.setText("Hapus dari Keranjang");
        btnHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusItemActionPerformed(evt);
            }
        });

        btnKosongkan.setText("Kosongkan Keranjang");
        btnKosongkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongkanActionPerformed(evt);
            }
        });

        lblTotal.setText("Total Belanja:");

        txtTotal.setText("Rp0");
        txtTotal.setEditable(false);

        btnCheckout.setText("Checkout");
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblProduk)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblCari)
                    .addGap(6, 6, 6)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(scrollProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblJumlah)
                    .addGap(6, 6, 6)
                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(btnTambah)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblKeranjang)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(scrollKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblJumlahBaru)
                    .addGap(6, 6, 6)
                    .addComponent(txtJumlahBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(btnUbahJumlah)
                    .addGap(6, 6, 6)
                    .addComponent(btnHapusItem)
                    .addGap(6, 6, 6)
                    .addComponent(btnKosongkan)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblTotal)
                    .addGap(6, 6, 6)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(btnCheckout)
                    .addGap(6, 6, 6)
                    .addComponent(btnTutup)
                    .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblProduk)
                        .addComponent(lblCari)
                        .addComponent(txtCari))
                    .addGap(8, 8, 8)
                    .addComponent(scrollProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblJumlah)
                        .addComponent(txtJumlah)
                        .addComponent(btnTambah))
                    .addGap(8, 8, 8)
                    .addComponent(lblKeranjang)
                    .addGap(8, 8, 8)
                    .addComponent(scrollKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblJumlahBaru)
                        .addComponent(txtJumlahBaru)
                        .addComponent(btnUbahJumlah)
                        .addComponent(btnHapusItem)
                        .addComponent(btnKosongkan))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotal)
                        .addComponent(txtTotal)
                        .addComponent(btnCheckout)
                        .addComponent(btnTutup))
                    .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        Integer productId = TableUtil.getSelectedId(tblProduk);
        if (productId == null) {
            Pesan.error(this, "Pilih produk dari daftar terlebih dahulu.");
            return;
        }
        try {
            int jumlah = InputUtil.parseInt(txtJumlah.getText(), "Jumlah");
            cartService.addItem(productId, jumlah);
            muatKeranjang();
            txtJumlah.setText("1");
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahJumlahActionPerformed
        Integer productId = TableUtil.getSelectedId(tblKeranjang);
        if (productId == null) {
            Pesan.error(this, "Pilih barang di keranjang yang jumlahnya akan diubah.");
            return;
        }
        try {
            int jumlah = InputUtil.parseInt(txtJumlahBaru.getText(), "Jumlah baru");
            cartService.updateQuantity(productId, jumlah);
            muatKeranjang();
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnUbahJumlahActionPerformed

    private void btnHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusItemActionPerformed
        Integer productId = TableUtil.getSelectedId(tblKeranjang);
        if (productId == null) {
            Pesan.error(this, "Pilih barang di keranjang yang akan dihapus.");
            return;
        }
        try {
            cartService.removeItem(productId);
            muatKeranjang();
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnHapusItemActionPerformed

    private void btnKosongkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKosongkanActionPerformed
        if (cartService.isEmpty()) {
            Pesan.info(this, "Keranjang sudah kosong.");
            return;
        }
        if (Pesan.confirm(this, "Kosongkan seluruh isi keranjang?")) {
            cartService.clear();
            muatKeranjang();
        }
    }//GEN-LAST:event_btnKosongkanActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        if (cartService.isEmpty()) {
            Pesan.error(this, "Keranjang masih kosong. Tambahkan barang terlebih dahulu.");
            return;
        }
        CheckoutForm dialog = new CheckoutForm(this, true);
        dialog.tampilkan();
        refreshData();
    }//GEN-LAST:event_btnCheckoutActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnTutupActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refreshData();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnHapusItem;
    private javax.swing.JButton btnKosongkan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnUbahJumlah;
    private javax.swing.JLabel lblCari;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblJumlahBaru;
    private javax.swing.JLabel lblKeranjang;
    private javax.swing.JLabel lblProduk;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JScrollPane scrollKeranjang;
    private javax.swing.JScrollPane scrollProduk;
    private javax.swing.JTable tblKeranjang;
    private javax.swing.JTable tblProduk;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtJumlahBaru;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
