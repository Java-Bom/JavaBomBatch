package com.javabom.definitiveguide.chap8;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UpperCaseNameService {

    public Customer upperCase(Customer source){
        Customer destination = new Customer();
        destination.setFirstName(source.getFirstName().toUpperCase(Locale.ROOT));
        destination.setLastName(source.getLastName().toUpperCase(Locale.ROOT));
        return destination;
    }
}
