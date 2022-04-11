package com.tuum.banking.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.tuum.banking.model.domain.Account;

@Mapper
public interface AccountRepository {

    @Select("select * from account")
    List<Account> findAll();

    @Select("SELECT * FROM account WHERE id = #{id}")
    Account findById(Long id);

    @Delete("DELETE FROM account WHERE id = #{id}")
    int deleteById(Long id);

    @Insert("""
            INSERT INTO account(country, customer_id, created_by)
            VALUES (#{country}::country_enum, #{customerId}, #{createdBy});
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(Account account);

}
