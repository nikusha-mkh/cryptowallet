package com.example.cryptowallet.repository;

import com.example.cryptowallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.address = :address")
    Optional<Wallet> findByAddressWithLock(String address);

    Optional<Wallet> findByAddress(String address);

    boolean existsByUserIdAndCurrencySymbol(UUID id, String currencySymbol);
}

