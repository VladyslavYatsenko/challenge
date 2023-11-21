package com.dws.challenge.web;


import com.dws.challenge.domain.TransferMoneyRequest;
import com.dws.challenge.service.TransferMoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
@Slf4j
public class TransferMoneyController {

    private final TransferMoneyService transferMoneyService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestBody @Valid TransferMoneyRequest transferMoneyRequest) {
        log.info("Performing transfer for account {}", transferMoneyRequest.getAccountFromId());
        transferMoneyService.transferMoney(transferMoneyRequest.getAccountFromId(), transferMoneyRequest.getAccountToId(), transferMoneyRequest.getAmount());
        return new ResponseEntity<>("Money transferred successfully", HttpStatus.OK);
    }
}
