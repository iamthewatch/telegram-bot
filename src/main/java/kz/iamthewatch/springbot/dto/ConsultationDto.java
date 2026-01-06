package kz.iamthewatch.springbot.dto;

public record ConsultationDto(
        Long chatId,
        String username,
        String firstname,
        String lastname,
        String personType,
        String creditType
) {
}