package org.example.controller;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService,
                                 TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    // ✅ ADD THIS METHOD (ONLY THIS IS NEW)
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction tx) {
        return transactionService.evaluateAndSave(tx);
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return transactionService.summary();
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Transaction> filter(
            @RequestParam(required = false) String senderAccount,
            @RequestParam(required = false) String receiverAccount,
            @RequestParam(required = false) String fraudStatus,
            @RequestParam(required = false) String status
    ) {
        return transactionService.filterData(
                senderAccount, receiverAccount, fraudStatus, status
        );
    }
}
