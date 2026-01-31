package kz.iamthewatch.springbot.controller;

import kz.iamthewatch.springbot.model.ConsultationRequest;
import kz.iamthewatch.springbot.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consultation")
@AllArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping()
    public ResponseEntity<List<ConsultationRequest>> getAllConsultationRequest() {
        return ResponseEntity.ok(consultationService.getAll());
    }
}