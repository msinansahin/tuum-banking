package com.tuum.banking.config;

import java.util.Optional;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.tuum.banking.listener.EntityInterceptor;

@Configuration
public class BatisConfig {

    @Bean
    public TransactionFactory transactionFactory() {
        return new SpringManagedTransactionFactory();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, EntityInterceptor entityInterceptor) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(entityInterceptor);
        var obj = bean.getObject();
        Assert.notNull(obj, "SqlSessionFactoryBean object should be specified");
        Optional.ofNullable(obj.getConfiguration())
                .ifPresent(factory -> factory.setMapUnderscoreToCamelCase(true));
        return obj;
    }

}
