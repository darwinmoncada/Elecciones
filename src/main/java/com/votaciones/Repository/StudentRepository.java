package com.votaciones.Repository;

import com.votaciones.Models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByDocumento(String documento);
}
