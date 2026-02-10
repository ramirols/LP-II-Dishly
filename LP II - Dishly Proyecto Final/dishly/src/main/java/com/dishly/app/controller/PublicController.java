package com.dishly.app.controller;

import com.dishly.app.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    private final UsuarioService usuarioService;

    public PublicController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String home() {
        return "public/home";
    }

    @GetMapping("/menu")
    public String menu() {
        return "public/menu";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "public/register";
    }
    
    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ) {
        usuarioService.registrar(name, email, password);
        return "redirect:/login";
    }
}