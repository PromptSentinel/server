package com.example.promptsentinel.domain.evalutation.dao;

import com.example.promptsentinel.domain.evalutation.Evaluation;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findById(Long id);
    Optional<List<Evaluation>> findByMember(Member member);

    boolean existsByPlaceGoogleId(String placeId);

    default Evaluation findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EVALUATION_NOT_FOUND));
    }
    default List<Evaluation> findByMemberOrElseThrow(Member member) {
        return findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.EVALUATION_NOT_FOUND));
    }
}
