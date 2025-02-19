package com.receipt.processor.service.impl;

import com.receipt.processor.model.Item;
import com.receipt.processor.model.Receipt;
import com.receipt.processor.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private static final Map<String, Integer> receiptStore = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> processReceipt(Receipt receipt) {
        String receiptId = UUID.randomUUID().toString();
        int points = calculatePoints(receipt);
        receiptStore.put(receiptId, points);
        return Collections.singletonMap("id", receiptId);
    }

    @Override
    public Map<String, Integer> getPoints(String id) {
        if (!receiptStore.containsKey(id)) {
            return null;
        }
        return Collections.singletonMap("points", receiptStore.getOrDefault(id, 0));
    }

    private int calculatePoints(Receipt receipt) {

        int points = receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        if (receipt.getTotal().matches("\\d+\\.00")) points += 50;
        if (Double.parseDouble(receipt.getTotal()) % 0.25 == 0) points += 25;

        points += (receipt.getItems().size() / 2) * 5;

        for (Item item : receipt.getItems()) {
            if (item.getShortDescription().trim().length() % 3 == 0) {
                points += Math.ceil(Double.parseDouble(item.getPrice()) * 0.2);
            }
        }

        if (Integer.parseInt(receipt.getPurchaseDate().split("-")[2]) % 2 == 1) points += 6;

        int hour = Integer.parseInt(receipt.getPurchaseTime().split(":")[0]);
        if (hour >= 14 && hour < 16) points += 10;

        return points;
    }
}
