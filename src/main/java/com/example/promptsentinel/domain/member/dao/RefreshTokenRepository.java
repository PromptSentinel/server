package com.example.promptsentinel.domain.member.dao;

import com.example.promptsentinel.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);


    boolean existsByMemberId(Long memberId);

}
