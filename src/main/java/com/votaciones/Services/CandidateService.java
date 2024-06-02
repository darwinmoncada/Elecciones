package com.votaciones.Services;

import com.votaciones.Models.Candidate;
import com.votaciones.Repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public void addVote(String candidateId) {
        Candidate candidate = getCandidateById(candidateId);
        if (candidate != null) {
            candidate.setVotos(candidate.getVotos() + 1);
            candidateRepository.save(candidate);
        }
    }

    public void saveCandidate(Candidate candidate) {
        candidateRepository.save(candidate); // Guardar cambios en la base de datos
    }

    public Candidate getWinnerByCategory(String category) {
        return candidateRepository.findTopByCategoriaOrderByVotosDesc(category);
    }

    public List<Candidate> getAllCandidatesExceptWinners() {
        // Obtener el ganador de Personería
        Candidate ganadorPersoneria = getWinnerByCategory("Personería");
        // Obtener el ganador de Secretario
        Candidate ganadorSecretario = getWinnerByCategory("Secretario");

        // Obtener todos los candidatos
        List<Candidate> allCandidates = getAllCandidates();

        // Crear una nueva lista para almacenar los candidatos excepto los ganadores
        List<Candidate> candidatesExceptWinners = allCandidates;

        // Remover los ganadores de Personería y Secretario de la lista
        candidatesExceptWinners.remove(ganadorPersoneria);
        candidatesExceptWinners.remove(ganadorSecretario);

        return candidatesExceptWinners;
    }

    public List<Candidate> getAllCandidatesByCategory(String category) {
        return candidateRepository.findByCategoriaOrderByVotosDesc(category);
    }
}
