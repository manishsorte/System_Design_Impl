package com.system.design.system.design.impl.service;

import com.system.design.system.design.impl.entity.User;
import com.system.design.system.design.impl.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImpl{

    @Autowired
    private UserRepository userRepository;

    // ✅ Create User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Read - Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Read - Get user by ID
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // ✅ Update User
    public User updateUser(int id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setBalance(updatedUser.getBalance());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // ✅ Delete User
    public void deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
