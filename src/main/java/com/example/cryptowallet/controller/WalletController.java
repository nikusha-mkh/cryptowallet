package com.example.cryptowallet.controller;


import com.example.cryptowallet.dto.CreateWalletRequest;
import com.example.cryptowallet.dto.DepositRequest;
import com.example.cryptowallet.dto.TransferRequest;
import com.example.cryptowallet.dto.WalletResponse;
import com.example.cryptowallet.entity.Transaction;
import com.example.cryptowallet.entity.Wallet;
import com.example.cryptowallet.repository.WalletRepository;
import com.example.cryptowallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletRepository walletRepository;
    private final WalletService walletService;


    @GetMapping("/my-wallets")
    public ResponseEntity<List<Wallet>> getUserWallets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(walletService.getWalletsByUser(userDetails.getUsername()));
    }


    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request.userId(), request.currency());


        WalletResponse response = new WalletResponse(
                wallet.getId(),
                wallet.getAddress(),
                wallet.getCurrencySymbol(),
                wallet.getBalance()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositRequest request){
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


    @GetMapping("/{address}/history")
    public ResponseEntity<Page<Transaction>> getHistory(
            @PathVariable String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(walletService.getTransactionHistoryPaged(address, page, size));
    }



}
