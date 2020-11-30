package com.example.demo.repository;

import com.example.demo.models.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    Optional<Student> findByNikContaining(String nik);

    Optional<Student> findByNik2Containing(String nik2);

    Optional<Student> findByNorekContaining(String norek);
}
