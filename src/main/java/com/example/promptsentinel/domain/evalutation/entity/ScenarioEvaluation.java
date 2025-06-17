package com.example.promptsentinel.domain.evalutation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario_evaluation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;


//    @ManyToOne
//    @JoinColumn(name = "strategy_evaluation_id")
//    private StrategyEvaluation strategyEvaluation;

    @Column
    private String scenarioName;

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
