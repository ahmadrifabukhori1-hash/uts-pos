package view;

import java.math.BigDecimal;
import model.Transaction;
import service.AppData;
import service.CartService;
import service.TransactionService;
import service.ValidationException;
import util.CurrencyUtil;
import util.InputUtil;
import util.Pesan;

/**
 * Dialog checkout: nama pembeli, total, uang tunai, dan kembalian.
 * Transaksi diselesaikan oleh TransactionService.checkout().
 */
public class CheckoutForm extends javax.swing.JDialog {

    private final CartService cartService = AppData.getInstance().getCartService();
    private final TransactionService transactionService = AppData.getInstance().getTransactionService();

    public CheckoutForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /** Mengosongkan isian, menampilkan total terbaru, lalu membuka dialog. */
    public void tampilkan() {
        txtNama.setText("");
        txtTunai.setText("");
        txtKembalian.setText("");
        txtTotal.setText(CurrencyUtil.format(cartService.getTotal()));
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblCatatan = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        lblTunai = new javax.swing.JLabel();
        txtTunai = new javax.swing.JTextField();
        lblKembalian = new javax.swing.JLabel();
        txtKembalian = new javax.swing.JTextField();
        btnHitung = new javax.swing.JButton();
        btnSelesai = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Checkout");

        lblNama.setText("Nama Pembeli:");


        lblCatatan.setText("Kosongkan nama pembeli untuk memakai nama \"Umum\".");

        lblTotal.setText("Total Belanja:");

        txtTotal.setEditable(false);

        lblTunai.setText("Uang Tunai:");


        lblKembalian.setText("Kembalian:");

        txtKembalian.setEditable(false);

        btnHitung.setText("Hitung Kembalian");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        btnSelesai.setText("Selesaikan Transaksi");
        btnSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelesaiActionPerformed(evt);
            }
        });

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblNama, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblCatatan)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblTunai, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtTunai, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(lblKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnHitung)
                    .addGap(6, 6, 6)
                    .addComponent(btnSelesai)
                    .addGap(6, 6, 6)
                    .addComponent(btnBatal)
                    .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNama)
                        .addComponent(txtNama))
                    .addGap(8, 8, 8)
                    .addComponent(lblCatatan)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotal)
                        .addComponent(txtTotal))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTunai)
                        .addComponent(txtTunai))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblKembalian)
                        .addComponent(txtKembalian))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnHitung)
                        .addComponent(btnSelesai)
                        .addComponent(btnBatal))
                    .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        try {
            BigDecimal tunai = InputUtil.parseMoney(txtTunai.getText(), "Uang tunai");
            BigDecimal kembalian = transactionService.calculateChange(cartService.getTotal(), tunai);
            txtKembalian.setText(CurrencyUtil.format(kembalian));
        } catch (ValidationException ex) {
            txtKembalian.setText("");
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelesaiActionPerformed
        try {
            BigDecimal tunai = InputUtil.parseMoney(txtTunai.getText(), "Uang tunai");
            Transaction trx = transactionService.checkout(cartService, txtNama.getText(), tunai);
            txtKembalian.setText(CurrencyUtil.format(trx.getChangeAmount()));
            Pesan.info(this, "Transaksi berhasil.\nNo. transaksi: " + trx.getNumber()
                    + "\nPembeli: " + trx.getCustomerName()
                    + "\nTotal: " + CurrencyUtil.format(trx.getTotal())
                    + "\nTunai: " + CurrencyUtil.format(trx.getCashPaid())
                    + "\nKembalian: " + CurrencyUtil.format(trx.getChangeAmount()));
            dispose();
        } catch (ValidationException ex) {
            Pesan.error(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnSelesaiActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnSelesai;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JLabel lblKembalian;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTunai;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTunai;
    // End of variables declaration//GEN-END:variables
}
