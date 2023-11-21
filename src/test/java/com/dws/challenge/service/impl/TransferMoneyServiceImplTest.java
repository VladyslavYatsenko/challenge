package com.dws.challenge.service.impl;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.TransferMoneyException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.TransferMoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransferMoneyServiceImplTest {

    @Autowired
    private TransferMoneyService transferMoneyService;

    @MockBean
    private AccountsService accountsService;

    @BeforeEach
    public void setUp() {
        //mocking creating of an accounts
        Account accountTo = new Account("Id-123, ", BigDecimal.valueOf(100));
        Account accountFrom = new Account("Id-123, ", BigDecimal.valueOf(200));
        when(accountsService.getAccount("Id-123")).thenReturn(accountTo);
        when(accountsService.getAccount("Id-321")).thenReturn(accountFrom);

    }

    @Test
    void shouldPerformTransferMoneyFromOneAccountToAnother() {
        Account accountFrom = new Account("Id-123", BigDecimal.valueOf(100));
        Account accountTo = new Account("Id-321", BigDecimal.valueOf(200));
        BigDecimal amount = BigDecimal.valueOf(50);

        assertDoesNotThrow(() -> transferMoneyService.transferMoney(accountFrom.getAccountId(), accountTo.getAccountId(), amount));
    }

    @Test
    void shouldThrowTransferMoneyException() {
        Account accountFrom = new Account("Id-123", BigDecimal.valueOf(100));
        Account accountTo = new Account("Id-321", BigDecimal.valueOf(200));

        BigDecimal amount = BigDecimal.valueOf(150);

        assertThrows(TransferMoneyException.class, () -> transferMoneyService.transferMoney(accountFrom.getAccountId(), accountTo.getAccountId(), amount));
    }

}