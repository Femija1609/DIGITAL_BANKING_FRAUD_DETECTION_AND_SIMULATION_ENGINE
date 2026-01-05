package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // =================================================
    // FRAUD RULE SUPPORT (TIME WINDOW QUERIES)
    // =================================================

    // Count transactions for sender in time window
    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE t.senderAccount = :acc
          AND t.timestamp >= :from
          AND t.timestamp <= :to
    """)
    long countBySenderAccountBetween(
            @Param("acc") String senderAccount,
            @Param("from") String from,
            @Param("to") String to
    );

    // Count failed transactions for sender in time window
    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE t.senderAccount = :acc
          AND t.status = 'FAILED'
          AND t.timestamp >= :from
          AND t.timestamp <= :to
    """)
    long countFailedBySenderBetween(
            @Param("acc") String senderAccount,
            @Param("from") String from,
            @Param("to") String to
    );

    // Find latest transaction for sender
    List<Transaction> findTop1BySenderAccountOrderByIdDesc(String senderAccount);

    // Transactions since a given timestamp (device rule)
    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.senderAccount = :acc
          AND t.timestamp >= :from
        ORDER BY t.id DESC
    """)
    List<Transaction> findBySenderSince(
            @Param("acc") String senderAccount,
            @Param("from") String from
    );

    // =================================================
    // SUMMARY DASHBOARD SUPPORT
    // =================================================

    long countByStatus(String status);

    long countByFraudStatus(String fraudStatus);
}
