package com.retailstore.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.retailstore.dtos.CustomerDto;
import com.retailstore.dtos.InvoiceDto;
import com.retailstore.dtos.ItemDto;
import com.retailstore.entity.Customer;
import com.retailstore.entity.Invoice;
import com.retailstore.entity.Item;
import com.retailstore.entity.enums.CustomerType;
import com.retailstore.entity.enums.ItemType;
import com.retailstore.repository.CustomerRepository;
import com.retailstore.service.impl.DefaultCustomerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DefaultCustomerService customerService;

    private Customer customer;
    private Long customerId;

    @Before
    public void setup() {

        customerId = 1L;

        customer = Customer.builder()
                .id(customerId)
                .firstName("Chris")
                .lastName("Semaan")
                .type(CustomerType.AFFILIATE)
                .build();
    }

    @After
    public void tearDown() {

        this.expectedException = ExpectedException.none();
    }

    @Test
    public void whenFindAllCustomers_isOk() {


        List<Customer> customers = new ArrayList<>();
        customers.add(customer);

        when(this.customerRepository.findAll()).thenReturn(customers);

        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(1, result.size());

        List<CustomerDto> expectedResult = new ArrayList<>();
        expectedResult.add(CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .type(customer.getType())
                .lastName(customer.getLastName())
                .dateCreated(customer.getDateCreated()).build());


        assertEquals(expectedResult, result);
    }

    @Test
    public void whenGetCustomerById_NotFound_throwException() {

        this.expectedException.expect(EntityNotFoundException.class);
        this.expectedException.expectMessage("Customer with id [" + customerId + "] does not exist");

        when(this.customerRepository.findById(customerId)).thenReturn(Optional.empty());
        customerService.getCustomerById(customerId);
    }


    @Test
    public void whenGetCustomerById_isOk() {

        when(this.customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(customerId);

        assertEquals(result.getFirstName(), customer.getFirstName());
        assertEquals(result.getLastName(), customer.getLastName());
    }

    @Test
    public void whenGetCustomerInvoices_isOk() {

        List<Invoice> invoices = Lists.newArrayList(
                Invoice.builder()
                        .id(1L)
                        .customer(customer)
                        .name("Fahed Supermarket Invoice")
                        .items(
                                Lists.newArrayList(
                                        Item.builder()
                                                .name("Grocery1")
                                                .itemType(ItemType.GROCERY)
                                                .itemPrice(BigDecimal.valueOf(150))
                                                .build())
                        )
                        .build());
        customer.setInvoices(invoices);

        when(this.customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        List<InvoiceDto> customerInvoicesResult = customerService.getCustomerInvoices(customerId);

        List<InvoiceDto> expectedInvoices = Lists.newArrayList(
                InvoiceDto.builder()
                        .id(1L)
                        .name("Fahed Supermarket Invoice")
                        .items(
                                Lists.newArrayList(
                                        ItemDto.builder()
                                                .name("Grocery1")
                                                .itemType(ItemType.GROCERY)
                                                .itemPrice(BigDecimal.valueOf(150))
                                                .build())
                        ).build());

        assertEquals(expectedInvoices, customerInvoicesResult);
    }
}
