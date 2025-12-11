package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.model.UserSession;
import kz.iamthewatch.springbot.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public Locale getLocale(Long chatId) {
        return Locale.forLanguageTag(getOrCreateUserSession(chatId).getLocale());
    }

    public void setLocale(Long chatId, String locale) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setLocale(locale);
        userSessionRepository.save(userSession);
    }

    public void startConsultationFlow(Long chatId, String personType) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setConsultationPersonType(personType);
        userSession.setConsultationCreditType(null);
        userSession.setAwaitingConsultationClientType(false);
        userSession.setAwaitingConsultationCreditType(true);
        userSession.setAwaitingConsultationConfirmation(false);
        userSessionRepository.save(userSession);
    }

    public void selectCreditType(Long chatId, String creditType) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setConsultationCreditType(creditType);
        userSession.setAwaitingConsultationCreditType(false);
        userSession.setAwaitingConsultationConfirmation(true);
        userSessionRepository.save(userSession);
    }

    public boolean isAwaitingConsultationCreditType(Long chatId) {
        UserSession userSession = getOrCreateUserSession(chatId);
        return Boolean.TRUE.equals(userSession.getAwaitingConsultationCreditType());
    }

    public boolean isAwaitingConsultationConfirmation(Long chatId) {
        UserSession userSession = getOrCreateUserSession(chatId);
        return Boolean.TRUE.equals(userSession.getAwaitingConsultationConfirmation());
    }

    public String getConsultationPersonType(Long chatId) {
        return getOrCreateUserSession(chatId).getConsultationPersonType();
    }

    public String getConsultationCreditType(Long chatId) {
        return getOrCreateUserSession(chatId).getConsultationCreditType();
    }

    public void clearConsultationFlow(Long chatId) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setConsultationPersonType(null);
        userSession.setConsultationCreditType(null);
        userSession.setAwaitingConsultationClientType(false);
        userSession.setAwaitingConsultationCreditType(false);
        userSession.setAwaitingConsultationConfirmation(false);
        userSessionRepository.save(userSession);
    }

    private UserSession getOrCreateUserSession(Long chatId) {
        Optional<UserSession> userSession = userSessionRepository.findByChatId(chatId);

        return userSession.orElseGet(() -> {
            UserSession newUserSession = new UserSession();
            newUserSession.setChatId(chatId);
            newUserSession.setLocale("ru");
            newUserSession.setAwaitingConsultationClientType(false);
            newUserSession.setAwaitingConsultationCreditType(false);
            newUserSession.setAwaitingConsultationConfirmation(false);
            return userSessionRepository.save(newUserSession);
        });
    }
}