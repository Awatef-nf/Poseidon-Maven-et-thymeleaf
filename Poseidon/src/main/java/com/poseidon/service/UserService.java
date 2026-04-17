package com.poseidon.service;

import com.poseidon.domain.User;
import com.poseidon.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Object findAll() {
       return userRepository.findAll();
    }

    public void addUser(@Valid User user) {
        userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ClassCastException("Invalid user Id: " + id));
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
