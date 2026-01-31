package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.repository.ConsultationRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConsultationService {

    private final ConsultationRequestRepository consultationRequestRepository;

    public List<ConsultationRequest> getAll() {
        return consultationRequestRepository.findAll();
    }
}