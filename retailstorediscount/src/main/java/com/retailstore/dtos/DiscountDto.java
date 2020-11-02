package com.retailstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DiscountDto {

    private BigDecimal totalPurchasedAmount;
    private BigDecimal percentageDiscountAmount;
    private BigDecimal oneHundredDiscountAmount;
    private BigDecimal totalDiscountAmount;
}
