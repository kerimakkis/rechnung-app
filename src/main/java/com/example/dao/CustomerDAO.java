package com.example.dao;

import com.example.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setAddress(rs.getString("address"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            //System.out.println("Loaded Customer: " + customer.getName());
            customers.add(customer);
        }

        /*for (Customer customer : customers) {
            System.out.println("Loaded Customer: "+ customer);
        }*/

        return customers;
    }
}

