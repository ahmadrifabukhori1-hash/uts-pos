package crud;

/** Kegagalan saat mengakses database (koneksi putus, query salah, dan sebagainya). */
public class DataException extends RuntimeException {

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(String message) {
        super(message);
    }
}
