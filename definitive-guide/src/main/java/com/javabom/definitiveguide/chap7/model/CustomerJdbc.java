package com.javabom.definitiveguide.chap7.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerJdbc {

    private Long id;

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    @Builder
    public CustomerJdbc(Long id, String firstName, String middleInitial, String lastName, String address, String street, String city, String state, String zipCode) {
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
