package com.tuum.banking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    Queue queueAccount(BankingProperties bankingProperties) {
        return new Queue(bankingProperties.getAccount().getQueueName(), true);
    }

    @Bean
    Queue queueTransaction(BankingProperties bankingProperties) {
        return new Queue(bankingProperties.getTransaction().getQueueName(), true);
    }

    @Bean
    TopicExchange exchangeAccount(BankingProperties bankingProperties) {
        return new TopicExchange(bankingProperties.getAccount().getExchangeName());
    }


    @Bean
    TopicExchange exchangeTransaction(BankingProperties bankingProperties) {
        return new TopicExchange(bankingProperties.getTransaction().getExchangeName());
    }

    @Bean
    Binding bindingAccount(Queue queueAccount, TopicExchange exchangeAccount, BankingProperties bankingProperties) {
        return BindingBuilder.bind(queueAccount).to(exchangeAccount).with(bankingProperties.getAccount().getRouteKey());
    }

    @Bean
    Binding bindingTransaction(Queue queueTransaction, TopicExchange exchangeTransaction, BankingProperties bankingProperties) {
        return BindingBuilder.bind(queueTransaction).to(exchangeTransaction).with(bankingProperties.getTransaction().getRouteKey());
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }

}
