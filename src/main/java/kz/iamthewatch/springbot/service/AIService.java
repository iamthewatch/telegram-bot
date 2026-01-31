package kz.iamthewatch.springbot.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_PROVIDER_ERROR;
import static kz.iamthewatch.springbot.utils.PromptConstants.AI_SYSTEM_PROMPT;

@Service
@AllArgsConstructor
public class AIService {

    private final LocalizationService localizationService;
    private final ChatClient chatClient;

    public String answerQuestion(String userPrompt, Locale locale) {
        try {
            String systemPrompt = localizationService.getLocalizedMessage(locale, AI_SYSTEM_PROMPT);
            return chatClient.prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();
        } catch (Exception ex) {
            return localizationService.getLocalizedMessage(locale, AI_PROVIDER_ERROR);
        }
    }
}