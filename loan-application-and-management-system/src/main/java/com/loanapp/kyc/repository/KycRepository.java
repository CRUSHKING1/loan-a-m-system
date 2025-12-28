package com.loanapp.kyc.repository;

import com.loanapp.common.enums.KycStatus;
import com.loanapp.kyc.entity.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, Long> {

    Optional<Kyc> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Kyc> findByIdAndStatus(Long id, KycStatus status);

    boolean existsByUserIdAndStatus(Long userId, KycStatus status);

    List<Kyc> findByStatus(KycStatus status);
}
