package com.dishly.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {


    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {

        return "cliente/carrito";
    }

    @GetMapping("/carrito/agregar/{id}")
    public String agregar(@PathVariable Long id, HttpSession session) {

        return "redirect:/menu";
    }

    // DATOS PARA EL HEADER
    @ModelAttribute
    public void header(HttpSession session, Model model) {

    }
}