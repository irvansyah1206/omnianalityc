package com.example.omnianalytic.controller;

import com.example.omnianalytic.model.AuditLog;
import com.example.omnianalytic.model.dto.TransactionRequest;
import com.example.omnianalytic.repository.AuditLogRepository;
import com.example.omnianalytic.repository.TransactionRepository;
import com.example.omnianalytic.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalyticsController {

    private final IngestionService ingestionService;
    private final TransactionRepository transactionRepo;
    private final AuditLogRepository auditRepo;

    // 1. Ingest Data (Producer)
    @PostMapping("/transactions")
    public String ingest(@RequestBody List<TransactionRequest> requests) {
        ingestionService.processBulkRequest(requests);
        return "Processing " + requests.size() + " transactions.";
    }

    // 2. Report (Native SQL)
    @GetMapping("/top-merchants")
    public List<Object[]> getTopMerchants() {
        return transactionRepo.findTopMerchants();
    }

    // 3. Search Logs (Elasticsearch)
    @GetMapping("/logs")
    public Iterable<AuditLog> searchLogs(@RequestParam String query) {
        return auditRepo.findByMessageContaining(query);
    }
}