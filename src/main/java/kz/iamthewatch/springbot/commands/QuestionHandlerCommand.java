package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.AIService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXIT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@AllArgsConstructor
public class QuestionHandlerCommand implements Command {

    private final AIService aiService;
    private final MessageService messageService;
    private final UserSessionService userSessionService;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = update.getMessage().getChatId();
        UserState userState = userSessionService.getUserState(chatId);
        if (!userState.equals(UserState.AI_CHAT)) {
            return false;
        }

        String messageText = getMessageText(update);
        String exitText = localizationService.getLocalizedMessage(chatId, AI_CHAT_EXIT);
        return !messageText.equals(exitText);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String question = getMessageText(update);
        Locale locale = userSessionService.getLocale(chatId);

        String answer = aiService.answerQuestion(chatId, question, locale);

        messageService.sendMessage(chatId, answer);
    }

    @Override
    public String getCommand() {
        return CommandName.QUESTION_HANDLER.name();
    }
}