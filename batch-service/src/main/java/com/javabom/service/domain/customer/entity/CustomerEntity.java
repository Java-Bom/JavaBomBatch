package com.javabom.service.domain.customer.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "customer_entity")
@Table(name = "customer_entity")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "middleInitial")
    private String middleInitial;
    @Column(name = "lastName")
    private String lastName;
    private String address;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    @Builder
    public CustomerEntity(Long id, String firstName, String middleInitial, String lastName, String address, String street, String city, String state, String zipCode) {
        this.id = id;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.address = address;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
}
