package kz.iamthewatch.springbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kz.iamthewatch.springbot.model.ChatMessageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_PROVIDER_ERROR;
import static kz.iamthewatch.springbot.utils.PromptConstants.AI_SYSTEM_PROMPT;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatMessageHistoryService chatMessageHistoryService;
    private final LocalizationService localizationService;
    private final ChatModel chatModel;

    public String answerQuestion(Long chatId, String userPrompt, Locale locale) {
        try {
            Prompt prompt = buildPrompt(chatId, userPrompt, locale);
            String answer = getValidatedAnswer(prompt);
            chatMessageHistoryService.saveMessage(chatId, userPrompt, answer);

            return answer;
        } catch (Exception ex) {
            return localizationService.getLocalizedMessage(locale, AI_PROVIDER_ERROR);
        }
    }

    private Prompt buildPrompt(Long chatId, String userPrompt, Locale locale) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(localizationService.getLocalizedMessage(locale, AI_SYSTEM_PROMPT)));
        messages.addAll(getHistoryMessages(chatId));
        messages.add(new UserMessage(userPrompt));
        return new Prompt(messages);
    }

    private List<Message> getHistoryMessages(Long chatId) {
        List<ChatMessageHistory> history = chatMessageHistoryService.getLastMessages(chatId);
        List<Message> messageHistory = new ArrayList<>();

        for (ChatMessageHistory message : history) {
            messageHistory.add(new UserMessage(message.getUserMessage()));
            messageHistory.add(new AssistantMessage(message.getAssistantMessage()));
        }

        return messageHistory;
    }

    private String getValidatedAnswer(Prompt prompt) {
        String answer = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getText();

        if (answer == null || answer.isBlank()) {
            throw new IllegalStateException("AI provider returned empty response");
        }

        return answer.trim();
    }
}