package com.javabom.definitiveguide.chap8;

import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.HashSet;
import java.util.Set;

public class UniqueLastNameValidator implements Validator<Customer> {
    private Set<String> lastNames = new HashSet<>();

    @Override
    public void validate(Customer value) throws ValidationException {
        if(lastNames.contains(value.getLastName())){
            throw new ValidationException("중복 발생 - " + value.getLastName());
        }
        lastNames.add(value.getLastName());
    }
}
