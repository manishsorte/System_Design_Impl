package com.system.design.system.design.impl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.design.system.design.impl.entity.User;
import com.system.design.system.design.impl.entity.IdempotentRecords;
import com.system.design.system.design.impl.repository.IdempotencyRepository;
import com.system.design.system.design.impl.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdempotencyRepository idempotencyRepository;

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
    public User updateUser(int id, String idempotencyKey, User updatedUser) throws JsonProcessingException {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());

            //Check Idempotency key is available in DB
            Optional<IdempotentRecords> existingIdempotencyKey = idempotencyRepository.findById(idempotencyKey);
            if (existingIdempotencyKey.isPresent()) {
                System.out.println("Duplicate request detected. Returning previous response...");
                existingUser.setBalance(retrieveIdempotentDataResponse(existingIdempotencyKey).getBalance());
            }
            else {
                saveIdempotentDataResponse(updatedUser,idempotencyKey);
                existingUser.setBalance(updatedUser.getBalance());
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public User retrieveIdempotentDataResponse(Optional<IdempotentRecords> record) throws JsonProcessingException {
        // Retrieve
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(record.get().getResponseData(), User.class); // deserialize JSON -> User
    }

    public void saveIdempotentDataResponse(User user,String key) throws JsonProcessingException {
        // Save
        ObjectMapper mapper = new ObjectMapper();
        IdempotentRecords record = new IdempotentRecords();
        record.setIdemKey(key);
        record.setResponseData(mapper.writeValueAsString(user)); // serialize User -> JSON
        idempotencyRepository.save(record);
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
