package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.AIService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@AllArgsConstructor
public class QuestionHandlerCommand implements Command {

    private final AIService aiService;
    private final MessageService messageService;
    private final UserSessionService userSessionService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = update.getMessage().getChatId();
        UserState userState = userSessionService.getUserState(chatId);
        return userState.equals(UserState.WAITING_FOR_ANSWER);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String question = getMessageText(update);
        Locale locale = userSessionService.getLocale(chatId);

        String answer = aiService.answerQuestion(question, locale);

        userSessionService.setUserState(chatId, UserState.IDLE);
        messageService.sendMessage(chatId, answer);
    }

    @Override
    public String getCommand() {
        return CommandName.QUESTION_HANDLER.name();
    }
}