package com.example.omnianalytic.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "audit-logs")
public class AuditLog {
    @Id
    private String id;
    private String message;
    private String severity;
}
