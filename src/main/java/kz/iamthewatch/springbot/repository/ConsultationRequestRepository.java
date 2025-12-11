package kz.iamthewatch.springbot.repository;

import kz.iamthewatch.springbot.model.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Long> {
}