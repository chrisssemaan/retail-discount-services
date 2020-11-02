package com.retailstore.repository;

import com.retailstore.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


   /* select
        invoice0_.id as id1_1_,
        invoice0_.customer_id as customer4_1_,
        invoice0_.date_created as date_cre2_1_,
        invoice0_.name as name3_1_
    from
    invoice invoice0_
    left outer join
    customer customer1_
    on invoice0_.customer_id = customer1_.id
    where
    invoice0_.id =?
    and customer1_.id =?*/

    Optional<Invoice> findByIdAndCustomer_Id(Long invoiceId, Long customerId);

}
