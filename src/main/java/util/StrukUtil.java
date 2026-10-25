package util;

import java.util.ArrayList;
import java.util.List;
import model.Transaction;
import model.TransactionDetail;

/**
 * Membuat teks struk dari sebuah transaksi tersimpan. Seluruh nilai dibaca dari
 * snapshot di Transaction/TransactionDetail, bukan dari data produk saat ini.
 */
public final class StrukUtil {

    /** Lebar struk dalam karakter (font monospaced). */
    public static final int LEBAR = 40;

    private StrukUtil() {
    }

    public static List<String> buat(Transaction t) {
        List<String> baris = new ArrayList<>();
        baris.add(tengah("KASIR OOP POLOS"));
        baris.add(tengah("STRUK PEMBELIAN"));
        baris.add(garis('='));
        baris.add(kiriKanan("No. Transaksi", t.getNumber()));
        baris.add(kiriKanan("Tanggal", DateUtil.format(t.getDate())));
        baris.add(kiriKanan("Pembeli", t.getCustomerName()));
        baris.add(garis('-'));
        for (TransactionDetail d : t.getDetails()) {
            for (String bagian : potong(d.getProductName(), LEBAR)) {
                baris.add(bagian);
            }
            baris.add(kiriKanan("  " + d.getQuantity() + " x " + CurrencyUtil.format(d.getPrice()),
                    CurrencyUtil.format(d.getSubtotal())));
        }
        baris.add(garis('-'));
        baris.add(kiriKanan("TOTAL", CurrencyUtil.format(t.getTotal())));
        baris.add(kiriKanan("TUNAI", CurrencyUtil.format(t.getCashPaid())));
        baris.add(kiriKanan("KEMBALIAN", CurrencyUtil.format(t.getChangeAmount())));
        baris.add(garis('='));
        baris.add(tengah("Terima kasih"));
        return baris;
    }

    public static String buatTeks(Transaction t) {
        return String.join("\n", buat(t));
    }

    private static String garis(char c) {
        return String.valueOf(c).repeat(LEBAR);
    }

    private static String tengah(String s) {
        int spasi = Math.max(0, (LEBAR - s.length()) / 2);
        return " ".repeat(spasi) + s;
    }

    private static String kiriKanan(String kiri, String kanan) {
        int spasi = LEBAR - kiri.length() - kanan.length();
        if (spasi < 1) {
            // Terlalu panjang: kanan dipindah ke baris berikutnya, rata kanan.
            return kiri + "\n" + " ".repeat(Math.max(0, LEBAR - kanan.length())) + kanan;
        }
        return kiri + " ".repeat(spasi) + kanan;
    }

    private static List<String> potong(String s, int lebar) {
        List<String> hasil = new ArrayList<>();
        for (int i = 0; i < s.length(); i += lebar) {
            hasil.add(s.substring(i, Math.min(s.length(), i + lebar)));
        }
        if (hasil.isEmpty()) {
            hasil.add("");
        }
        return hasil;
    }

    /** Memecah daftar baris yang mengandung "\n" menjadi baris tunggal (untuk dicetak). */
    public static List<String> ratakan(List<String> baris) {
        List<String> hasil = new ArrayList<>();
        for (String b : baris) {
            for (String p : b.split("\n", -1)) {
                hasil.add(p);
            }
        }
        return hasil;
    }
}
