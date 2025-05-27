package com.example.promptsentinel.domain.csv.entity;

import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CSVData")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvData extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(length = 50)
    private String label;

    public CsvData(String question, String response, String label){
        this.question = question;
        this.response = response;
        this.label = label;

    }
}

