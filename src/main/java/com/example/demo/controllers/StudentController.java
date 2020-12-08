package com.example.demo.controllers;

import com.example.demo.error.CustomErrorResponse;
import com.example.demo.models.Ktp;
import com.example.demo.models.Student;
import com.example.demo.repository.KtpRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.PDFThymeleafExample;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController

@RequestMapping("/student")
public class StudentController {
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    @Qualifier("PDFThymeleafExample")
    PDFThymeleafExample PDFThymeleafExample;

    @Autowired
    @Qualifier("studentRepository")
    StudentRepository studentRepository;

    @Autowired
    @Qualifier("ktpRepository")
    KtpRepository ktpRepository;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Student> getAllUsers() {
        // This returns a JSON or XML with the users
        return studentRepository.findAll();
    }

    @GetMapping(path = "/pdfGenerator")
    public ResponseEntity<Student> pdfGenerator() throws IOException, DocumentException, ParseException {

        String html = PDFThymeleafExample.parseThymeleafTemplate();
        PDFThymeleafExample.generatePdfFromHtml(html);
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setMessage("Message nya cutom sendiri yaaa");
        errors.setStatus(HttpStatus.OK.value());

        return new ResponseEntity(errors, HttpStatus.OK);
    }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public ResponseEntity<Student> addNewStudent(@RequestParam String nama, @RequestParam String nik, @RequestParam long norek, @RequestParam String nik2, @RequestParam("file") MultipartFile file) throws ParseException {
        String fileName = fileStorageService.storeFile(file);

        //custom error instantiation
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());

        //check length of nik
        if (nik.length() == 16 && nik2.length() == 16 && !nik.equals(nik2)) {

            //check
            Optional<Ktp> ktpNikContaining = ktpRepository.findByNikContaining(nik);
            Optional<Ktp> ktpNikContaining2 = ktpRepository.findByNikContaining(nik2);
//
            if (ktpNikContaining.isPresent() && ktpNikContaining2.isPresent()) {

                //select to database by nik
                Optional<Student> nikContaining = studentRepository.findByNikContaining(nik);
                Optional<Student> nik2Containing = studentRepository.findByNik2Containing(nik2);
                Optional<Student> norekContaining = studentRepository.findStudentByNorek(norek);
//
//
//                //check nik exist
                if (nikContaining.isEmpty() && nik2Containing.isEmpty() && norekContaining.isEmpty() ) {
                    Date date = new Date();
                    Student n = new Student();
                    n.setNama(nama);
                    n.setNik(nik);
                    n.setNik2(nik2);
                    n.setNorek(norek);
                    n.setImage_url(fileName);
                    n.setCreated_at(new Timestamp(date.getTime()));
                    return new ResponseEntity<>(studentRepository.save(n), HttpStatus.OK);
                } else {

                    errors.setMessage("Nik1, Nik2, or Norek is exist");
                    errors.setStatus(HttpStatus.FOUND.value());

                    return new ResponseEntity(errors, HttpStatus.FOUND);
                }
            } else {

                errors.setMessage("Ktp not valid");
                errors.setStatus(HttpStatus.FOUND.value());

                return new ResponseEntity(errors, HttpStatus.FOUND);
            }
        } else {
            errors.setMessage("Error validate");
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
