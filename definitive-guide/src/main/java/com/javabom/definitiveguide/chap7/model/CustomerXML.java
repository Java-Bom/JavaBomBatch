package com.javabom.definitiveguide.chap7.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customer") //매칭되는 리먼트 지정
@Getter
@Setter
@NoArgsConstructor
public class CustomerXML {

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private List<Transaction> transactionList;

    @XmlElementWrapper(name = "transactions") // 감싸져 있는 앨리먼트앨 지정
    @XmlElement(name = "transaction") //컬렉션 내 각 앨리먼트 지정
    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

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
