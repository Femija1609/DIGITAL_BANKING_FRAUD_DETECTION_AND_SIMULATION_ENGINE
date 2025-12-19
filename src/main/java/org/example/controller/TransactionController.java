package org.example.controller;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
// commit test line

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService,
                                 TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    // ----------------------------
    // POST → Create Transaction
    // ----------------------------
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction tx) {
        Transaction saved = transactionService.evaluateAndSave(tx);
        return ResponseEntity.ok(saved);
    }

    // ----------------------------
    // GET ALL TRANSACTIONS
    // ----------------------------
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    // ----------------------------
    // ⭐ NEW GET FILTER API
    // ----------------------------
    @GetMapping("/filter")
    public ResponseEntity<?> filterTransactions(
            @RequestParam(required = false) String senderAccount,
            @RequestParam(required = false) String receiverAccount,
            @RequestParam(required = false) String fraudStatus,
            @RequestParam(required = false) String status
    ) {
        List<Transaction> result = transactionService.filterData(
                senderAccount, receiverAccount, fraudStatus, status
        );

        return ResponseEntity.ok(result);
    }

    // ----------------------------
    // ⭐ NEW SUMMARY API
    // ----------------------------
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> transactionSummary() {
        return ResponseEntity.ok(transactionService.summary());
    }
}
