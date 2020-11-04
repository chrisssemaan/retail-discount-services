package com.retailstore.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.retailstore.entity.Customer;
import com.retailstore.entity.enums.CustomerType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {
        entityManager.clear();
    }


    @Test
    public void whenSaveCustomer_isOk() {

        Customer customer = Customer.builder()
                .firstName("Christian")
                .lastName("Semaan")
                .type(CustomerType.AFFILIATE)
                .build();

        entityManager.persist(customer);

        assertEquals(1, customerRepository.findAll().size());
        Optional<Customer> customerOptional = customerRepository.findById(customer.getId());

        assertTrue(customerOptional.isPresent());
        assertEquals(customer.getFirstName(), customerOptional.get().getFirstName());
        assertEquals(customer.getLastName(), customerOptional.get().getLastName());
        assertNotNull(customerOptional.get().getDateCreated());

    }
}





