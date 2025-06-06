package com.example.promptsentinel.domain.member.dao;

import com.example.promptsentinel.domain.evalutation.Evaluation;
import com.example.promptsentinel.domain.member.entity.Member;
import com.example.promptsentinel.global.common.error.CustomException;
import com.example.promptsentinel.global.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String memberEmail);


    default Member findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

}