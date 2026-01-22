package com.example.omnianalytic.model.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Builder
@Document(indexName = "transactions")
public class TransactionDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String cardId;

    @Field(type = FieldType.Double)
    private BigDecimal amount;

    @Field(type = FieldType.Long)
    private Long timestamp;
}
