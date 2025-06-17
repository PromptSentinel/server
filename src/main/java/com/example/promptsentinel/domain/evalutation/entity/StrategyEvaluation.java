package com.example.promptsentinel.domain.evalutation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "strategy_evaluation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @Column
    private String strategy;

    @Column
    private String RoBERTaLabelErrorCount;

    @Column
    private String DeBERTaLabelErrorCount;

    @Column
    private String BARTLabelErrorCount;

    @Column
    private String ELECTRALabelErrorCount;


}
