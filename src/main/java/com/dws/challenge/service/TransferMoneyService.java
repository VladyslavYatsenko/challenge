package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface TransferMoneyService {

  CompletableFuture<Void> transferMoney(String accountFromId, String accountToId, BigDecimal amount);

}
