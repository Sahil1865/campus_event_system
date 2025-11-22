package com.sahil.campus_event_system.controller;

import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getUserPass())) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password!");
            return "redirect:/login";
        }

        // Store logged-in user in session
        session.setAttribute("loggedUser", user);
        return "redirect:/events";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
