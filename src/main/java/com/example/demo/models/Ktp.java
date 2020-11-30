package com.example.demo.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "ktp",
        uniqueConstraints = {
                @UniqueConstraint(name = "nik", columnNames = {"nik"})
        }
)
public class Ktp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nama")
    private String nama;

    @Column(name = "nik")
    private String nik;
//
//    @ManyToOne
//    @JoinColumn(name = "ktp", insertable = false, updatable = false)
//    private Set<Student> students;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

//    public Set<Student> getStudents() {
//        return students;
//    }
//
//    public void setStudents(Set<Student> students) {
//        this.students = students;
//    }
}
