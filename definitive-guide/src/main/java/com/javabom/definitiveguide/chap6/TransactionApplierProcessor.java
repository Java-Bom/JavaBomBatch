package com.javabom.definitiveguide.chap6;

import com.javabom.definitiveguide.chap6.domain.AccountSummary;
import com.javabom.definitiveguide.chap6.domain.Transaction;
import com.javabom.definitiveguide.chap6.domain.TransactionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class TransactionApplierProcessor implements ItemProcessor<AccountSummary, AccountSummary> {

    private TransactionDao transactionDao;

    public TransactionApplierProcessor(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public AccountSummary process(AccountSummary accountSummary) throws Exception {
        List<Transaction> transactions = transactionDao.getTransactionsByAccountNumber(accountSummary.getAccountNumber());
        for (Transaction transaction : transactions) {
            accountSummary.setCurrentBalance(accountSummary.getCurrentBalance() + transaction.getAmount());
        }
        return accountSummary;
    }
}
