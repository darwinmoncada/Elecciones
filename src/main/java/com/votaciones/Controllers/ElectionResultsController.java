package com.votaciones.Controllers;

import com.votaciones.Models.Candidate;
import com.votaciones.Services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ElectionResultsController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/resultados")
    public String mostrarResultados(Model model) {
        // Obtener el ganador de Personería
        Candidate ganadorPersoneria = candidateService.getWinnerByCategory("Personería");
        // Obtener el ganador de Secretario
        Candidate ganadorSecretario = candidateService.getWinnerByCategory("Secretario");

        // Obtener la lista de secretarios y personeros ordenados por votos
        List<Candidate> secretarios = candidateService.getAllCandidatesByCategory("Secretario");
        List<Candidate> personeros = candidateService.getAllCandidatesByCategory("Personería");

        model.addAttribute("ganadorPersoneria", ganadorPersoneria);
        model.addAttribute("ganadorSecretario", ganadorSecretario);
        model.addAttribute("secretarios", secretarios);
        model.addAttribute("personeros", personeros);

        return "componet/electionResults";
    }
}
