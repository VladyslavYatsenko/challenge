package com.dws.challenge.domain;


import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferMoneyRequest {

    @NotNull
    @NotEmpty
    private String accountFromId;
    @NotNull
    @NotEmpty
    private String accountToId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount to transfer should be greater than 0")
    private BigDecimal amount;
}
