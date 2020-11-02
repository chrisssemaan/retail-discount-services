package com.retailstore.entity;

import com.retailstore.entity.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    @NotEmpty
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerType type;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Invoice> invoices;
}
