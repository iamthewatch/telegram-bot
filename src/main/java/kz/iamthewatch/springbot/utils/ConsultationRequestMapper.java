package kz.iamthewatch.springbot.utils;

import kz.iamthewatch.springbot.dto.ConsultationDto;
import kz.iamthewatch.springbot.model.ConsultationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class ConsultationRequestMapper {

    public void upsertFields(ConsultationRequest target, ConsultationDto dto) {
        target.setPersonType(dto.personType());
        target.setCreditType(dto.creditType());
        target.setUsername(dto.username());
        target.setFirstname(dto.firstname());
        target.setLastname(dto.lastname());
        target.setCreatedAt(LocalDateTime.now());
    }
}