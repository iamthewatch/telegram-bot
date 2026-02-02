package kz.iamthewatch.springbot.repository;

import kz.iamthewatch.springbot.model.ConsultationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Long> {

    Optional<ConsultationRequest> findByChatIdAndIsActiveTrue(Long chatId);

    Page<ConsultationRequest> findAllByIsActiveTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ConsultationRequest cr set cr.isActive = false where cr.id = :id")
    void deactivateById(@Param("id") Long id);
}