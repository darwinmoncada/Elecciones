package com.votaciones.Controllers;

import com.votaciones.Models.Candidate;
import com.votaciones.Repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;
    @GetMapping("/listar_candidatos")
    public String listarCandidatos(Model model) {
        List<Candidate> candidatos = candidateRepository.findAll();
        model.addAttribute("candidatos", candidatos);
        return "componet/listar_candidatos";
    }
    @PostMapping("/registerCandidate")
    public String registerCandidate(@RequestParam("nombreCandidato") String nombre,
                                    @RequestParam("eslogan") String eslogan,
                                    @RequestParam("categoria") String categoria,
                                    @RequestParam("fotoCandidato") MultipartFile foto) {
        try {
            Candidate candidate = new Candidate();
            candidate.setNombre(nombre);
            candidate.setEslogan(eslogan);
            candidate.setCategoria(categoria);

            // Convertir la foto a Base64
            String fotoBase64 = Base64.getEncoder().encodeToString(foto.getBytes());
            candidate.setFotoBase64(fotoBase64);

            candidateRepository.save(candidate);
            return "redirect:/admin/listar_candidatos";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/registerCandidate?error";
        }
    }


    @GetMapping("/editCandidate/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            model.addAttribute("candidate", candidateOptional.get());
            return "componet/updateCandidate";
        } else {
            return "redirect:/admin/listar_candidatos";
        }
    }
    @PostMapping("/updateCandidate/{id}")
    public String editCandidate(){
        return "redirect:/admin/listar_candidatos";
    }



    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable String id) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            candidateRepository.deleteById(id);
        }
        return "redirect:/admin/listar_candidatos";
    }


}

