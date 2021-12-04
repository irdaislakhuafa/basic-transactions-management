package com.basic.transactions.management.helpers.pagehelpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPage {
    private Integer pageNumber;
    private Integer dataSize;
    private String sortBy;
    private String sortAs;
}
