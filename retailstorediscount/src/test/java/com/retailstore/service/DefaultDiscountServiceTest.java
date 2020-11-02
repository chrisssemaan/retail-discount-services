package com.retailstore.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.retailstore.dtos.DiscountDto;
import com.retailstore.entity.Customer;
import com.retailstore.entity.Invoice;
import com.retailstore.entity.Item;
import com.retailstore.entity.enums.CustomerType;
import com.retailstore.entity.enums.ItemType;
import com.retailstore.repository.InvoiceRepository;
import com.retailstore.service.impl.DefaultDiscountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDiscountServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private DefaultDiscountService discountService;

    private Customer customer;
    private Invoice invoice;
    private Long customerId;
    private Long invoiceId;

    @Before
    public void setup() {

        customerId = 1L;
        invoiceId = 2L;

        customer = Customer.builder()
                .id(customerId)
                .firstName("Chris")
                .lastName("Semaan")
                .type(CustomerType.AFFILIATE)
                .dateCreated(LocalDate.now().minusYears(4))
                .build();

        invoice = Invoice.builder()
                .id(invoiceId)
                .customer(customer)
                .name("Aoun Supermarket")
                .items(Lists.newArrayList(
                        Item.builder()
                                .name("Grocery1")
                                .itemType(ItemType.GROCERY)
                                .invoice(invoice)
                                .itemPrice(BigDecimal.valueOf(150)).build(),

                        Item.builder()
                                .name("Steak")
                                .itemType(ItemType.MEAT)
                                .invoice(invoice)
                                .itemPrice(BigDecimal.valueOf(400)).build(),

                        Item.builder()
                                .name("Raw Meat")
                                .itemType(ItemType.OTHER)
                                .invoice(invoice)
                                .itemPrice(BigDecimal.valueOf(350)).build()

                )).build();
    }

    @Test
    public void whenCalculateDiscount_customerType_EMPLOYEE_isOk() {

        customer.setType(CustomerType.EMPLOYEE);

        when(this.invoiceRepository.findByIdAndCustomer_Id(invoiceId, customerId))
                .thenReturn(Optional.of(invoice));

        DiscountDto discountDtoResult = this.discountService.calculateDiscount(customerId, invoiceId);

        DiscountDto expectedDiscountDto = DiscountDto.builder()
                .totalPurchasedAmount(BigDecimal.valueOf(900))
                .percentageDiscountAmount(BigDecimal.valueOf(225))
                .oneHundredDiscountAmount(BigDecimal.valueOf(45))
                .totalDiscountAmount(BigDecimal.valueOf(270))
                .build();

        assertEquals(expectedDiscountDto, discountDtoResult);
    }

    @Test
    public void whenCalculateDiscount_customerType_AFFILIATE_isOk() {

        customer.setType(CustomerType.AFFILIATE);

        when(this.invoiceRepository.findByIdAndCustomer_Id(invoiceId, customerId))
                .thenReturn(Optional.of(invoice));

        DiscountDto discountDtoResult = this.discountService.calculateDiscount(customerId, invoiceId);

        DiscountDto expectedDiscountDto = DiscountDto.builder()
                .totalPurchasedAmount(BigDecimal.valueOf(900))
                .percentageDiscountAmount(BigDecimal.valueOf(75))
                .oneHundredDiscountAmount(BigDecimal.valueOf(45))
                .totalDiscountAmount(BigDecimal.valueOf(120))
                .build();

        assertEquals(expectedDiscountDto, discountDtoResult);
    }


    @Test
    public void whenCalculateDiscount_customerType_CUSTOMER_isOk() {

        customer.setType(CustomerType.CUSTOMER);

        when(this.invoiceRepository.findByIdAndCustomer_Id(invoiceId, customerId))
                .thenReturn(Optional.of(invoice));

        DiscountDto discountDtoResult = this.discountService.calculateDiscount(customerId, invoiceId);

        DiscountDto expectedDiscountDto = DiscountDto.builder()
                .totalPurchasedAmount(BigDecimal.valueOf(900))
                .percentageDiscountAmount(BigDecimal.valueOf(37))
                .oneHundredDiscountAmount(BigDecimal.valueOf(45))
                .totalDiscountAmount(BigDecimal.valueOf(82))
                .build();

        assertEquals(expectedDiscountDto, discountDtoResult);
    }

}
