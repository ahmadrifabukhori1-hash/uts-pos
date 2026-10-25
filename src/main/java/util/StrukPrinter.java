package util;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.Transaction;

/** Menampilkan pratinjau struk lalu mencetaknya lewat dialog cetak sistem. */
public final class StrukPrinter {

    private static final Font FONT_CETAK = new Font(Font.MONOSPACED, Font.PLAIN, 9);

    private StrukPrinter() {
    }

    /** Pratinjau struk; jika pengguna memilih Cetak, lanjut ke dialog printer. */
    public static void pratinjauDanCetak(Component parent, Transaction t) {
        JTextArea area = new JTextArea(StrukUtil.buatTeks(t));
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        area.setMargin(new java.awt.Insets(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(420, 380));
        int pilihan = JOptionPane.showOptionDialog(parent, scroll, "Struk " + t.getNumber(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new Object[]{"Cetak", "Tutup"}, "Cetak");
        if (pilihan == 0) {
            cetak(parent, t);
        }
    }

    /** Mencetak struk. Pengguna memilih printer di dialog cetak (bisa juga "Save as PDF"). */
    public static void cetak(Component parent, Transaction t) {
        List<String> baris = StrukUtil.ratakan(StrukUtil.buat(t));
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Struk " + t.getNumber());
        job.setPrintable(new StrukPrintable(baris));
        if (!job.printDialog()) {
            return; // dibatalkan pengguna
        }
        try {
            job.print();
            Pesan.info(parent, "Struk " + t.getNumber() + " dikirim ke printer.");
        } catch (PrinterException ex) {
            Pesan.error(parent, "Struk gagal dicetak: " + ex.getMessage());
        }
    }

    private static final class StrukPrintable implements Printable {

        private final List<String> baris;

        StrukPrintable(List<String> baris) {
            this.baris = baris;
        }

        @Override
        public int print(Graphics g, PageFormat pf, int halaman) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(FONT_CETAK);
            FontMetrics fm = g2.getFontMetrics();
            int tinggi = fm.getHeight();
            int perHalaman = Math.max(1, (int) (pf.getImageableHeight() / tinggi));
            int awal = halaman * perHalaman;
            if (awal >= baris.size()) {
                return NO_SUCH_PAGE;
            }
            g2.translate(pf.getImageableX(), pf.getImageableY());
            int y = fm.getAscent();
            for (int i = awal; i < Math.min(baris.size(), awal + perHalaman); i++) {
                g2.drawString(baris.get(i), 0, y);
                y += tinggi;
            }
            return PAGE_EXISTS;
        }
    }
}
