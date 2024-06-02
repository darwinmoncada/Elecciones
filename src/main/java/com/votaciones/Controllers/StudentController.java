package com.votaciones.Controllers;


import com.votaciones.Models.Student;
import com.votaciones.Repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/list")
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "componet/listStudents";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "componet/registerStudent";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "componet/registerStudent";
        } else {
            return "redirect:/admin/students/list";
        }
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, @RequestParam("isEdit") String isEdit) {
        if ("true".equals(isEdit)) {
            // Actualizar un estudiante existente
            studentRepository.save(student);
        } else {
            // Insertar un nuevo estudiante (la base de datos genera el ID automáticamente)
            student.setId(null); // Asegurarse de que el ID sea nulo para que se genere automáticamente
            studentRepository.save(student);
        }
        return "redirect:/admin/students/list";
    }


    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            List<Student> students = new ArrayList<>();

            boolean firstRow = true; // Variable para identificar la primera fila

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Saltar la fila de encabezado
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                Student student = new Student();

                Cell nombreCell = row.getCell(0);
                Cell documentoCell = row.getCell(1);

                // Obtener el valor de la celda para "nombre"
                if (nombreCell != null) {
                    if (nombreCell.getCellType() == CellType.STRING) {
                        student.setNombre(nombreCell.getStringCellValue());
                    } else if (nombreCell.getCellType() == CellType.NUMERIC) {
                        student.setNombre(String.valueOf(nombreCell.getNumericCellValue()));
                    }
                }

                // Obtener el valor de la celda para "documento"
                if (documentoCell != null) {
                    if (documentoCell.getCellType() == CellType.STRING) {
                        student.setDocumento(documentoCell.getStringCellValue());
                    } else if (documentoCell.getCellType() == CellType.NUMERIC) {
                        student.setDocumento(String.format("%.0f", documentoCell.getNumericCellValue()));
                    }
                }

                students.add(student);
            }

            studentRepository.saveAll(students);

            return "redirect:/admin/students/list";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/students/upload?error=true";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        studentRepository.deleteById(id);
        return "redirect:/admin/students/list";
    }
}
