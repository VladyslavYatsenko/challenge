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

        try {
            //Setting up an order for locking
            if (accountFrom.getAccountId().compareTo(accountTo.getAccountId()) < 0) {
                accountFrom.getLock().lock(); // A
                accountTo.getLock().lock(); // B
            } else {
                accountTo.getLock().lock(); // B
                accountFrom.getLock().lock(); // A
            }

            log.info("Locks are applied");
            if (accountFrom.getBalance().compareTo(amount) >= 0) {
                accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
                accountsService.updateAccount(accountFrom);
                notificationService.notifyAboutTransfer(accountFrom, "Money transfer was performed to Account: " + accountTo.getAccountId() + ", Amount: " + amount);

                accountTo.setBalance(accountTo.getBalance().add(amount));
                accountsService.updateAccount(accountTo);
                notificationService.notifyAboutTransfer(accountTo, "Money transfer was performed from Account: " + accountFrom.getAccountId() + ", Amount: " + amount);
            } else {
                log.error("Cannot perform transfer");
                throw new TransferMoneyException("Cannot perform transfer, please check your balance");
            }
        } finally {
            if (accountFrom.getAccountId().compareTo(accountTo.getAccountId()) < 0) {
                accountTo.getLock().unlock(); // B
                accountFrom.getLock().unlock(); // A, releasing locks in reverse order to avoid deadlock
            } else {
                accountFrom.getLock().unlock(); // A, releasing locks in reverse order to avoid deadlock
                accountTo.getLock().unlock(); // B
            }

            log.info("Locks are released");
        }
    }

}
