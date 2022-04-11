package com.tuum.banking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.tuum.banking.DatabaseIntegration;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Balance;

@DatabaseIntegration
@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository subject;

    @Sql("classpath:sql/insert-test-account.sql")
    @Test
    void insert() {
        var balance = new Balance()
                .setAccountId(9112L)
                .setAmount(BigDecimal.valueOf(1500))
                .setCurrency(Currency.EUR);
        subject.insert(balance);
        assertThat(balance.getId()).isNotNull();
    }

    @Sql("classpath:sql/insert-test-balance.sql")
    @Test
    void findBalance() {
        var test = subject.findBalance(9211L, Currency.EUR);
        assertThat(test.getAccountId()).isEqualTo(9211L);
    }
}
