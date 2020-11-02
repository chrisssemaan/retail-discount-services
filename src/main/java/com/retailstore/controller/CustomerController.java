package com.retailstore.controller;

import com.retailstore.dtos.CustomerDto;
import com.retailstore.dtos.InvoiceDto;
import com.retailstore.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Customer controller.
 */
@RequestMapping(value = "api/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Instantiates a new Customer controller.
     *
     * @param customerService the customer service
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Rest Resource that get the list of All store customers
     *
     * @return list of all {}
     */
    @ApiOperation(
            notes = "Returns  list of all store customers.",
            value = "Get a list of all store customers.",
            nickname = "listAll",
            tags = {"customers"})
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return new ResponseEntity<>(this.customerService.getAllCustomers(), HttpStatus.OK);
    }

    /**
     * Rest Resource that return single Customer by Id
     *
     * @param customerId
     * @return customer
     */
    @ApiOperation(notes = "Returns a Customer.",
            value = "Get Customer",
            nickname = "getCustomerById",
            tags = {"customers"})
    @GetMapping(value = "/{customer-id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customer-id") Long customerId) {

        CustomerDto customerDTO = this.customerService.getCustomerById(customerId);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @ApiOperation(notes = "Returns a Customer invoices",
            value = "Get Customer invoices",
            nickname = "getCustomerInvoices",
            tags = {"customers"})
    @GetMapping(value = "/{customer-id}/invoices")
    public ResponseEntity<List<InvoiceDto>> getCustomerInvoices(@PathVariable("customer-id") Long customerId) {

        List<InvoiceDto> invoices = this.customerService.getCustomerInvoices(customerId);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }
}
