package kz.iamthewatch.springbot.service;

import java.time.LocalDateTime;
import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.repository.ConsultationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;

    public ConsultationRequest saveRequest(Long chatId, String personType, String creditType) {
        ConsultationRequest request = new ConsultationRequest();
        request.setChatId(chatId);
        request.setPersonType(personType);
        request.setCreditType(creditType);
        request.setCreatedAt(LocalDateTime.now());
        return consultationRequestRepository.save(request);
    }
}