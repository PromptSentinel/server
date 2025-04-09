package com.example.promptsentinel.domain.member.dto;

import com.example.promptsentinel.domain.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);


    boolean existsByMemberId(Long memberId);
}
