package com.javabom.definitiveguide.chap7.service;


import com.javabom.service.domain.customer.entity.CustomerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerStubService {
    private List<CustomerEntity> customers;
    private int curIndex;
    private Random generator = new Random();

    private final String[] firstNames = {"찬인", "기현", "나단", "민형", "재연", "민정", "유성"};
    private final String middleInitial = "ABCDEFG";
    private final String[] lastNames = {"박", "방", "홍", "최", "이", "김", "서"};
    private final String[] streets = {"1", "2", "3", "4", "5", "6", "7"};
    private final String[] cities = {"서인천", "동인천", "석촌", "파주", "광명", "디엠씨", "일산"};
    private final String[] states = {"IL", "NY", "CA", "NE"};

    public CustomerStubService() {
        curIndex = 0;
        customers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            customers.add(buildCustomer((long) i));
        }
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int curIndex) {
        this.curIndex = curIndex;
    }

    private CustomerEntity buildCustomer(Long id) {
        return CustomerEntity.builder()
                .id(id)
                .firstName(firstNames[generator.nextInt(firstNames.length - 1)])
                .middleInitial(String.valueOf(middleInitial.charAt(generator.nextInt(middleInitial.length() - 1))))
                .lastName(lastNames[generator.nextInt(lastNames.length - 1)])
                .address(generator.nextInt(999) + " " + streets[generator.nextInt(streets.length - 1)])
                .city(cities[generator.nextInt(cities.length - 1)])
                .state(states[generator.nextInt(states.length - 1)])
                .zipCode(String.valueOf(generator.nextInt(999)))
                .build();
    }

    public CustomerEntity getCustomer() {
        CustomerEntity customerEntity = null;

        if (curIndex < customers.size()) {
            customerEntity = customers.get(curIndex);
            curIndex++;
        }

        return customerEntity;
    }
}
