package com.votaciones.Services;

import com.votaciones.Models.Student;
import com.votaciones.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student getStudentByDocumento(String documento) {
        return studentRepository.findByDocumento(documento);
    }

    public void markStudentAsVoted(String id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setYaVoto(true);
            studentRepository.save(student); // Guardar cambios en la base de datos
        }
    }

    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student); // Guardar cambios en la base de datos
    }
}
