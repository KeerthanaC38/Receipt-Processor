package com.receipt.processor.controller;
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
        if (receipt.getRetailer() == null || receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "The receipt is invalid."));
        }
        return ResponseEntity.ok(receiptService.processReceipt(receipt));
    }


    @GetMapping("/receipts/{id}/points")
    public ResponseEntity<Object> getPoints(@PathVariable String id) {
        if (id == null || id.isEmpty() || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "The receipt is invalid."));
        }
        Map<String, Integer> points = receiptService.getPoints(id);
        if (points == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No receipt found for that ID."));
        }
        return ResponseEntity.ok(points);
    }
}
