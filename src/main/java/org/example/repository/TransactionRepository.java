package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
// commit test line

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Count transactions for account in a time window (we store timestamp as string, so we will compare lexicographically
    // if using ISO_LOCAL_DATE_TIME format. If not, convert to appropriate type.)
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.senderAccount = :acc AND t.timestamp >= :from AND t.timestamp <= :to")
    long countBySenderAccountBetween(@Param("acc") String senderAccount,
                                     @Param("from") String from,
                                     @Param("to") String to);

    // Count failed transactions for account in a time window
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.senderAccount = :acc AND t.successStatus = false AND t.timestamp >= :from AND t.timestamp <= :to")
    long countFailedBySenderBetween(@Param("acc") String senderAccount,
                                    @Param("from") String from,
                                    @Param("to") String to);

    // Get last transaction for account (ordered by id)
    List<Transaction> findTop1BySenderAccountOrderByIdDesc(String senderAccount);

    // Get transactions for sender in last N minutes (timestamp stored as ISO String)
    @Query("SELECT t FROM Transaction t WHERE t.senderAccount = :acc AND t.timestamp >= :from")
    List<Transaction> findRecentBySender(@Param("acc") String senderAccount, @Param("from") String from);

    // Find last transaction for account within 90 days for device compare
    @Query("SELECT t FROM Transaction t WHERE t.senderAccount = :acc AND t.timestamp >= :from ORDER BY t.id DESC")
    List<Transaction> findBySenderSince(@Param("acc") String senderAccount, @Param("from") String from);

}
