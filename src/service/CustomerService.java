package service;

import model.Customer;

import java.util.*;

public class CustomerService {
    private static final Map<String, Customer> mapOfCustomers = new HashMap<>();
    private static CustomerService customerService = null;

    private CustomerService() { }
    public static CustomerService getInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }
    public void addCustomer(String email, String firstName, String lastName) {
        if (mapOfCustomers.containsKey(email)) {
            throw new IllegalArgumentException("Requested email address already exists. Please re-enter a new email address:");
        }
        Customer customer = new Customer(firstName, lastName, email);
        mapOfCustomers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        Customer customer = mapOfCustomers.get(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Failed: requested customer email " + customerEmail + " does not exist.");
        }
        return customer;
    }

    public Collection<Customer> getAllCustomers() {
        return mapOfCustomers.values();
    }


}
