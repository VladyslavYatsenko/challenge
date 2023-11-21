package com.dws.challenge.web;


import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class TransferMoneyControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AccountsService accountsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void prepareMockMvc() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        //mocking creating of an accounts
        Account accountTo = new Account("Id-123, ", BigDecimal.valueOf(100));
        Account accountFrom = new Account("Id-123, ", BigDecimal.valueOf(200));

        String errorId = "errorId";
        when(accountsService.getAccount(errorId)).thenThrow(new AccountNotFoundException("Account with id " + errorId + " not found"));
        when(accountsService.getAccount("Id-123")).thenReturn(accountTo);
        when(accountsService.getAccount("Id-321")).thenReturn(accountFrom);
    }


    @Test
    void performTransferFromOneAccountToAnother() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"accountFromId\": \"Id-123\",\n" +
                        "    \"accountToId\": \"Id-321\",\n" +
                        "    \"amount\" : 10\n" +
                        "}")).andExpect(status().isOk());
    }

    @Test
    void shouldNotPerformTransferFromNotExistingAccount() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"accountFromId\": \"errorId\",\n" +
                        "    \"accountToId\": \"Id-321\",\n" +
                        "    \"amount\" : 1000\n" +
                        "}")).andExpect(status().isNotFound());
    }

    @Test
    void shouldNotPerformTransferToNotExistingAccount() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"accountFromId\": \"Id-123\",\n" +
                        "    \"accountToId\": \"errorId\",\n" +
                        "    \"amount\" : 1000\n" +
                        "}")).andExpect(status().isNotFound());
    }

    @Test
    void shouldNotPerformTransferFromOneAccountToAnother() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"accountFromId\": \"Id-123\",\n" +
                        "    \"accountToId\": \"Id-321\",\n" +
                        "    \"amount\" : 1000\n" +
                        "}")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotPerformTransferWithZeroAmount() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"accountFromId\": \"Id-123\",\n" +
                        "    \"accountToId\": \"Id-321\",\n" +
                        "    \"amount\" : 0\n" +
                        "}")).andExpect(status().isBadRequest());
    }
}
