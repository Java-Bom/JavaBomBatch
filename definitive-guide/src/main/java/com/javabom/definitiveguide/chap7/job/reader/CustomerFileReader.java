package com.javabom.definitiveguide.chap7.job.reader;

import com.javabom.definitiveguide.chap7.model.CustomerTransactions;
import com.javabom.definitiveguide.chap7.model.Transaction;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import java.util.ArrayList;

public class CustomerFileReader implements ResourceAwareItemReaderItemStream<CustomerTransactions> {

    private Object curItem = null;
    private ResourceAwareItemReaderItemStream<Object> delegate;

    public CustomerFileReader(ResourceAwareItemReaderItemStream<Object> delegate) {
        this.delegate = delegate;
    }

    /***
     * 파일에서 고객 레코드를 읽음
     * 다음 고객 레코드를 만나기 전까지 현재 처리 중인 고객 레코드와 관련된 거래 내역 레코드를 한 줄씩 읽어들임
     * 고객 레코드를 발견하면 현재 처리 중인 고객의 레코드의 처리가 끝난 것으로 간주해 커스텀 ItemReader로 반환
     */
    @Override
    public CustomerTransactions read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (curItem == null) {
            curItem = delegate.read();
        }
        CustomerTransactions item = (CustomerTransactions) curItem;
        curItem = null;

        if (item != null) {
            item.setTransactionList(new ArrayList<>());

            while (peek() instanceof Transaction) {
                item.getTransactionList().add((Transaction) curItem);
                curItem = null;
            }
        }
        return item;
    }

    /***
     * 현재 레코드를 캐시(curItem)에 저장
     */
    private Object peek() throws Exception {
        if (curItem == null) {
            curItem = delegate.read();
        }
        return curItem;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    @Override
    public void setResource(Resource resource) {
        this.delegate.setResource(resource);
    }
}
