package com.receipt.processor.controller;
import com.receipt.processor.model.Item;
import com.receipt.processor.model.Receipt;
import com.receipt.processor.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/receipts/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt) {
        if (receipt.getRetailer() == null || receipt.getRetailer().isEmpty()
                || receipt.getItems() == null || receipt.getItems().isEmpty()
                || receipt.getTotal() == null || receipt.getTotal().isEmpty()
                || receipt.getPurchaseDate() == null || receipt.getPurchaseDate().isEmpty()
                || receipt.getPurchaseTime() == null || receipt.getPurchaseTime().isEmpty() ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "The receipt is invalid."));
        }
        for (Item item : receipt.getItems()) {
            if (item.getShortDescription() == null || item.getShortDescription().isEmpty() ||
                    item.getPrice() == null || item.getPrice().isEmpty()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Each item in the receipt must have a short description and a price."));
            }
        }
        return ResponseEntity.ok(receiptService.processReceipt(receipt));
    }


    @GetMapping("/receipts/{id}/points")
    public ResponseEntity<Object> getPoints(@PathVariable String id) {
        if (id == null || id.isEmpty() || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No receipt found for that ID"));
        }
        Map<String, Integer> points = receiptService.getPoints(id);
        if (points == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No receipt found for that ID."));
        }
        return ResponseEntity.ok(points);
    }
}
