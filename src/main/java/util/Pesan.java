package util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Pembungkus JOptionPane agar semua form menampilkan pesan dengan cara yang
 * sama. Handler dapat diganti (setHandler) sehingga pengujian otomatis tidak
 * perlu membuka dialog sungguhan.
 */
public final class Pesan {

    public interface Handler {

        void info(Component parent, String message);

        void error(Component parent, String message);

        boolean confirm(Component parent, String message);
    }

    private static class SwingHandler implements Handler {

        @Override
        public void info(Component parent, String message) {
            JOptionPane.showMessageDialog(parent, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
        public void error(Component parent, String message) {
            JOptionPane.showMessageDialog(parent, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
        }

        @Override
        public boolean confirm(Component parent, String message) {
            return JOptionPane.showConfirmDialog(parent, message, "Konfirmasi",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        }
    }

    private static Handler handler = new SwingHandler();

    private Pesan() {
    }

    public static void setHandler(Handler newHandler) {
        handler = newHandler == null ? new SwingHandler() : newHandler;
    }

    public static void info(Component parent, String message) {
        handler.info(parent, message);
    }

    public static void error(Component parent, String message) {
        handler.error(parent, message);
    }

    public static boolean confirm(Component parent, String message) {
        return handler.confirm(parent, message);
    }
}
