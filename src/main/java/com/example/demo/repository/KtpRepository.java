package com.example.demo.repository;

import com.example.demo.models.Ktp;
import com.example.demo.models.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface KtpRepository extends CrudRepository<Ktp, Long> {

    Optional<Ktp> findByNikContaining(String nik);
}

