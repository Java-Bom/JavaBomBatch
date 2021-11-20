package com.javabom.definitiveguide.chap7.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String addressNumber;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public Customer(String firstName, String middleInitial, String lastName, String addressNumber,
                    String street, String city, String state, String zipCode) {
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.addressNumber = addressNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
}
