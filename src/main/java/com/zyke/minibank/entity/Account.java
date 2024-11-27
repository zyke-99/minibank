package com.zyke.minibank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
@Audited
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @ManyToMany
    @JoinTable(
            name = "account_customer",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers;
}
