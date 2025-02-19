package com.receipt.processor.model;

import lombok.Data;

import java.util.List;
@Data
public class Receipt {
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private String total;
    private List<Item> items;
}
