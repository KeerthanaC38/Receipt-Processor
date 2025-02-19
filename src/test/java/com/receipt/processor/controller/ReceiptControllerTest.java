package com.receipt.processor.controller;

import com.receipt.processor.model.Item;
import com.receipt.processor.model.Receipt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPoints() throws Exception {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Walgreens");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("08:13");
        receipt.setTotal("2.65");
        receipt.setItems(Arrays.asList(
                new Item("Pepsi - 12-oz", "1.25"),
                new Item("Dasani", "1.40")
        ));
        String postResponse = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(receipt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        String receiptId = objectMapper.readTree(postResponse).get("id").asText();

        System.out.println("Receipt ID from POST: " + postResponse);

        String pointsResponse = mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Points for receipt ID " + receiptId + ": " + pointsResponse);
    }
}
