package com.retailstore.controller;

import com.retailstore.dtos.DiscountDto;
import com.retailstore.service.DiscountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Discount controller.
 */
@RequestMapping(value = "api/discount")
@RestController
public class DiscountController {


    private final DiscountService discountService;

    /**
     * Instantiates a new Discount controller.
     *
     * @param discountService the discount service
     */
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }


    /**
     * Rest Api to get discount.
     *
     * @param customerId the customer id
     * @param invoiceId  the invoice id
     * @return the discount
     */
    @ApiOperation(notes = "Returns a Customer Invoices Discount",
            value = "Get Customer Invoices Discount",
            nickname = "getDiscount",
            tags = {"discount"})
    @GetMapping(value = "/customers/{customer-id}/invoices/{invoice-id}")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable("customer-id") Long customerId,
                                                   @PathVariable("invoice-id") Long invoiceId) {

        DiscountDto discountDto = this.discountService.calculateDiscount(customerId, invoiceId);
        return new ResponseEntity<>(discountDto, HttpStatus.OK);
    }
}