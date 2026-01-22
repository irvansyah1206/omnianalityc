package com.example.omnianalytic.repository;

import com.example.omnianalytic.model.AuditLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AuditLogRepository extends ElasticsearchRepository<AuditLog, String> {
    // Spring Data Elastic otomatis membuat implementasi pencarian
    List<AuditLog> findByMessageContaining(String message);
}
