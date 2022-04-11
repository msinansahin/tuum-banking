package com.tuum.banking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.tuum.banking.DatabaseIntegration;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.service.EntityToMessageGateway;
import com.tuum.banking.util.PageRequest;

@DatabaseIntegration
@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository subject;

    @MockBean
    EntityToMessageGateway entityToMessageGateway;

    @Sql("classpath:sql/insert-test-balance.sql")
    @Test
    void insert() {
        var transaction = new Transaction()
                .setAccountId(9211L)
                .setAmount(BigDecimal.TEN)
                .setDirection(Transaction.Direction.IN)
                .setCurrency(Currency.EUR)
                .setDescription("desc");
        subject.insert(transaction);
        assertThat(transaction.getId()).isNotNull().isPositive();
    }

    @Sql("classpath:sql/insert-test-balance.sql")
    @Test
    void findAllByAccountId() {
        insertTransactions(15);
        var testFirst = subject.findAllByAccountId(9211L, new PageRequest().setPage(0).setSize(7));
        assertThat(testFirst.size()).isEqualTo(7);
        var testSecond = subject.findAllByAccountId(9211L, new PageRequest().setPage(1).setSize(7));
        assertThat(testSecond.size()).isEqualTo(7);
        var testThird = subject.findAllByAccountId(9211L, new PageRequest().setPage(2).setSize(7));
        assertThat(testThird.size()).isEqualTo(1);
        var testFourth = subject.findAllByAccountId(9211L, new PageRequest().setPage(3).setSize(7));
        assertThat(testFourth).isEmpty();
    }

    @Sql("classpath:sql/insert-test-balance.sql")
    @Test
    void countByAccountId() {
        insertTransactions(15);
        assertThat(subject.countByAccountId(9211L)).isEqualTo(15);
        assertThat(subject.countByAccountId(9999999L)).isZero();
    }

    private void insertTransactions(int count) {
        for (int i = 0; i < count ; i++) {
            var transaction = new Transaction()
                    .setAccountId(9211L)
                    .setAmount(BigDecimal.TEN)
                    .setDirection(Transaction.Direction.IN)
                    .setCurrency(Currency.EUR)
                    .setDescription("desc" + i);
            subject.insert(transaction);
        }
    }

}
