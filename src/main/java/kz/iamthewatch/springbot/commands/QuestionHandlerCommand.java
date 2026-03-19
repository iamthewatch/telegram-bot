package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.AIService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXIT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
public class QuestionHandlerCommand extends AbstractStateMessageCommand {

    private final AIService aiService;
    private final MessageService messageService;
    private final LocalizationService localizationService;

    public QuestionHandlerCommand(
            AIService aiService,
            MessageService messageService,
            UserSessionService userSessionService,
            LocalizationService localizationService
    ) {
        super(userSessionService);
        this.aiService = aiService;
        this.messageService = messageService;
        this.localizationService = localizationService;
    }

    @Override
    protected UserState requiredState() {
        return UserState.AI_CHAT;
    }

    @Override
    protected boolean additionalCondition(Update update, Long chatId) {
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