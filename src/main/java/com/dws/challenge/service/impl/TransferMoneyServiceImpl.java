package com.dws.challenge.service.impl;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.TransferMoneyException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.TransferMoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransferMoneyServiceImpl implements TransferMoneyService {

    private final AccountsService accountsService;

    private final NotificationService notificationService;

    @Async
    @Override
    //@Transactional(isolation = Isolation.SERIALIZABLE) TODO to use transactional need to add spring tx or spring-data-jpa-starter
    public CompletableFuture<Void> transferMoney(String accountFromId, String accountToId, BigDecimal amount) {
        Account accountFrom = accountsService.getAccount(accountFromId);
        Account accountTo = accountsService.getAccount(accountToId);

        if (accountFrom.getBalance().compareTo(amount) >= 0) {
            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            notificationService.notifyAboutTransfer(accountFrom, "Money transfer was performed to Account: " + accountTo.getAccountId() + ", Amount: "+  amount);

            accountTo.setBalance(accountTo.getBalance().add(amount));
            notificationService.notifyAboutTransfer(accountTo, "Money transfer was performed from Account: " + accountFrom.getAccountId() + ", Amount: "+  amount);
        } else {
            throw new TransferMoneyException("Cannot perform transfer, please check your balance");
        }
        return CompletableFuture.completedFuture(null);
    }
}
