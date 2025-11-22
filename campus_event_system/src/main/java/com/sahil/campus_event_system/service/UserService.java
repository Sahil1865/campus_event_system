package com.sahil.campus_event_system.service;

import com.sahil.campus_event_system.dto.UserRequest;
import com.sahil.campus_event_system.dto.UserResponse;
import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setUserName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserContact(request.getPhoneNumber());
        user.setUserPass(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    /**
     * ✅ Clean login logic
     * Returns the user if credentials are correct, otherwise returns empty.
     */
    public Optional<User> login(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(rawPassword, user.getUserPass())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setUserId(user.getUserId());
        res.setName(user.getUserName());
        res.setEmail(user.getEmail());
        res.setRole(user.getUserRole());
        res.setPhoneNumber(user.getUserContact());
        return res;
    }
}
