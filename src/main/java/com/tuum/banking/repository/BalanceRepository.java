package com.tuum.banking.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Balance;

@Mapper
public interface BalanceRepository {


    @Select("SELECT * FROM balance")
    List<Balance> findAll();

    @Select("SELECT * FROM balance WHERE id = #{id}")
    Balance findById(Long id);

    @Insert("""
            INSERT INTO balance(account_id, amount, currency, created_by)
            VALUES (#{accountId}, #{amount}, #{currency}::currency_enum, #{createdBy})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(Balance balance);

    @Update("""
            UPDATE balance SET valid_until = CURRENT_TIMESTAMP
            WHERE id = #{balanceId}
            """)
    void expireBalance(Long balanceId);

    @Select("""
            SELECT * FROM balance
            WHERE account_id = #{accountId}
            AND currency = #{currency}::currency_enum
            AND valid_until IS NULL
            """)
    Balance findBalance(Long accountId, Currency currency);

    @Select("""
            SELECT * FROM balance
            WHERE account_id = #{accountId}
            AND valid_until IS NULL
            """)
    List<Balance> findBalancesByAccountId(Long accountId);
}
