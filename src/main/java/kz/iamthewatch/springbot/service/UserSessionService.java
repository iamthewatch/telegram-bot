package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.enums.LanguageCode;
import kz.iamthewatch.springbot.model.UserSession;
import kz.iamthewatch.springbot.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Transactional
    public Locale getLocale(Long chatId) {
        return Locale.forLanguageTag(getOrCreateUserSession(chatId).getLocale());
    }

    @Transactional
    public String getConsultationPersonType(Long chatId) {
        return getOrCreateUserSession(chatId).getConsultationPersonType();
    }

    @Transactional
    public String getConsultationCreditType(Long chatId) {
        return getOrCreateUserSession(chatId).getConsultationCreditType();
    }

    @Transactional
    public void setLocale(Long chatId, LanguageCode locale) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setLocale(locale.getLocale());
        userSessionRepository.save(userSession);
    }

    @Transactional
    public void setConsultationPersonType(Long chatId, String personType) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setConsultationPersonType(personType);
        userSessionRepository.save(userSession);
    }

    @Transactional
    public void setConsultationCreditType(Long chatId, String creditType) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setConsultationCreditType(creditType);
        userSessionRepository.save(userSession);
    }

    private UserSession getOrCreateUserSession(Long chatId) {
        Optional<UserSession> userSession = userSessionRepository.findByChatId(chatId);

        return userSession.orElseGet(() -> {
            UserSession newUserSession = new UserSession();
            newUserSession.setChatId(chatId);
            newUserSession.setLocale(LanguageCode.LANG_RU.getLocale());
            return userSessionRepository.save(newUserSession);
        });
    }
}