package com.example.cryptowallet.repository;

import com.example.cryptowallet.entity.Transaction;
import org.springframework.data.domain.Page;         // <--- Import
import org.springframework.data.domain.Pageable;     // <--- Import
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    // სახელი უნდა იყოს ზუსტად ასეთი, რომ Spring-მა "fromAddress" და "toAddress" იპოვოს
    Page<Transaction> findAllByFromAddressOrToAddress(String from, String to, Pageable pageable);
}