package com.example.service;

import com.example.model.Customer;
import com.example.model.Invoice;
import com.example.model.InvoiceItem;
import com.example.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {
    public Invoice createInvoice(Customer customer, List<InvoiceItem> items) {
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setItems(items);

        double totalAmount = 0;
        for (InvoiceItem item : items) {
            totalAmount += item.getTotalPrice();
        }

        BigDecimal totalAmountBD = BigDecimal.valueOf(totalAmount);
        BigDecimal taxRate = BigDecimal.valueOf(0.18);
        BigDecimal taxAmountBD = totalAmountBD.multiply(taxRate);
        taxAmountBD = taxAmountBD.setScale(2, RoundingMode.HALF_UP);

        double taxAmount = taxAmountBD.doubleValue();
        BigDecimal grandTotalBD = totalAmountBD.add(taxAmountBD);
        grandTotalBD = grandTotalBD.setScale(2, RoundingMode.HALF_UP);
        double grandTotal = grandTotalBD.doubleValue();

        //double grandTotal = totalAmount + taxAmount;

        invoice.setTotalAmount(totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setGrandTotal(grandTotal);

        return invoice;
    }
}

