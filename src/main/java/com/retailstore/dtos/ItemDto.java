package com.retailstore.dtos;

import com.retailstore.entity.enums.ItemType;
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
public class ItemDto {

    private String name;

    private ItemType itemType;

    private BigDecimal itemPrice;
}
