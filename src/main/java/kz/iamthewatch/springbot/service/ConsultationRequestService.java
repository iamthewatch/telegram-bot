package kz.iamthewatch.springbot.service;

import java.util.Optional;

import kz.iamthewatch.springbot.dto.ConsultationDto;
import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.repository.ConsultationRequestRepository;
import kz.iamthewatch.springbot.utils.ConsultationRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultationRequestMapper consultationRequestMapper;

    public Page<ConsultationRequest> getAllActive(Pageable pageable) {
        return consultationRequestRepository.findAllByIsActiveTrue(pageable);
    }

    @Transactional
    public void setInactiveConsultationRequest(Long id) {
        consultationRequestRepository.deactivateById(id);
    }

    @Transactional
    public void saveOrUpdateRequest(ConsultationDto dto) {
        ConsultationRequest request = findByChatId(dto.chatId())
                .orElseGet(() -> createNew(dto.chatId()));
        consultationRequestMapper.upsertFields(request, dto);
        consultationRequestRepository.save(request);
    }

    private Optional<ConsultationRequest> findByChatId(Long chatId) {
        return consultationRequestRepository.findByChatIdAndIsActiveTrue(chatId);
    }

    private ConsultationRequest createNew(Long chatId) {
        ConsultationRequest request = new ConsultationRequest();
        request.setChatId(chatId);
        request.setActive(true);
        return request;
    }
}