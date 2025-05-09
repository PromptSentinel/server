package com.example.promptsentinel.domain.member.dao;

import com.example.promptsentinel.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String memberEmail);

    Optional<Member> findByProviderId(String providerId);

}