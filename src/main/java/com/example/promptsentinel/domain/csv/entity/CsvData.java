package com.example.promptsentinel.domain.csv.entity;

import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CSVData")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CsvData extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String strategy;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column
    private int RoBERTaLabel;

    @Column
    private int DeBERTaLabel;

    @Column
    private int BARTLabel;

    @Column
    private int ELECTRALabel;

    public CsvData(String strategy, String question, String response,  int RoBERTaLabel, int DeBERTaLabel, int BARTLabel, int ELECTRALabel){
        this.strategy = strategy;
        this.question = question;
        this.response = response;
        this.RoBERTaLabel = RoBERTaLabel;
        this.DeBERTaLabel = DeBERTaLabel;
        this.BARTLabel = BARTLabel;
        this.ELECTRALabel = ELECTRALabel;

    }
}

