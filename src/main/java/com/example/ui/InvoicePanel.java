package com.example.ui;

import com.example.model.Customer;
import com.example.model.Invoice;
import com.example.model.InvoiceItem;
import com.example.model.Product;
import com.example.service.InvoiceService;
import com.example.util.DatabaseHelper;
import com.example.util.PDFGenerator;
import com.example.dao.CustomerDAO;
import com.example.dao.ProductDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JComboBox<Customer> customerComboBox;
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JButton addButton;
    private JButton generateButton;
    private JTextArea invoiceArea;

    private List<InvoiceItem> invoiceItems;

    public InvoicePanel() {
        setLayout(new BorderLayout());

        invoiceItems = new ArrayList<>();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        try (Connection connection = DatabaseHelper.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(connection);
            ProductDAO productDAO = new ProductDAO(connection);

            //customerComboBox = new JComboBox<>(customerDAO.getAllCustomers().toArray(new Customer[0]));
            //productComboBox = new JComboBox<>(productDAO.getAllProducts().toArray(new Product[0]));
            List<Customer> customers = customerDAO.getAllCustomers();
            if (customers.isEmpty()){
                System.out.println("No Customers found");

            }else {
                System.out.println("Customers Found : "+customers.size());
            }
            customerComboBox= new JComboBox<>(customers.toArray(new Customer[0]));

            List<Product> products = productDAO.getAllProducts();
            productComboBox= new JComboBox<>(products.toArray(new Product[0]));



        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error accessing database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        quantityField = new JTextField();
        addButton = new JButton("Add Product");
        generateButton = new JButton("Generate Invoice");

        inputPanel.add(new JLabel("Customer:"));
        inputPanel.add(customerComboBox);
        inputPanel.add(new JLabel("Product:"));
        inputPanel.add(productComboBox);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(generateButton);

        add(inputPanel, BorderLayout.NORTH);

        invoiceArea = new JTextArea();
        add(new JScrollPane(invoiceArea), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product selectedProduct = (Product) productComboBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());

                InvoiceItem item = new InvoiceItem();
                item.setProduct(selectedProduct);
                item.setQuantity(quantity);
                item.setTotalPrice(selectedProduct.getUnitPrice() * quantity);
                invoiceItems.add(item);

                updateInvoiceArea();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
                InvoiceService invoiceService = new InvoiceService();
                Invoice invoice = invoiceService.createInvoice(selectedCustomer, invoiceItems);

                PDFGenerator pdfGenerator = new PDFGenerator();
                try {
                    pdfGenerator.generateInvoicePDF(invoice, "invoice.pdf");
                    JOptionPane.showMessageDialog(InvoicePanel.this, "Invoice PDF generated successfully.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void updateInvoiceArea() {
        StringBuilder builder = new StringBuilder();
        for (InvoiceItem item : invoiceItems) {
            builder.append("Product: ").append(item.getProduct().getName())
                    .append(", Quantity: ").append(item.getQuantity())
                    .append(", Total Price: ").append(item.getTotalPrice())
                    .append("\n");
        }
        invoiceArea.setText(builder.toString());
    }
}
