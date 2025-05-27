package com.example.promptsentinel.domain.prompt.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prompt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String question;
}
