package org.example.controller;

import org.example.dto.TransactionDashboardDTO;
import org.example.dto.TransactionSimulateRequest;
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

    // ===============================
    // SUMMARY (DASHBOARD)
    // ===============================
    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return transactionService.summary();
    }

    // ===============================
    // ALL TRANSACTIONS (DTO)
    // ===============================
    @GetMapping
    public List<TransactionDashboardDTO> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionDashboardDTO::new)
                .toList();
    }

    // ===============================
    // FILTER TRANSACTIONS
    // ===============================
    @GetMapping("/filter")
    public List<TransactionDashboardDTO> filter(
            @RequestParam(required = false) String senderAccount,
            @RequestParam(required = false) String receiverAccount,
            @RequestParam(required = false) String fraudStatus,
            @RequestParam(required = false) String status
    ) {
        return transactionService
                .filterData(senderAccount, receiverAccount, fraudStatus, status)
                .stream()
                .map(TransactionDashboardDTO::new)
                .toList();
    }

    // ===============================
    // MANUAL TRANSACTION SIMULATION
    // ===============================
    @PostMapping("/simulate")
    public TransactionDashboardDTO simulate(
            @RequestBody TransactionSimulateRequest request) {

        Transaction tx = transactionService.simulate(request);
        return new TransactionDashboardDTO(tx);
    }
}
