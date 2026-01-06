package kz.iamthewatch.springbot.service;

import java.time.LocalDateTime;

import kz.iamthewatch.springbot.dto.ConsultationDto;
import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.repository.ConsultationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;

    public void saveRequest(ConsultationDto dto) {
        ConsultationRequest request = new ConsultationRequest();
        request.setChatId(dto.chatId());
        request.setPersonType(dto.personType());
        request.setCreditType(dto.creditType());
        request.setUsername(dto.username());
        request.setFirstname(dto.firstname());
        request.setLastname(dto.lastname());
        request.setCreatedAt(LocalDateTime.now());
        consultationRequestRepository.save(request);
    }
}