package com.example.demo.controllers;

import com.example.demo.error.CustomErrorResponse;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController

@RequestMapping("/student")
public class StudentController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    @Qualifier("fileStorageService")
    FileStorageService fileStorageService;

    @Autowired
    @Qualifier("studentRepository")
    StudentRepository studentRepository;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Student> getAllUsers() {
        // This returns a JSON or XML with the users
        return studentRepository.findAll();
    }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public ResponseEntity<Student> addNewStudent(@RequestParam String nama, @RequestParam String nik, @RequestParam("file") MultipartFile file) throws ParseException {
        String fileName = fileStorageService.storeFile(file);

        //custom error instantiation
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());

        //check length of nik
        if (nik.length() == 16) {
            //select to database by nik
            Optional<Student> nikContaining = studentRepository.findByNikContaining(nik);

            //check nik exist
            if (!nikContaining.isPresent()) {
                Date date = new Date();
                Student n = new Student();
                n.setNama(nama);
                n.setNik(nik);
                n.setImage_url(fileName);
                n.setCreated_at(new Timestamp(date.getTime()));
                return new ResponseEntity<>(studentRepository.save(n), HttpStatus.OK);
            } else {
                errors.setMessage("Nik is exist");
                errors.setStatus(HttpStatus.FOUND.value());

                return new ResponseEntity(errors, HttpStatus.FOUND);
            }
        } else {
            errors.setMessage("Error validate, nik length must to 16");
            errors.setStatus(HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<Student> editStudent(@PathVariable("id") long id, @RequestParam("file") MultipartFile file, @RequestParam("created_at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date created_at) throws ParseException {
        String fileName = fileStorageService.storeFile(file);
        Optional<Student> studentData = studentRepository.findById(id);

//        return fileName;
        if (studentData.isPresent()) {
            Student n = studentData.get();
            n.setImage_url(fileName);
            n.setNama(n.getNama());
            n.setCreated_at(created_at);
            return new ResponseEntity<>(studentRepository.save(n), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
