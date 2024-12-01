package com.zyke.minibank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer customer) {

        customer.getAccounts().add(this);
        this.customers.add(customer);
    }
}
