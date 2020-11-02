package com.retailstore.dtos;

import com.retailstore.entity.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private CustomerType type;
    private LocalDate dateCreated;
}