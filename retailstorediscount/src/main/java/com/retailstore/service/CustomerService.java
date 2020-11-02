package com.retailstore.service;

import com.retailstore.dtos.CustomerDto;
import com.retailstore.dtos.InvoiceDto;

import java.util.List;

/**
 * The interface Customer service.
 *
 * @author Christian.Semaan
 */
public interface CustomerService {


    /**
     * Gets all customers.
     *
     * @return the all customers
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Find customer by id customer dto.
     *
     * @param id the id
     * @return the customer dto
     */
    CustomerDto getCustomerById(Long id);

    /**
     * Gets customer invoices.
     *
     * @param id the id
     * @return the customer invoices
     */
    List<InvoiceDto> getCustomerInvoices(Long id);
}
