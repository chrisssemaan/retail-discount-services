package com.retailstore.service;

import com.retailstore.dtos.DiscountDto;

/**
 * The interface Discount service.
 *
 * @author Christian.Semaan
 */
public interface DiscountService {

    /**
     * Calculate discount big decimal.
     *
     * @param customerId the customer id
     * @param invoiceId  the invoice id
     * @return the big decimal
     */
    DiscountDto calculateDiscount(Long customerId, Long invoiceId);
}
