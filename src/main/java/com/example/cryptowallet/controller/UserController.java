package com.example.cryptowallet.controller;


import com.example.cryptowallet.dto.CreateUserRequest;
import com.example.cryptowallet.entity.User;
import com.example.cryptowallet.repository.UserRepository;
import com.example.cryptowallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody CreateUserRequest request){
        return ResponseEntity.ok(userService.createUser(request.username(), request.password(), request.email())); // <--- აქ აკლია მესამე!);
    }

    @GetMapping
    public ResponseEntity<List<User>>getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
