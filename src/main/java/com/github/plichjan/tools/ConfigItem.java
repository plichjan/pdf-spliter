package com.github.plichjan.tools;

/**
 * @author jan.plichta
 * @since 28.12.2015
 */
public class ConfigItem {
    private int fromPage;
    private int toPage;
    private String fileName;

    public int getFromPage() {
        return fromPage;
    }

    public void setFromPage(int fromPage) {
        this.fromPage = fromPage;
    }

    public int getToPage() {
        return toPage;
    }

    public void setToPage(int toPage) {
        this.toPage = toPage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
