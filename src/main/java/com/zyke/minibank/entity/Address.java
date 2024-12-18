package com.zyke.minibank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "address")
@Audited
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class Address extends BaseEntity {

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "region")
    private String region;

    @Column(name = "town", nullable = false)
    private String town;

    @Column(name = "street_type")
    private String streetType;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "street_number", nullable = false)
    private String streetNumber;

    @Column(name = "floor")
    private String floor;
}
