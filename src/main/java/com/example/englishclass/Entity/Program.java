package com.example.englishclass.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@Table(name="Program")
@Getter
@Setter
public class Program {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String program;

    @Column
    private String programTerm;

    @Column
    private int price;

    @Column
    private String UserEmail;
}
