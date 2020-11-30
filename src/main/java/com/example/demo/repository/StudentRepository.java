package com.example.demo.repository;

import com.example.demo.models.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    List<Student> findByNamaContaining(String nama);
    Optional<Student> findByNikContaining(String nik);
}
