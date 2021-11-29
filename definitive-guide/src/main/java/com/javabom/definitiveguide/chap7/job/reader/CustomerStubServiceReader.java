package com.javabom.definitiveguide.chap7.job.reader;


import com.javabom.definitiveguide.chap7.service.CustomerStubService;
import com.javabom.service.domain.customer.entity.CustomerEntity;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamSupport;

public class CustomerStubServiceReader extends ItemStreamSupport implements ItemReader<CustomerEntity> {
    private final CustomerStubService delegate;
    private static final String INDEX_KEY = "current.index.customer";

    public CustomerStubServiceReader() {
        delegate = new CustomerStubService();
    }

    @Override
    public CustomerEntity read() {
        return delegate.getCustomer();
    }

    @Override
    public void open(ExecutionContext executionContext) {
        delegate.setCurIndex(0);

        if (executionContext.containsKey(INDEX_KEY)) {
            int index = executionContext.getInt(getExecutionContextKey(INDEX_KEY));
            if (index == 50) {
                delegate.setCurIndex(51);
            } else {
                delegate.setCurIndex(index);
            }
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.putInt(getExecutionContextKey(INDEX_KEY), delegate.getCurIndex());
    }

    @Override
    public void close() {
        super.close();
    }
}
