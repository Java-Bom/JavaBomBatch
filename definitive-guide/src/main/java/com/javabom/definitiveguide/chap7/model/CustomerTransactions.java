package com.javabom.definitiveguide.chap7.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerTransactions {

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private List<Transaction> transactionList;

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(firstName);
        output.append(" ");
        output.append(middleInitial);
        output.append(". ");
        output.append(lastName);

        if (transactionList != null && transactionList.size() > 0) {
            output.append(" has ");
            output.append(transactionList.size());
            output.append(" transactions.");
        } else {
            output.append(" has no transactions.");
        }

        return output.toString();
    }
}
