package com.example.promptsentinel.domain.evalutation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PromptPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String prompt;

    @Column
    private String response;

    //나중에 enum으로 바꿀 예정
    @Column
    private String attackType;

    @Column
    private boolean evaluationResult;

}
