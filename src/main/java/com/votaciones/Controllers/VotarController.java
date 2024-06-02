package com.votaciones.Controllers;

import com.votaciones.Models.Candidate;
import com.votaciones.Models.Student;
import com.votaciones.Services.CandidateService;
import com.votaciones.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/votar")
public class VotarController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/iniciar")
    public String iniciarSesion(@RequestParam String documento, Model model) {
        Student student = studentService.getStudentByDocumento(documento);
        if (student == null) {
            model.addAttribute("mensaje", "Estudiante no encontrado");
            return "index";
        } else if (student.isYaVoto()) {
            // Mostrar modal de alerta con el mensaje de error
            model.addAttribute("alertMessage", "Estudiante ya ha votado");
            model.addAttribute("alertType", "alert-danger"); // Esta línea agrega la clase 'alert-danger'
            return "index";
        } else {
            // Redirigir al estudiante a la página de votación incluyendo su ID en la URL
            return "redirect:/votar/iniciar/" + student.getId();
        }
    }

    // Método para manejar la solicitud GET para /votar/iniciar/{studentId}
    @GetMapping("/iniciar/{studentId}")
    public String mostrarFormularioVotacion(@PathVariable String studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        if (student == null || student.isYaVoto()) {
            // Manejar el caso en el que el estudiante no existe o ya ha votado
            model.addAttribute("mensaje", "El estudiante no existe o ya ha votado.");
            return "index";
        } else {
            // Obtener la lista de candidatos y pasarla a la vista
            model.addAttribute("student", student);
            model.addAttribute("candidates", candidateService.getAllCandidates());
            return "seleccionar_candidatos";
        }
    }


    @PostMapping("/votar")
    public String votar(@RequestParam String studentId, @RequestParam String personeria, @RequestParam String secretario, Model model) {
        Student student = studentService.getStudentById(studentId);
        Candidate candidatoPersoneria = candidateService.getCandidateById(personeria);
        Candidate candidatoSecretario = candidateService.getCandidateById(secretario);

        if (student != null && candidatoPersoneria != null && candidatoSecretario != null && !student.isYaVoto()) {
            // Busca el candidato de personería por su ID y aumenta el contador de votos
            candidatoPersoneria.setVotos(candidatoPersoneria.getVotos() + 1);
            candidateService.saveCandidate(candidatoPersoneria);

            // Busca el candidato de secretario por su ID y aumenta el contador de votos
            candidatoSecretario.setVotos(candidatoSecretario.getVotos() + 1);
            candidateService.saveCandidate(candidatoSecretario);

            // Cambia el estado del estudiante a yaVoto = true
            student.setYaVoto(true);
            studentService.saveStudent(student);

            // Redirecciona al index después de registrar el voto
        }
        return "redirect:/";

    }

}





