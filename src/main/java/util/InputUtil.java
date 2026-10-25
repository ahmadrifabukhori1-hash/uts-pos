package util;

import java.math.BigDecimal;
import service.ValidationException;

/** Mengubah teks dari kolom input menjadi angka, dengan pesan kesalahan yang jelas. */
public final class InputUtil {

    private InputUtil() {
    }

    /** Bilangan bulat. Aturan nilai (misalnya harus &gt; 0) diperiksa oleh service. */
    public static int parseInt(String text, String fieldName) {
        String s = text == null ? "" : text.trim();
        if (s.isEmpty()) {
            throw new ValidationException(fieldName + " tidak boleh kosong.");
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new ValidationException(fieldName + " harus berupa angka bulat (contoh: 10).");
        }
    }

    /**
     * Nilai uang. Menerima: 5000, 5.000, Rp5.000, 5000,50, 5.000,50.
     * Titik dianggap pemisah ribuan, koma dianggap pemisah desimal.
     */
    public static BigDecimal parseMoney(String text, String fieldName) {
        String s = text == null ? "" : text.trim();
        if (s.regionMatches(true, 0, "Rp", 0, 2)) {
            s = s.substring(2).trim();
        }
        if (s.isEmpty()) {
            throw new ValidationException(fieldName + " tidak boleh kosong.");
        }
        if (s.contains(",")) {
            s = s.replace(".", "").replace(',', '.');
        } else if (s.matches("\\d{1,3}(\\.\\d{3})+")) {
            s = s.replace(".", "");
        }
        if (!s.matches("\\d+(\\.\\d+)?")) {
            throw new ValidationException(fieldName + " harus berupa angka (contoh: 5000).");
        }
        BigDecimal value = new BigDecimal(s);
        if (value.stripTrailingZeros().scale() > 2) {
            throw new ValidationException(fieldName + " maksimal memiliki dua angka di belakang koma.");
        }
        if (value.precision() - value.scale() > 13) {
            throw new ValidationException(fieldName + " terlalu besar.");
        }
        return value;
    }
}
