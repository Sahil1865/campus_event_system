package com.sahil.campus_event_system.controller;

import com.sahil.campus_event_system.dto.UserRequest;
import com.sahil.campus_event_system.dto.UserResponse;
import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        try {
            UserResponse saved = userService.registerUser(request);
            return ResponseEntity.ok(Map.of(
                    "message", "Registration successful",
                    "user", saved
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Login (clean)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<User> optionalUser = userService.login(email, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "userId", user.getUserId(),
                    "userName", user.getUserName(),
                    "role", user.getUserRole(),
                    "email", user.getEmail()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
    }

    // ✅ Get a user by ID
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(user);
    }

    // ✅ Delete user
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
