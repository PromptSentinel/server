package com.example.promptsentinel.domain.model.entity;

import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "llm_model")
@Getter
@Setter
@NoArgsConstructor
public class LLMModel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String modelUrl;

    @Column
    private String modelName;
    @Column
    private String APIKey;

    @Column(length = 1000)
    private List<String> headerList;

    @Column(length = 5000)
    private String requestFormat;

    @Column(length = 5000)
    private String responseFormat;

    @Column
    private String attributeName;

    @Builder
    public LLMModel(Member member, String modelName, String modelUrl,  String APIKey, List<String> headerList, String requestFormat, String responseFormat, String attributeName) {
        this.member = member;
        this.modelName = modelName;
        this.modelUrl = modelUrl;
        this.APIKey = APIKey;
        this.headerList = List.copyOf(headerList);
        this.requestFormat = requestFormat;
        this.responseFormat = responseFormat;
        this.attributeName = attributeName;
    }


}

