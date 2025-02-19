package com.receipt.processor.service;
import com.receipt.processor.model.Receipt;
import java.util.Map;
public interface ReceiptService {
    Map<String, String> processReceipt(Receipt receipt);
    Map<String, Integer> getPoints(String id);
}
