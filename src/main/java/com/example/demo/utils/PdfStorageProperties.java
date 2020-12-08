package com.example.demo.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class PdfStorageProperties {
    private String pdfDir;

    public String getPdfDir() {
        return pdfDir;
    }

    public void setPdfDir(String uploadDir) {
        this.pdfDir = uploadDir;
    }
}
