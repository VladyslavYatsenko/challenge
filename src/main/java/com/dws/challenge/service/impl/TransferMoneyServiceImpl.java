package com.dws.challenge.service.impl;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.TransferMoneyException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.TransferMoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferMoneyServiceImpl implements TransferMoneyService {

    private final AccountsService accountsService;

    private final NotificationService notificationService;

    @Override
    public void transferMoney(String accountFromId, String accountToId, BigDecimal amount) {
        Account accountFrom = accountsService.getAccount(accountFromId);
        Account accountTo = accountsService.getAccount(accountToId);

        // Optimistic locking (checking the version of an account) if version is changed cannot perform transfer
        if (!accountFrom.getVersion().equals(accountsService.getAccount(accountFromId).getVersion()) ||
                !accountTo.getVersion().equals(accountsService.getAccount(accountToId).getVersion())) {
            throw new ConcurrentModificationException("Cannot perform transfer, Optimistic locking failed.");
        }

        if (accountFrom.getBalance().compareTo(amount) >= 0) {
            accountFrom.setVersion(accountFrom.getVersion() + 1);
            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountsService.updateAccount(accountFrom);
            notificationService.notifyAboutTransfer(accountFrom, "Money transfer was performed to Account: " + accountTo.getAccountId() + ", Amount: " + amount);

            accountTo.setVersion(accountTo.getVersion() + 1);
            accountTo.setBalance(accountTo.getBalance().add(amount));
            accountsService.updateAccount(accountTo);
            notificationService.notifyAboutTransfer(accountTo, "Money transfer was performed from Account: " + accountFrom.getAccountId() + ", Amount: " + amount);
        } else {
            log.error("Cannot perform transfer");
            throw new TransferMoneyException("Cannot perform transfer, please check your balance");
        }
    }
}
