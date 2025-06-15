package com.example.promptsentinel.domain.prompt.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prompt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String strategy;

    @Column
    private String scenarioName;

    @Column(length = 1000)
    private String question;

    @Column(length = 10000)
    private String generatedPrompt;
}
