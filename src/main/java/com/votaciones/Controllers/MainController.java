package com.votaciones.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index"; // Este es el nombre de la vista que quieres mostrar, asegúrate de que coincida con el nombre del archivo HTML.
    }
    @GetMapping("/admin/das")
    public String adminDashboard(@RequestParam(value = "componentName", required = false) String componentName, Model model) {
        if (componentName != null) {
            model.addAttribute("componentName", componentName);
        }
        return "admin/dashboard";
    }

    @GetMapping("/loadComponent")
    public String loadComponent(@RequestParam String componentName, Model model) {
        return "componet/" + componentName;
    }
}