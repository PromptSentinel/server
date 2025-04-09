package com.example.promptsentinel.domain.member.dto;

import com.example.promptsentinel.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String memberEmail);

    Optional<Member> findByProviderId(String providerId);

}