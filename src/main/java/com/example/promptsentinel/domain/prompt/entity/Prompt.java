package com.example.promptsentinel.domain.prompt.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "prompt")
@Data
@Builder

public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String strategy;

    @Column
    private String scenarioName;

    @Column
    private String question;

    @Column
    private String generatedPrompt;
}
