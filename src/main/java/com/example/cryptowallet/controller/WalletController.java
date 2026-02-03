package com.example.cryptowallet.controller;


import com.example.cryptowallet.dto.CreateWalletRequest;
import com.example.cryptowallet.dto.DepositRequest;
import com.example.cryptowallet.dto.TransferRequest;
import com.example.cryptowallet.dto.WalletResponse;
import com.example.cryptowallet.entity.Transaction;
import com.example.cryptowallet.entity.Wallet;
import com.example.cryptowallet.repository.WalletRepository;
import com.example.cryptowallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletRepository walletRepository;
    private final WalletService walletService;


    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request.userId(), request.currency());

        // Entity-ს პირდაპირ დაბრუნება არ არის კარგი პრაქტიკა, ვაკონვერტირებთ DTO-ში
        WalletResponse response = new WalletResponse(
                wallet.getId(),
                wallet.getAddress(),
                wallet.getCurrencySymbol(),
                wallet.getBalance()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositRequest request){
        Transaction tx = walletService.deposit(request.address(), request.amount());
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request){
        return ResponseEntity.ok(walletService.transfer(
                request.fromAddress(),
                request.toAddress(),
                request.amount()
        ));
    }


    @GetMapping("/{address}/transactions")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String address){
        return ResponseEntity.ok(walletService.getTransactionHistory(address));
    }

    @GetMapping("/{address}/history")
    public ResponseEntity<Page<Transaction>> getHistory(
            @PathVariable String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(walletService.getTransactionHistoryPaged(address, page, size));
    }



}
