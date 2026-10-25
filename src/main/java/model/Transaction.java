package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Transaksi yang sudah selesai. Class ini IMMUTABLE: semua nilai (termasuk
 * daftar detail) tidak dapat diubah setelah dibuat.
 */
public final class Transaction {

    private final int id;
    private final LocalDateTime date;
    private final String customerName;
    private final BigDecimal total;
    private final BigDecimal cashPaid;
    private final BigDecimal changeAmount;
    private final List<TransactionDetail> details;

    public Transaction(int id, LocalDateTime date, String customerName, BigDecimal total,
            BigDecimal cashPaid, BigDecimal changeAmount, List<TransactionDetail> details) {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.total = total;
        this.cashPaid = cashPaid;
        this.changeAmount = changeAmount;
        this.details = List.copyOf(details);
    }

    public int getId() {
        return id;
    }

    /** Nomor transaksi untuk ditampilkan, contoh: TRX-0001. */
    public String getNumber() {
        return String.format("TRX-%04d", id);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getCashPaid() {
        return cashPaid;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    /** Daftar detail yang tidak dapat diubah. */
    public List<TransactionDetail> getDetails() {
        return details;
    }
}
