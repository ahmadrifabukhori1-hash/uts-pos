package service;

/**
 * Dilempar ketika input atau aturan bisnis dilanggar. Pesannya sudah berupa
 * kalimat yang aman ditampilkan langsung kepada pengguna.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
