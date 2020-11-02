package com.retailstore.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.retailstore.entity.Customer;
import com.retailstore.entity.Invoice;
import com.retailstore.entity.Item;
import com.retailstore.entity.enums.CustomerType;
import com.retailstore.entity.enums.ItemType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InvoiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private  InvoiceRepository invoiceRepository;


    @Before
    public void setup() {

    }

    @After
    public void tearDown() {
        entityManager.clear();
    }


    @Test
    public void whenFindByIdAndCustomer_Id_isOk() {

        // Add Customers
        Customer customer = Customer.builder()
                .firstName("Christian")
                .lastName("Semaan")
                .type(CustomerType.AFFILIATE)
                .invoices(new ArrayList<>())
                .build();

        entityManager.persist(customer);

        customer = Customer.builder()
                .firstName("Vero")
                .lastName("Abach")
                .type(CustomerType.AFFILIATE)
                .invoices(new ArrayList<>())
                .build();
        entityManager.persist(customer);

        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());

        // Add invoices to first Customer
        customer = customers.get(0);
        Invoice invoice = Invoice.builder()
                .customer(customer)
                .name("Aoun Supermarket Invoice")
        .build();
        customer.getInvoices().add(invoice);
        entityManager.persist(customer);


        // Add invoices to second Customer
        customer = customers.get(1);
        invoice = Invoice.builder()
                .customer(customer)
                .name("Fahed Supermarket Invoice")
                .build();
        customer.getInvoices().add(invoice);
        entityManager.persist(customer);

        assertEquals(2, invoiceRepository.findAll().size());

        customers = customerRepository.findAll();
        customer = customers.get(0);
        Long customerId = customer.getId();
        Long invoiceId = customer.getInvoices().get(0).getId();

        // Assert the findByIdAndCustomer_Id
        Optional<Invoice> customerInvoice = invoiceRepository.findByIdAndCustomer_Id(invoiceId, customerId);
        assertTrue(customerInvoice.isPresent());
        assertEquals("Christian", customerInvoice.get().getCustomer().getFirstName());
        assertEquals("Aoun Supermarket Invoice", customerInvoice.get().getName());


        customerInvoice = invoiceRepository.findByIdAndCustomer_Id(invoiceId, 555555L);
        assertFalse(customerInvoice.isPresent());

    }

}
