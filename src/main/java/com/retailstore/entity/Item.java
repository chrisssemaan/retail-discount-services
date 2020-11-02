package com.retailstore.entity;

import com.retailstore.entity.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @NotNull
    @Column(nullable = false)
    private BigDecimal itemPrice;

    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;
}
