package com.github.plichjan.tools;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        final FileInputStream inputStream = new FileInputStream(args[1]);
        final PdfReader inputPDF = new PdfReader(inputStream);
        List<ConfigItem> items = readConfig(args[0]);
        for (ConfigItem item : items) {
            splitPDF(new FileOutputStream(item.getFileName()), item.getFromPage(), item.getToPage(), inputPDF);
        }
    }

    private static List<ConfigItem> readConfig(String configFileName) throws IOException {
        final List<ConfigItem> items = new ArrayList<ConfigItem>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFileName), "UTF-8"));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            items.add(parseConfigLine(line));
        }
        return items;
    }

    private static ConfigItem parseConfigLine(String line) {
        final ConfigItem item = new ConfigItem();
        final String[] split = line.trim().split("\\s+", 2);
        item.setFileName(split[1]);
        final String interval = split[0];
        if (interval.contains("-")) {
            final String[] boundaries = interval.split("-", 2);
            item.setFromPage(Integer.parseInt(boundaries[0]));
            item.setToPage(Integer.parseInt(boundaries[1]));
        } else {
            item.setFromPage(Integer.parseInt(interval));
            item.setToPage(Integer.parseInt(interval));
        }
        return item;
    }

    /**
     * @see <a href="http://viralpatel.net/blogs/itext-tutorial-merge-split-pdf-files-using-itext-jar/">zdroj</a>
     *
     * @param outputStream Output PDF file
     * @param fromPage     start page from input PDF file
     * @param toPage       end page from input PDF file
     * @param inputPDF     Input PDF file
     */
    public static void splitPDF(OutputStream outputStream, int fromPage, int toPage, PdfReader inputPDF) {
        Document document = new Document();
        try {
            int totalPages = inputPDF.getNumberOfPages();

            //make fromPage equals to toPage if it is greater
            if (toPage > totalPages) {
                toPage = totalPages;
            }
            if (fromPage > toPage) {
                fromPage = toPage;
            }


            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
            PdfImportedPage page;

            while (fromPage <= toPage) {
                document.newPage();
                page = writer.getImportedPage(inputPDF, fromPage);
                cb.addTemplate(page, 0, 0);
                fromPage++;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
