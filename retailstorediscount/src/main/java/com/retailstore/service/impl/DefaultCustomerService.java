package com.retailstore.service.impl;

import static java.lang.String.format;

import com.retailstore.dtos.CustomerDto;
import com.retailstore.dtos.InvoiceDto;
import com.retailstore.dtos.ItemDto;
import com.retailstore.entity.Customer;
import com.retailstore.entity.Item;
import com.retailstore.repository.CustomerRepository;
import com.retailstore.service.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DefaultCustomerService implements CustomerService {

    private static final String CUSTOMER_DOES_NOT_EXIST = "Customer with id [%s] does not exist.";

    private final CustomerRepository customerRepository;

    public DefaultCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        log.info("Getting all Customers");

        List<CustomerDto> customers = this.customerRepository.findAll()
                .stream()
                .map(customer -> CustomerDto.builder()
                        .id(customer.getId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .type(customer.getType())
                        .dateCreated(customer.getDateCreated()).build())
                .collect(Collectors.toList());

        log.trace("All returned Customers : {}", customers);
        log.info("Customers retrieved successfully with size [{}] ",
                customers.size());
        return customers;
    }

    @Override
    public CustomerDto getCustomerById(Long id) {

        log.info("Finding customer by id [{}]", id);

        Customer customer = this.findById(id);
        return CustomerDto.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .type(customer.getType())
                    .dateCreated(customer.getDateCreated())
                .build();
    }

    @Override
    public List<InvoiceDto> getCustomerInvoices(Long id) {

        log.info("Finding customer with id [{}], all invoices ", id);

        List<InvoiceDto> invoices = this.findById(id).getInvoices()
                .stream()
                .map(invoice -> {

                    InvoiceDto invoiceDto = InvoiceDto.builder()
                            .id(invoice.getId())
                            .name(invoice.getName())
                            .dateCreated(invoice.getDateCreated())
                            .items(new ArrayList<>())
                            .build();

                    for (Item item : invoice.getItems()) {
                        ItemDto itemDto = ItemDto.builder()
                                .name(item.getName())
                                .itemType(item.getItemType())
                                .itemPrice(item.getItemPrice())
                                .build();
                        invoiceDto.getItems().add(itemDto);
                    }

                    return invoiceDto;
                }).collect(Collectors.toList());

        log.trace("All returned Invoices : {}", invoices);
        log.info("returned retrieved successfully with size [{}] ", invoices.size());

        return invoices;
    }

    /**
     * Find customer by id customer.
     *
     * @param id the id
     * @return the customer
     */
    private Customer findById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Customer with id [{}], Not Found", id);
                    return new EntityNotFoundException(format(CUSTOMER_DOES_NOT_EXIST, id));
                });
    }

}
