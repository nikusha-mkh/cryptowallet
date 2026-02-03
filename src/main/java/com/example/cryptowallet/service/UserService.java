package com.example.cryptowallet.service;

import com.example.cryptowallet.entity.User;
import com.example.cryptowallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String email){
        User user = User.builder()
                .username(username)
                .email(email)
                .password("dummy_hashed_password")
                .build();

        return userRepository.save(user);
    }
}
