package com.example.cryptowallet.repository;

import com.example.cryptowallet.entity.Transaction;
import org.springframework.data.domain.Page;         // <--- Import
import org.springframework.data.domain.Pageable;     // <--- Import
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // ეს ჩაამატე აქ:
    Page<Transaction> findByFromAddressOrToAddressOrderByCreatedAtDesc(
            String fromAddress,
            String toAddress,
            Pageable pageable
    );
}