package com.javabom.definitiveguide.chap7.job.query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CustomerByCityQueryProvider extends AbstractJpaQueryProvider {
    private final String city;

    public CustomerByCityQueryProvider(String city) {
        this.city = city;
    }

    @Override
    public Query createQuery() {
        EntityManager entityManager = getEntityManager();

        Query query = entityManager.createQuery("SELECT c FROM customer_entity c WHERE c.city = :city");
        query.setParameter("city", city);

        return query;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(city, "도시 이름이 비어있음");
    }
}
