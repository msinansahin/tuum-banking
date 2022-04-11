package com.tuum.banking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.tuum.banking.DatabaseIntegration;
import com.tuum.banking.common.Country;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.service.EntityToMessageGateway;

@DatabaseIntegration
@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository subject;

    @MockBean
    EntityToMessageGateway entityToMessageGateway;

    @Sql("classpath:sql/insert-test-account.sql")
    @Test
    void findById() {
        assertThat(subject.findById(9111L)).isNotNull();
        assertThat(subject.findById(9112L)).isNotNull();
        assertThat(subject.findById(1234L)).isNull();
    }

    @Test
    void insert() {
        var account1 = new Account()
                .setCustomerId(1L)
                .setCountry(Country.EST)
                .setBalances(List.of(
                        new Balance().setCurrency(Currency.EUR).setAmount(BigDecimal.TEN),
                        new Balance().setCurrency(Currency.SEK).setAmount(BigDecimal.TEN)));
        subject.insert(account1);
        assertThat(account1.getId()).isNotNull();
    }
}
