package com.example.cryptowallet.controller;


import com.example.cryptowallet.dto.CreateUserRequest;
import com.example.cryptowallet.entity.User;
import com.example.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.ok(userService.createUser(request.username(),request.email()));
    }
}
