package com.tuum.banking.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.util.PageRequest;

@Mapper
public interface TransactionRepository {

    @Insert("""
            INSERT INTO transaction(account_id, amount, currency, direction, description, created_by)
            VALUES (#{accountId}, #{amount}, #{currency}::currency_enum, #{direction}::direction_enum, #{description}, #{createdBy})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(Transaction transaction);

    @Select("""
            SELECT * FROM transaction
            WHERE account_id = #{accountId}
            ORDER BY id desc
            LIMIT #{pageRequest.size} OFFSET #{pageRequest.offset}
            """)
    List<Transaction> findAllByAccountId(Long accountId, PageRequest pageRequest);


    @Select("""
            SELECT count(1) FROM transaction
            WHERE account_id = #{accountId}
            """)
    Long countByAccountId(Long accountId);
}
