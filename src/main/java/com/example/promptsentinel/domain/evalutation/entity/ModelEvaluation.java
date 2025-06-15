package com.example.promptsentinel.domain.evalutation.entity;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "model_evaluation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelEvaluation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
