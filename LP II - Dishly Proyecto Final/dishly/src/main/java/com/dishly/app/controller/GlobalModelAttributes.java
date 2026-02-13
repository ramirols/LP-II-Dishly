package com.dishly.app.controller;

import java.util.ArrayList;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addCartAttributes(Model model) {

        model.addAttribute("cartItems", new ArrayList<>());
        model.addAttribute("cartTotal", 0.0);
        model.addAttribute("cartCount", 0);
    }
}
