package com.example.demo.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "students"
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nama")
    private String nama;

    @Column(name = "nik")
    private String nik;

    @Column(name = "nik2")
    private String nik2;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "norek")
    private long norek;

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

    public String getNik2() {
        return nik2;
    }

    public void setNik2(String nik2) {
        this.nik2 = nik2;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public long getNorek() {
        return norek;
    }

    public void setNorek(long norek) {
        this.norek = norek;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", nik='" + nik + '\'' +
                ", nik2='" + nik2 + '\'' +
                ", image_url='" + image_url + '\'' +
                ", created_at=" + created_at +
                ", norek=" + norek +
                '}';
    }

    public Student() {
    }

    public Student(long id, String nama, String nik, String nik2, String image_url, Date created_at, Integer norek) {
        this.id = id;
        this.nama = nama;
        this.nik = nik;
        this.nik2 = nik2;
        this.image_url = image_url;
        this.created_at = created_at;
        this.norek = norek;
    }

    public Student(String nama, String nik, String nik2, String image_url, Date created_at, Integer norek) {
        this.nama = nama;
        this.nik = nik;
        this.nik2 = nik2;
        this.image_url = image_url;
        this.created_at = created_at;
        this.norek = norek;
    }
}
