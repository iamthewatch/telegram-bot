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

    private UserSession getOrCreateUserSession(Long chatId) {
        Optional<UserSession> userSession = userSessionRepository.findByChatId(chatId);

        return userSession.orElseGet(() -> {
            UserSession newUserSession = new UserSession();
            newUserSession.setChatId(chatId);
            newUserSession.setLocale("ru");
            return userSessionRepository.save(newUserSession);
        });
    }
}