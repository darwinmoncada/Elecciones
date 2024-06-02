package com.votaciones.Controllers;

import com.votaciones.Models.User;
import com.votaciones.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder al panel de administración.");
            return "redirect:/admin/login";
        }
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String manageStudents(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") != null) {
            return "admin/students";
        } else {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder a esta página.");
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/candidates")
    public String manageCandidates(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") != null) {
            return "admin/candidates";
        } else {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para acceder a esta página.");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAdmin(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>("Admin registrado exitosamente con ID: " + createdUser.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User userEncontrado = userService.findByUsernameAndPassword(username, password);
        if (userEncontrado != null) {
            session.setAttribute("admin", userEncontrado);
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/admin/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logoutRedirect() {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Ha cerrado sesión exitosamente.");
        return "redirect:/";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.getPassword().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Contraseña actual incorrecta.");
            return "redirect:/admin/dashboard";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las nuevas contraseñas no coinciden.");
            return "redirect:/admin/dashboard";
        }

        admin.setPassword(newPassword);
        userService.updateUser(admin);
        session.invalidate();  // Cerrar sesión para que el admin vuelva a iniciar sesión con la nueva contraseña
        return "redirect:/admin/login";
    }

    @PostMapping("/changeAdminInfo")
    public String changeAdminInfo(@RequestParam String currentPasswordAdmin, @RequestParam String newUsername, HttpSession session, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.getPassword().equals(currentPasswordAdmin)) {
            redirectAttributes.addFlashAttribute("error", "Contraseña actual incorrecta.");
            return "redirect:/admin/dashboard";
        }

        admin.setUsername(newUsername);
        userService.updateUser(admin);
        redirectAttributes.addFlashAttribute("message", "Nombre de usuario actualizado exitosamente.");
        return "redirect:/admin/dashboard";
    }
}
