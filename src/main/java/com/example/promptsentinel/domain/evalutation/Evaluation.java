package com.example.promptsentinel.domain.evalutation;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.domain.model.entity.LLMModel;
import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
@Builder
public class Evaluation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "llm_model_id")
    private LLMModel llmModel;

    @Column
    private Double percentage;


    @OneToMany
    private List<CsvData> promptEntity;
}
