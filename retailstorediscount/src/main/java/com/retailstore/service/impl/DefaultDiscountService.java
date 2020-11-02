package com.retailstore.service.impl;

import static java.lang.String.format;

import com.retailstore.dtos.DiscountDto;
import com.retailstore.entity.Customer;
import com.retailstore.entity.Invoice;
import com.retailstore.entity.Item;
import com.retailstore.entity.enums.ItemType;
import com.retailstore.repository.InvoiceRepository;
import com.retailstore.service.DiscountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Default discount service.
 */
@Log4j2
@Service
public class DefaultDiscountService implements DiscountService {


    private static final String INVOICE_DOES_NOT_EXIST = "Invoice Id [%s] not Found for customer with Id [%s]";

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal EMPLOYEE_DISCOUNT = BigDecimal.valueOf(30);
    private static final BigDecimal AFFILIATE_DISCOUNT = BigDecimal.valueOf(10);
    private static final BigDecimal CUSTOMER_OVER_TWO_YEARS = BigDecimal.valueOf(5);
    private static final BigDecimal SPENDING_OVER_100 = BigDecimal.valueOf(5);

    private final InvoiceRepository invoiceRepository;


    /**
     * Instantiates a new Default discount service.
     *
     * @param invoiceRepository the invoice repository
     */
    public DefaultDiscountService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public DiscountDto calculateDiscount(Long customerId, Long invoiceId) {

        log.info("Calculating Discounted Amount For Customer [{}], and invoice with id [{}]", customerId,
                invoiceId);

        log.debug("Getting the customer Invoice with validation");
        Invoice invoice = this.invoiceRepository.findByIdAndCustomer_Id(invoiceId, customerId)
                .orElseThrow(() -> {
                    log.warn("Invoice with id [{}], Not Found", invoiceId);
                    return new EntityNotFoundException(format(INVOICE_DOES_NOT_EXIST, invoiceId, customerId));
                });

        log.debug("Calculating the total purchased items amount");
        BigDecimal totalPurchasedAmount = this.calculatePurchasedItems(invoice.getItems());
        log.debug("Calculated total purchased items amount [{}]", totalPurchasedAmount);

        log.debug("Calculating the items amount that the discount apply");
        BigDecimal totalDiscountableAmount = this.calculateDiscountItems(invoice.getItems());
        log.debug("Calculated the items amount that the discount apply [{}]", totalPurchasedAmount);

        // Calculate the percentage discount base on the customer type
        Customer customer = invoice.getCustomer();
        log.debug("Calculating the discount for the customer type [{}]", customer.getType());
        BigDecimal percentageDiscountAmount = BigDecimal.ZERO;
        switch (customer.getType()) {
            case EMPLOYEE:
                percentageDiscountAmount = this.calculatePercentage(EMPLOYEE_DISCOUNT, totalDiscountableAmount);
                break;
            case AFFILIATE:
                percentageDiscountAmount = this.calculatePercentage(AFFILIATE_DISCOUNT, totalDiscountableAmount);
                break;
            case CUSTOMER:
                if (customer.getDateCreated().isBefore(LocalDate.now().minusYears(2))) {
                    percentageDiscountAmount = this.calculatePercentage(CUSTOMER_OVER_TWO_YEARS,
                            totalDiscountableAmount);
                }
                break;
            default:
                throw new IllegalStateException("Customer Type cannot be [" + customer.getType() + "]");
        }

        BigDecimal oneHundredDiscountAmount = this.calculateOneHundredDiscountAmount(totalPurchasedAmount);
        log.debug("Calculated oneHundredDiscountAmount amount [{}]", oneHundredDiscountAmount);
        BigDecimal totalDiscountAmount = percentageDiscountAmount.add(oneHundredDiscountAmount);
        log.debug("Calculated totalDiscountAmount amount [{}]", totalDiscountAmount);

        DiscountDto discountDto = DiscountDto.builder()
                    .totalPurchasedAmount(totalPurchasedAmount)
                    .percentageDiscountAmount(percentageDiscountAmount)
                    .oneHundredDiscountAmount(oneHundredDiscountAmount)
                    .totalDiscountAmount(totalDiscountAmount)
                .build();

        log.trace("Discount Body: {}", discountDto);
        log.info(" Discounted Amount For Customer [{}], and invoice [{}] Successfully calculated",
                customerId, invoiceId);
        return discountDto;
    }

    /**
     * Calculate customer discounts big decimal.
     *
     * @param totalPurchaseAmount the total purchase amount
     * @return the big decimal
     */
    private BigDecimal calculateOneHundredDiscountAmount(BigDecimal totalPurchaseAmount) {
        if (totalPurchaseAmount.compareTo(HUNDRED) > 0) {
            return totalPurchaseAmount.divide(HUNDRED, 0, RoundingMode.DOWN).multiply(SPENDING_OVER_100);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calculate total items amount.
     *
     * @param purchasedItems the purchased items
     * @return the big decimal
     */
    private BigDecimal calculatePurchasedItems(List<Item> purchasedItems) {
        return purchasedItems.stream()
                .map(Item::getItemPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0));
    }

    /**
     * Calculate discount items big decimal.
     *
     * @param purchasedItems the purchased items
     * @return the big decimal
     */
    private BigDecimal calculateDiscountItems(List<Item> purchasedItems) {
        return purchasedItems.stream()
                .filter(i -> i.getItemType() != ItemType.GROCERY)
                .map(Item::getItemPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0));
    }

    /**
     * Calculate percentage big decimal.
     *
     * @param discount the discount
     * @param total    the total
     * @return the big decimal
     */
    private BigDecimal calculatePercentage(BigDecimal discount, BigDecimal total) {

      /*   100          -> 30
           total amount -> ?
       */
        return total.multiply(discount).divide(HUNDRED, 0, RoundingMode.FLOOR);
    }
}
