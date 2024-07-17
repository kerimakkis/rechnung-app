package com.example.util;

import com.example.model.Invoice;
import com.example.model.InvoiceItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PDFGenerator {
    public void generateInvoicePDF(Invoice invoice, String filePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 750);
        contentStream.showText("Invoice No: " + invoice.getId());
        contentStream.newLine();
        contentStream.showText("Customer Id: " + invoice.getCustomer().getId());
        contentStream.newLine();
        contentStream.showText("Customer: " + cleanText(invoice.getCustomer().getName()));
        contentStream.newLine();
        contentStream.showText("Address: " + cleanText(invoice.getCustomer().getAddress()));
        contentStream.newLine();
        contentStream.showText("Customer Email: " + cleanText(invoice.getCustomer().getEmail()));
        contentStream.newLine();
        contentStream.showText("Customer Phone: " + invoice.getCustomer().getPhone());
        contentStream.newLine();
        contentStream.newLine();

        for (InvoiceItem item : invoice.getItems()) {
            contentStream.showText("Product: " + item.getProduct().getName() + ", Quantity: " + item.getQuantity() + ", Total Price: " + item.getTotalPrice());
            contentStream.newLine();
        }

        contentStream.newLineAtOffset(400, -20);
        contentStream.showText("Total Amount: " + invoice.getTotalAmount());
        contentStream.newLine();
        contentStream.showText("Tax Amount: " + invoice.getTaxAmount());
        contentStream.newLine();
        contentStream.showText("Grand Total: " + invoice.getGrandTotal());
        contentStream.endText();
        contentStream.close();

        document.save(filePath);
        document.close();
    }

    private String cleanText(String text) {
        return text.replace("\r", "").replace("\n", " ");
    }
}
