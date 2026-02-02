package kz.iamthewatch.springbot.controller;

import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.service.ConsultationRequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultation-requests")
@AllArgsConstructor
public class ConsultationController {

    private final ConsultationRequestService consultationRequestService;

    @GetMapping
    public ResponseEntity<Page<ConsultationRequest>> getAllActive(Pageable pageable) {
        return ResponseEntity.ok(consultationRequestService.getAllActive(pageable));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateConsultationRequest(@PathVariable Long id) {
        consultationRequestService.setInactiveConsultationRequest(id);
        return ResponseEntity.noContent().build();
    }
}