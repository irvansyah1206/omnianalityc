package com.example.omnianalytic.repository;

import com.example.omnianalytic.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query(value = """
        SELECT merchant_name, total_sales,
               DENSE_RANK() OVER (ORDER BY total_sales DESC) as rank_position
        FROM (
            SELECT merchant_name, SUM(amount) as total_sales
            FROM transactions
            GROUP BY merchant_name
        ) sales_data
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTopMerchants();
}
