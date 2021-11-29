package com.javabom.definitiveguide.config.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Profile("hibernate")
@Component
public class HibernateBatchConfigurer extends DefaultBatchConfigurer {
    private final DataSource dataSource;
    private final SessionFactory sessionFactory;
    private PlatformTransactionManager transactionManager;

    public HibernateBatchConfigurer(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        super(dataSource);
        this.dataSource = dataSource;
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.transactionManager = new HibernateTransactionManager(this.sessionFactory);
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }
}
