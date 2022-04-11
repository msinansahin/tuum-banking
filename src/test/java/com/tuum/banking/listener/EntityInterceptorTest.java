package com.tuum.banking.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.model.domain.Account;
import com.tuum.banking.service.EntityToMessageGateway;
import com.tuum.banking.service.SessionProvider;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class EntityInterceptorTest {

    @Spy
    @InjectMocks
    private EntityInterceptor subject;

    @Mock private EntityToMessageGateway entityToMessageGateway;
    @Mock private SessionProvider sessionProvider;

    final Object returnedObj = new Object();

    @SneakyThrows
    @Test
    void intercept__insert() {
        var invocation = mock(Invocation.class);
        var account = mock(Account.class);
        when(sessionProvider.getUserWhoSignedIn()).thenReturn("test");
        when(invocation.getArgs()).thenReturn(new Object[] { 1 });
        doReturn(SqlCommandType.INSERT).when(subject).getSqlCommentTypeFromInvocation(invocation);
        doReturn(Optional.of(account)).when(subject).findEntityThroughArgs(new Object[] { 1 });
        when(invocation.proceed()).thenReturn(returnedObj);
        doNothing().when(entityToMessageGateway).sendEntity(SqlCommandType.INSERT, account);

        var test = subject.intercept(invocation);
        assertThat(test).isSameAs(returnedObj);
        verify(entityToMessageGateway).sendEntity(SqlCommandType.INSERT, account);
        verify(account).setCreatedBy("test");
    }

    @SneakyThrows
    @Test
    void intercept__update() {
        var invocation = mock(Invocation.class);
        var account = mock(Account.class);
        when(invocation.getArgs()).thenReturn(new Object[] { 1 });
        doReturn(SqlCommandType.UPDATE).when(subject).getSqlCommentTypeFromInvocation(invocation);
        doReturn(Optional.of(account)).when(subject).findEntityThroughArgs(new Object[] { 1 });
        when(invocation.proceed()).thenReturn(returnedObj);
        doNothing().when(entityToMessageGateway).sendEntity(SqlCommandType.UPDATE, account);

        var test = subject.intercept(invocation);
        assertThat(test).isSameAs(returnedObj);
        verify(entityToMessageGateway).sendEntity(SqlCommandType.UPDATE, account);
    }
}
