package com.example.cryptowallet.service;


import com.example.cryptowallet.entity.*;
import com.example.cryptowallet.repository.TransactionRepository;
import com.example.cryptowallet.repository.UserRepository;
import com.example.cryptowallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;


    public List<Wallet> getWalletsByUser(String username) {
        return walletRepository.findByUserUsername(username);
    }



    public Wallet createWallet(UUID userId, String currency){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (walletRepository.existsByUserIdAndCurrencySymbol(userId,currency)){
            throw new RuntimeException("Wallet for this currency already exists");
        }

        Wallet wallet = Wallet.builder()
                .user(user)
                .currencySymbol(currency)
                .balance(BigDecimal.ZERO)
                .address(generateFakeAddress(currency))
                .build();

        return walletRepository.save(wallet);
    }


    @Transactional
    public Transaction deposit(String toAddress, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException("Amount must be positive");
        }


        Wallet wallet = walletRepository.findByAddress(toAddress)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));


        wallet.setBalance(wallet.getBalance().add((amount)));
        walletRepository.save(wallet);


        Transaction transaction = Transaction.builder()
                .toAddress(toAddress)
                .fromAddress(null)
                .amount(amount)
                .currencySymbol(wallet.getCurrencySymbol())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);



    }

    @Transactional
    public Transaction transfer(String fromAddr, String toAddr, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<0) {
            throw new RuntimeException("Amount must be positive");
        }

        Wallet fromWallet = walletRepository.findByAddressWithLock(fromAddr)
                .orElseThrow(() -> new RuntimeException("Sender Wallet Not Found!"));

        Wallet toWallet = walletRepository.findByAddressWithLock(toAddr)
                .orElseThrow(()-> new RuntimeException("Reciever wallet not Found!"));


        if(!fromWallet.getCurrencySymbol().equals(toWallet.getCurrencySymbol())){
            throw new RuntimeException("Currency Missmatch");
        }

        if(fromWallet.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient funds");
        }


        Transaction transaction = Transaction.builder()
                .fromAddress(fromAddr)
                .toAddress(toAddr)
                .amount(amount)
                .currencySymbol(fromWallet.getCurrencySymbol())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    private String generateFakeAddress(String currency){
        return currency + "_" + UUID.randomUUID().toString().replace("-","").substring(0,16);
    }

    public Page<Transaction> getTransactionHistory(String address, int page, int size) {
        // 1. ვამოწმებთ არსებობს თუ არა საფულე
        if (walletRepository.findByAddress(address).isEmpty()) {
            throw new RuntimeException("Wallet not found");
        }

        // 2. ვქმნით PageRequest ობიექტს
        Pageable pageable = PageRequest.of(page, size);

        // 3. გადავცემთ მესამე პარამეტრად pageable-ს
        return transactionRepository.findByFromAddressOrToAddressOrderByCreatedAtDesc(
                address,
                address,
                pageable
        );
    }

    public Page<Transaction> getTransactionHistoryPaged(String address, int page, int size) {
        return transactionRepository.findByFromAddressOrToAddressOrderByCreatedAtDesc(
                address, address, PageRequest.of(page, size));
    }


}
