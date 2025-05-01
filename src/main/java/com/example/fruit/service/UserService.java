package com.example.fruit.service;

import com.example.fruit.model.User;
import com.example.fruit.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    public void save(User user) {
        repo.save(user);
    }

    public List<User> findAll() {
        return repo.findAll();
    }
}
