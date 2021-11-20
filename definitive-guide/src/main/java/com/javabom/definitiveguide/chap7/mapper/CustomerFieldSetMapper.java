package com.javabom.definitiveguide.chap7.mapper;

import com.javabom.definitiveguide.chap7.model.CustomerAddress;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CustomerFieldSetMapper implements FieldSetMapper<CustomerAddress> {

    @Override
    public CustomerAddress mapFieldSet(FieldSet fieldSet) {
        CustomerAddress customer = new CustomerAddress();

        customer.setAddress(fieldSet.readString("addressNumber") +
                " " + fieldSet.readString("street"));
        customer.setCity(fieldSet.readString("city"));
        customer.setFirstName(fieldSet.readString("firstName"));
        customer.setLastName(fieldSet.readString("lastName"));
        customer.setMiddleInitial(fieldSet.readString("middleInitial"));
        customer.setState(fieldSet.readString("state"));
        customer.setZipCode(fieldSet.readString("zipCode"));

        return customer;
    }
}
