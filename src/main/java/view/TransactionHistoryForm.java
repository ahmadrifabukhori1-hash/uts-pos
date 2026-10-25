package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.Transaction;
import model.TransactionDetail;
import service.AppData;
import service.TransactionService;
import util.CurrencyUtil;
import util.DateUtil;
import util.Pesan;
import util.StrukPrinter;

/**
 * Riwayat transaksi. Semua nilai yang tampil dibaca dari objek Transaction
 * dan TransactionDetail yang tersimpan (snapshot saat transaksi), bukan dari
 * data produk yang sekarang.
 */
public class TransactionHistoryForm extends javax.swing.JFrame {

    private final TransactionService transactionService = AppData.getInstance().getTransactionService();
    private List<Transaction> daftarTampil = new ArrayList<>();
    private boolean memuat = false;

    public TransactionHistoryForm() {
        initComponents();
        tblTransaksi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !memuat) {
                tampilkanDetail();
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

    /** Memuat ulang daftar transaksi; transaksi yang dipilih tetap dipilih. */
    public void refreshData() {
        Transaction terpilih = transaksiTerpilih();
        memuat = true;
        try {
            DefaultTableModel model = (DefaultTableModel) tblTransaksi.getModel();
            model.setRowCount(0);
            daftarTampil = transactionService.getHistory();
            int barisTerpilih = -1;
            for (int i = 0; i < daftarTampil.size(); i++) {
                Transaction t = daftarTampil.get(i);
                model.addRow(new Object[]{t.getNumber(), DateUtil.format(t.getDate()), t.getCustomerName(),
                    CurrencyUtil.format(t.getTotal()), CurrencyUtil.format(t.getCashPaid()),
                    CurrencyUtil.format(t.getChangeAmount())});
                if (terpilih != null && t.getId() == terpilih.getId()) {
                    barisTerpilih = i;
                }
            }
            if (barisTerpilih >= 0) {
                tblTransaksi.setRowSelectionInterval(barisTerpilih, barisTerpilih);
            }
        } finally {
            memuat = false;
        }
        tampilkanDetail();
    }

    private Transaction transaksiTerpilih() {
        int row = tblTransaksi.getSelectedRow();
        return (row >= 0 && row < daftarTampil.size()) ? daftarTampil.get(row) : null;
    }

    private void tampilkanDetail() {
        DefaultTableModel model = (DefaultTableModel) tblDetail.getModel();
        model.setRowCount(0);
        Transaction t = transaksiTerpilih();
        if (t == null) {
            lblDetail.setText("Detail Barang (belum ada transaksi dipilih)");
            return;
        }
        lblDetail.setText("Detail Barang - " + t.getNumber() + " (" + t.getCustomerName() + ")");
        for (TransactionDetail d : t.getDetails()) {
            model.addRow(new Object[]{d.getProductName(), CurrencyUtil.format(d.getPrice()),
                d.getQuantity(), CurrencyUtil.format(d.getSubtotal())});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDaftar = new javax.swing.JLabel();
        scrollTransaksi = new javax.swing.JScrollPane();
        tblTransaksi = new javax.swing.JTable();
        lblDetail = new javax.swing.JLabel();
        scrollDetail = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        btnMuatUlang = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setTitle("Riwayat Transaksi");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        lblDaftar.setText("Daftar Transaksi (pilih satu baris untuk melihat detail barang)");

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Transaksi", "Tanggal & Waktu", "Nama Pembeli", "Total Belanja", "Uang Pembayaran", "Kembalian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTransaksi.setViewportView(tblTransaksi);

        lblDetail.setText("Detail Barang");

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Produk", "Harga Saat Transaksi", "Jumlah", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollDetail.setViewportView(tblDetail);

        btnCetak.setText("Cetak Struk");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnMuatUlang.setText("Muat Ulang");
        btnMuatUlang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuatUlangActionPerformed(evt);
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
                    .addComponent(lblDaftar)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(scrollTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblDetail)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(scrollDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(btnCetak)
                    .addGap(6, 6, 6)
                    .addComponent(btnMuatUlang)
                    .addGap(6, 6, 6)
                    .addComponent(btnTutup)
                    .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblDaftar)
                    .addGap(8, 8, 8)
                    .addComponent(scrollTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addGap(8, 8, 8)
                    .addComponent(lblDetail)
                    .addGap(8, 8, 8)
                    .addComponent(scrollDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCetak)
                        .addComponent(btnMuatUlang)
                        .addComponent(btnTutup))
                    .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        Transaction t = transaksiTerpilih();
        if (t == null) {
            Pesan.error(this, "Pilih satu transaksi di tabel terlebih dahulu untuk mencetak struknya.");
            return;
        }
        StrukPrinter.pratinjauDanCetak(this, t);
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnMuatUlangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatUlangActionPerformed
        refreshData();
    }//GEN-LAST:event_btnMuatUlangActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnTutupActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refreshData();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnMuatUlang;
    private javax.swing.JButton btnTutup;
    private javax.swing.JLabel lblDaftar;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JScrollPane scrollDetail;
    private javax.swing.JScrollPane scrollTransaksi;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblTransaksi;
    // End of variables declaration//GEN-END:variables
}
