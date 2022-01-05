package com.basic.transactions.management.model.dto;

import lombok.Data;

@Data
public class SimpleTransferContainer {
    private String noRekSource;
    private String noRekDestination;
    private Double money;
}
