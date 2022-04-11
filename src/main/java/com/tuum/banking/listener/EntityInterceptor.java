package com.tuum.banking.listener;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import com.tuum.banking.model.domain.AbstractEntity;
import com.tuum.banking.service.EntityToMessageGateway;
import com.tuum.banking.service.SessionProvider;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class EntityInterceptor implements Interceptor {

    private final SessionProvider sessionProvider;
    private final EntityToMessageGateway entityToMessageGateway;

    @SneakyThrows
    @Override
    public Object intercept(Invocation invocation) {
        var sqlCommandType = getSqlCommentTypeFromInvocation(invocation);
        var entityOptional = findEntityThroughArgs(invocation.getArgs());
        if (SqlCommandType.INSERT == sqlCommandType) {
            entityOptional.ifPresent(entity -> entity.setCreatedBy(sessionProvider.getUserWhoSignedIn()));
        } else if (SqlCommandType.UPDATE == sqlCommandType) {
            log.warn("ignored updated by and updated created for demo, could be set last updated fields");
        }
        var returnObject = invocation.proceed();
        entityOptional.ifPresent(entity -> entityToMessageGateway.sendEntity(sqlCommandType, entity));
        return returnObject;
    }

    protected SqlCommandType getSqlCommentTypeFromInvocation(Invocation invocation) {
        var statement = (MappedStatement) invocation.getArgs()[0];
        return statement.getSqlCommandType();
    }

    protected Optional<AbstractEntity> findEntityThroughArgs(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return Optional.empty();
        }
        return Stream.of(args)
                .filter(AbstractEntity.class::isInstance)
                .findFirst()
                .map(AbstractEntity.class::cast);
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

}
