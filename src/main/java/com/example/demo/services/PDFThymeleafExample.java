package com.example.demo.services;

import com.example.demo.exceptions.FileStorageException;
import com.example.demo.utils.PdfStorageProperties;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFThymeleafExample {
    private final Path pdfStorageProperties;
    @Autowired
    public PDFThymeleafExample(PdfStorageProperties pdfStorageProperties) {
        this.pdfStorageProperties = Paths.get(pdfStorageProperties.getPdfDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.pdfStorageProperties);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void generatePdfFromHtml(String html) throws IOException, DocumentException {
        String namePdf = "thymeleaf.pdf";
        Path outputFolder = this.pdfStorageProperties.resolve(namePdf);
        OutputStream outputStream = new FileOutputStream(String.valueOf(outputFolder));

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.finishPDF();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    public String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        //set variable untuk di panggil di html nya
        context.setVariable("to", "Lukman Hidayah");
        context.setVariable("baseUrl", getCurrentBaseUrl());

        return templateEngine.process("thymeleaf_template", context);
    }

    private static String getCurrentBaseUrl() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

}
