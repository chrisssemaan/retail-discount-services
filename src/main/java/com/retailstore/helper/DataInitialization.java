package com.retailstore.helper;

import com.retailstore.entity.Customer;
import com.retailstore.entity.Invoice;
import com.retailstore.entity.Item;
import com.retailstore.entity.enums.CustomerType;
import com.retailstore.entity.enums.ItemType;
import com.retailstore.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data initialization.
 */
@Component
@Log4j2
public class DataInitialization implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    /**
     * Instantiates a new Data initialization.
     *
     * @param customerRepository the customer repository
     */
    public DataInitialization(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {


        log.info("Loading data...");
        try {
            List<Customer> customers = new ArrayList<>();

            Customer customer = Customer.builder()
                    .firstName("Chris")
                    .lastName("Semaan")
                    .type(CustomerType.AFFILIATE)
                    .build();
            customer.setInvoices(this.getInvoices(customer));
            customers.add(customer);


            customer = Customer.builder()
                    .firstName("Veronica")
                    .lastName("Abachian")
                    .type(CustomerType.EMPLOYEE)
                    .build();
            customer.setInvoices(getInvoices(customer));
            customers.add(customer);

            this.customerRepository.saveAll(customers);

            log.info("Data Loaded...");

        } catch (Exception e) {
          log.error("Error Loading initial Data", e);
        }
    }

    private List<Invoice> getInvoices(Customer customer) {

        List<Invoice> invoices = new ArrayList<>();
        Invoice invoice = Invoice.builder()
                .customer(customer)
                .name("Aoun Supermarket").build();
        invoices.add(invoice);

        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .name("Grocery1")
                .itemType(ItemType.GROCERY)
                .invoice(invoice)
                .itemPrice(BigDecimal.valueOf(150)).build());

        items.add(Item.builder()
                .name("Steak")
                .itemType(ItemType.MEAT)
                .invoice(invoice)
                .itemPrice(BigDecimal.valueOf(400)).build());


        items.add(Item.builder()
                .name("Raw Meat")
                .itemType(ItemType.OTHER)
                .invoice(invoice)
                .itemPrice(BigDecimal.valueOf(350)).build());

        invoice.setItems(items);

        return invoices;



    }
}
