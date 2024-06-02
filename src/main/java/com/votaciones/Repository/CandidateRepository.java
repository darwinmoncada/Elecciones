package com.votaciones.Repository;

import com.votaciones.Models.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Candidate findTopByCategoriaOrderByVotosDesc(String category);

    List<Candidate> findByCategoriaOrderByVotosDesc(String category);
}