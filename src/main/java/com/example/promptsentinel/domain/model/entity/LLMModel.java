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

    @Column
    private List<String> headerList;

    @Column
    private String requestFormat;

    @Column
    private String responseFormat;

    @Builder
    public LLMModel(Member member, String modelName, String APIKey, List<String> headerList, String requestFormat, String responseFormat) {
        this.member = member;
        this.modelName = modelName;
        this.APIKey = APIKey;
        this.headerList = List.copyOf(headerList);
        this.requestFormat = requestFormat;
        this.responseFormat = responseFormat;
    }

}

